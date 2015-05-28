/*
 * Copyright 2010-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.resolve.calls.inference

import org.jetbrains.kotlin.builtins.KotlinBuiltIns
import org.jetbrains.kotlin.descriptors.TypeParameterDescriptor
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.resolve.calls.inference.ConstraintSystemImpl.ConstraintKind.EQUAL
import org.jetbrains.kotlin.resolve.calls.inference.ConstraintSystemImpl.ConstraintKind.SUB_TYPE
import org.jetbrains.kotlin.resolve.calls.inference.TypeBounds.Bound
import org.jetbrains.kotlin.resolve.calls.inference.TypeBounds.BoundKind.EXACT_BOUND
import org.jetbrains.kotlin.resolve.calls.inference.TypeBounds.BoundKind.LOWER_BOUND
import org.jetbrains.kotlin.resolve.calls.inference.TypeBounds.BoundKind.UPPER_BOUND
import org.jetbrains.kotlin.resolve.calls.inference.constraintPosition.CompoundConstraintPosition
import org.jetbrains.kotlin.resolve.calls.inference.constraintPosition.ConstraintPosition
import org.jetbrains.kotlin.resolve.calls.inference.constraintPosition.ConstraintPositionKind.TYPE_BOUND_POSITION
import org.jetbrains.kotlin.resolve.scopes.JetScope
import org.jetbrains.kotlin.types.*
import org.jetbrains.kotlin.types.TypeUtils.DONT_CARE
import org.jetbrains.kotlin.types.TypeProjectionImpl
import org.jetbrains.kotlin.types.TypeSubstitutor
import org.jetbrains.kotlin.types.ErrorUtils
import org.jetbrains.kotlin.types.ErrorUtils.FunctionPlaceholderTypeConstructor
import org.jetbrains.kotlin.types.Variance
import org.jetbrains.kotlin.builtins.KotlinBuiltIns
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.resolve.calls.inference.ConstraintSystemImpl.ConstraintKind
import org.jetbrains.kotlin.types.checker.TypeCheckingProcedure
import org.jetbrains.kotlin.types.checker.TypeCheckingProcedureCallbacks
import java.util.ArrayList
import java.util.HashMap
import java.util.LinkedHashMap
import org.jetbrains.kotlin.resolve.calls.inference.constraintPosition.ConstraintPosition
import org.jetbrains.kotlin.resolve.calls.inference.constraintPosition.ConstraintPositionKind.*
import org.jetbrains.kotlin.resolve.calls.inference.constraintPosition.CompoundConstraintPosition
import org.jetbrains.kotlin.types.getCustomTypeVariable
import org.jetbrains.kotlin.types.isFlexible
import org.jetbrains.kotlin.resolve.calls.inference.TypeBounds.Bound
import org.jetbrains.kotlin.types.TypeSubstitution
import org.jetbrains.kotlin.types.checker.JetTypeChecker

public class ConstraintSystemImpl : ConstraintSystem {

    public enum class ConstraintKind {
        SUB_TYPE,
        EQUAL
    }

    fun ConstraintKind.toBound() = if (this == SUB_TYPE) UPPER_BOUND else EXACT_BOUND

    private val typeParameterBounds = LinkedHashMap<TypeParameterDescriptor, TypeBoundsImpl>()

    private val errors = ArrayList<ConstraintError>()
    public val constraintErrors: List<ConstraintError>
        get() = errors

    private val constraintSystemStatus = object : ConstraintSystemStatus {
        // for debug ConstraintsUtil.getDebugMessageForStatus might be used

        override fun isSuccessful() = !hasContradiction() && !hasUnknownParameters()

        override fun hasContradiction() = hasTypeConstructorMismatch() || hasConflictingConstraints() || hasCannotCaptureTypesError()

        override fun hasViolatedUpperBound() = !isSuccessful() && getSystemWithoutWeakConstraints().getStatus().isSuccessful()

        override fun hasConflictingConstraints() = typeParameterBounds.values().any { it.values.size() > 1 }

        override fun hasUnknownParameters() = typeParameterBounds.values().any { it.values.isEmpty() }

        override fun hasTypeConstructorMismatch() = errors.any { it is TypeConstructorMismatch }

        override fun hasOnlyErrorsFromPosition(constraintPosition: ConstraintPosition): Boolean {
            if (isSuccessful()) return false
            if (filterConstraintsOut(constraintPosition).getStatus().isSuccessful()) return true
            return errors.isNotEmpty() && errors.all { it.constraintPosition == constraintPosition }
        }

        override fun hasErrorInConstrainingTypes() = errors.any { it is ErrorInConstrainingType }

        override fun hasCannotCaptureTypesError() = errors.any { it is CannotCapture }
    }

    private fun getParameterToInferredValueMap(
            typeParameterBounds: Map<TypeParameterDescriptor, TypeBoundsImpl>,
            getDefaultTypeProjection: (TypeParameterDescriptor) -> TypeProjection
    ): Map<TypeParameterDescriptor, TypeProjection> {
        val substitutionContext = HashMap<TypeParameterDescriptor, TypeProjection>()
        for ((typeParameter, typeBounds) in typeParameterBounds) {
            val typeProjection: TypeProjection
            val value = typeBounds.value
            if (value != null && !TypeUtils.containsSpecialType(value, DONT_CARE)) {
                typeProjection = TypeProjectionImpl(value)
            }
            else {
                typeProjection = getDefaultTypeProjection(typeParameter)
            }
            substitutionContext.put(typeParameter, typeProjection)
        }
        return substitutionContext
    }

    private fun replaceUninferredBy(getDefaultValue: (TypeParameterDescriptor) -> TypeProjection): TypeSubstitutor {
        return TypeUtils.makeSubstitutorForTypeParametersMap(getParameterToInferredValueMap(typeParameterBounds, getDefaultValue))
    }

    private fun replaceUninferredBy(defaultValue: JetType): TypeSubstitutor {
        return replaceUninferredBy { TypeProjectionImpl(defaultValue) }
    }

    private fun replaceUninferredBySpecialErrorType(): TypeSubstitutor {
        return replaceUninferredBy { TypeProjectionImpl(ErrorUtils.createUninferredParameterType(it)) }
    }

    override fun getStatus(): ConstraintSystemStatus = constraintSystemStatus

    override fun registerTypeVariables(typeVariables: Map<TypeParameterDescriptor, Variance>) {
        for ((typeVariable, positionVariance) in typeVariables) {
            typeParameterBounds.put(typeVariable, TypeBoundsImpl(typeVariable, positionVariance))
        }
        for ((typeVariable, typeBounds) in typeParameterBounds) {
            for (declaredUpperBound in typeVariable.getUpperBounds()) {
                if (KotlinBuiltIns.getInstance().getNullableAnyType() == declaredUpperBound) continue //todo remove this line (?)
                val position = TYPE_BOUND_POSITION.position(typeVariable.getIndex())
                val variableType = JetTypeImpl(Annotations.EMPTY, typeVariable.getTypeConstructor(), false, listOf(), JetScope.Empty)
                addBound(variableType, Bound(declaredUpperBound, UPPER_BOUND, position, declaredUpperBound.isPure()))
            }
        }
    }

    fun JetType.isPure() = !TypeUtils.containsSpecialType(this) {
        type -> type.getConstructor().getDeclarationDescriptor() in getTypeVariables()
    }

    public fun copy(): ConstraintSystem = createNewConstraintSystemFromThis({ it }, { it.copy() }, { true })

    public fun substituteTypeVariables(typeVariablesMap: (TypeParameterDescriptor) -> TypeParameterDescriptor?): ConstraintSystem {
        // type bounds are proper types and don't contain other variables
        return createNewConstraintSystemFromThis(typeVariablesMap, { it.copy(typeVariablesMap) }, { true })
    }

    public fun filterConstraintsOut(vararg excludePositions: ConstraintPosition): ConstraintSystem {
        val positions = excludePositions.toSet()
        return filterConstraints { !positions.contains(it) }
    }

    public fun filterConstraints(condition: (ConstraintPosition) -> Boolean): ConstraintSystem {
        return createNewConstraintSystemFromThis({ it }, { it.filter(condition) }, condition)
    }

    public fun getSystemWithoutWeakConstraints(): ConstraintSystem {
        fun ConstraintPosition.hasOnlyStrongConstraints(): Boolean {
            // 'isStrong' for compound means 'has some strong constraints'
            // but for testing absence of weak constraints we need 'has only strong constraints' here
            return if (this is CompoundConstraintPosition) {
                this.positions.all { it.hasOnlyStrongConstraints() }
            }
            else {
                this.isStrong()
            }
        }

        return filterConstraints { it.hasOnlyStrongConstraints() }
    }

    private fun createNewConstraintSystemFromThis(
            substituteTypeVariable: (TypeParameterDescriptor) -> TypeParameterDescriptor?,
            replaceTypeBounds: (TypeBoundsImpl) -> TypeBoundsImpl,
            filterConstraintPosition: (ConstraintPosition) -> Boolean
    ): ConstraintSystem {
        val newSystem = ConstraintSystemImpl()
        for ((typeParameter, typeBounds) in typeParameterBounds) {
            val newTypeParameter = substituteTypeVariable(typeParameter)
            newSystem.typeParameterBounds.put(newTypeParameter, replaceTypeBounds(typeBounds))
        }
        newSystem.errors.addAll(errors.filter { filterConstraintPosition(it.constraintPosition) }.map { it.substituteTypeVariable(substituteTypeVariable) })
        return newSystem
    }

    override fun addSupertypeConstraint(constrainingType: JetType?, subjectType: JetType, constraintPosition: ConstraintPosition) {
        if (constrainingType != null && TypeUtils.noExpectedType(constrainingType)) return

        addConstraint(SUB_TYPE, subjectType, constrainingType, constraintPosition)
    }

    override fun addSubtypeConstraint(constrainingType: JetType?, subjectType: JetType, constraintPosition: ConstraintPosition) {
        addConstraint(SUB_TYPE, constrainingType, subjectType, constraintPosition)
    }

    fun addConstraint(constraintKind: ConstraintKind, subType: JetType?, superType: JetType?, constraintPosition: ConstraintPosition) {
        val typeCheckingProcedure = TypeCheckingProcedure(object : TypeCheckingProcedureCallbacks {
            private var depth = 0

            override fun assertEqualTypes(a: JetType, b: JetType, typeCheckingProcedure: TypeCheckingProcedure): Boolean {
                depth++
                doAddConstraint(EQUAL, a, b, constraintPosition, typeCheckingProcedure)
                depth--
                return true

            }

            override fun assertEqualTypeConstructors(a: TypeConstructor, b: TypeConstructor): Boolean {
                return a == b
            }

            override fun assertSubtype(subtype: JetType, supertype: JetType, typeCheckingProcedure: TypeCheckingProcedure): Boolean {
                depth++
                doAddConstraint(SUB_TYPE, subtype, supertype, constraintPosition, typeCheckingProcedure)
                depth--
                return true
            }

            override fun capture(typeVariable: JetType, typeProjection: TypeProjection): Boolean {
                val myTypeVariable = getMyTypeVariable(typeVariable)
                if (myTypeVariable != null && constraintPosition.isCaptureAllowed()) {
                    if (depth > 0) {
                        errors.add(CannotCapture(constraintPosition, myTypeVariable))
                    }
                    generateTypeParameterCaptureConstraint(typeVariable, typeProjection, constraintPosition)
                    return true
                }
                return false
            }

            override fun noCorrespondingSupertype(subtype: JetType, supertype: JetType): Boolean {
                errors.add(TypeConstructorMismatch(constraintPosition))
                return true
            }
        })
        doAddConstraint(constraintKind, subType, superType, constraintPosition, typeCheckingProcedure)
    }

    private fun isErrorOrSpecialType(type: JetType?, constraintPosition: ConstraintPosition): Boolean {
        if (TypeUtils.isDontCarePlaceholder(type) || ErrorUtils.isUninferredParameter(type)) {
            return true
        }

        if (type == null || (type.isError() && !ErrorUtils.isFunctionPlaceholder(type))) {
            errors.add(ErrorInConstrainingType(constraintPosition))
            return true
        }
        return false
    }

    private fun doAddConstraint(
            constraintKind: ConstraintKind,
            subType: JetType?,
            superType: JetType?,
            constraintPosition: ConstraintPosition,
            typeCheckingProcedure: TypeCheckingProcedure
    ) {
        if (isErrorOrSpecialType(subType, constraintPosition) || isErrorOrSpecialType(superType, constraintPosition)) return
        if (subType == null || superType == null) return

        assert(!ErrorUtils.isFunctionPlaceholder(superType)) {
            "The type for " + constraintPosition + " shouldn't be a placeholder for function type"
        }

        // function literal { x -> ... } goes without declaring receiver type
        // and can be considered as extension function if one is expected
        val newSubType = if (constraintKind == SUB_TYPE && ErrorUtils.isFunctionPlaceholder(subType)) {
            if (isMyTypeVariable(superType)) {
                // the constraint binds type parameter and a function type,
                // we don't add it without knowing whether it's a function type or an extension function type
                return
            }
            createTypeForFunctionPlaceholder(subType, superType)
        }
        else {
            subType
        }

        fun simplifyConstraint(subType: JetType, superType: JetType) {
            if (isMyTypeVariable(subType) && isMyTypeVariable(superType)) {
                addBound(subType, Bound(superType, constraintKind.toBound(), constraintPosition, pure = false))
            }

            if (isMyTypeVariable(subType)) {
                generateTypeParameterConstraint(subType, superType, constraintKind.toBound(), constraintPosition)
                return
            }
            if (isMyTypeVariable(superType)) {
                generateTypeParameterConstraint(superType, subType, constraintKind.toBound().reverse(), constraintPosition)
                return
            }
            // if superType is nullable and subType is not nullable, unsafe call or type mismatch error will be generated later,
            // but constraint system should be solved anyway
            val subTypeNotNullable = TypeUtils.makeNotNullable(subType)
            val superTypeNotNullable = TypeUtils.makeNotNullable(superType)
            if (constraintKind == EQUAL) {
                typeCheckingProcedure.equalTypes(subTypeNotNullable, superTypeNotNullable)
            }
            else {
                typeCheckingProcedure.isSubtypeOf(subTypeNotNullable, superTypeNotNullable)
            }
        }
        simplifyConstraint(newSubType, superType)

    }

    fun addBound(variable: JetType, bound: Bound) {
        val typeBounds = getTypeBounds(variable)
        if (typeBounds.bounds.contains(bound)) return

        typeBounds.addBound(bound)
        incorporateConstraint(variable, bound)
    }

    private fun generateTypeParameterConstraint(
            parameterType: JetType,
            constrainingType: JetType,
            boundKind: TypeBounds.BoundKind,
            constraintPosition: ConstraintPosition
    ) {
        var newConstrainingType = constrainingType

        // Here we are handling the case when T! gets a bound Foo (or Foo?)
        // In this case, type parameter T is supposed to get the bound Foo!
        // Example:
        // val c: Collection<Foo> = Collections.singleton(null : Foo?)
        // Constraints for T are:
        //   Foo? <: T!
        //   Foo >: T!
        // both Foo and Foo? transform to Foo! here
        if (parameterType.isFlexible()) {
            val typeVariable = parameterType.getCustomTypeVariable()
            if (typeVariable != null) {
                newConstrainingType = typeVariable.substitutionResult(constrainingType)
            }
        }

        if (!parameterType.isMarkedNullable() || !TypeUtils.isNullableType(newConstrainingType)) {
            addBound(parameterType, Bound(newConstrainingType, boundKind, constraintPosition, newConstrainingType.isPure()))
            return
        }
        // For parameter type T:
        // constraint T? =  Int? should transform to T >: Int and T <: Int?
        // constraint T? = Int! should transform to T >: Int and T <: Int!

        // constraints T? >: Int?; T? >: Int! should transform to T >: Int
        val notNullConstrainingType = TypeUtils.makeNotNullable(newConstrainingType)
        if (boundKind == EXACT_BOUND || boundKind == LOWER_BOUND) {
            addBound(parameterType, Bound(notNullConstrainingType, LOWER_BOUND, constraintPosition))
        }
        // constraints T? <: Int?; T? <: Int! should transform to T <: Int?; T <: Int! correspondingly
        if (boundKind == EXACT_BOUND || boundKind == UPPER_BOUND) {
            addBound(parameterType, Bound(newConstrainingType, UPPER_BOUND, constraintPosition))
        }
    }

    private fun generateTypeParameterCaptureConstraint(
            parameterType: JetType,
            constrainingTypeProjection: TypeProjection,
            constraintPosition: ConstraintPosition
    ) {
        val typeVariable = getMyTypeVariable(parameterType)!!
        if (!KotlinBuiltIns.isNullableAny(typeVariable.getUpperBoundsAsType())
            && constrainingTypeProjection.getProjectionKind() == Variance.IN_VARIANCE) {
            errors.add(CannotCapture(constraintPosition, typeVariable))
        }
        val typeProjection = if (parameterType.isMarkedNullable()) {
            TypeProjectionImpl(constrainingTypeProjection.getProjectionKind(), TypeUtils.makeNotNullable(constrainingTypeProjection.getType()))
        }
        else {
            constrainingTypeProjection
        }
        val capturedType = createCapturedType(typeProjection)
        addBound(parameterType, Bound(capturedType, EXACT_BOUND, constraintPosition))
    }

    override fun getTypeVariables() = typeParameterBounds.keySet()

    override fun getTypeBounds(typeVariable: TypeParameterDescriptor): TypeBoundsImpl {
        if (!isMyTypeVariable(typeVariable)) {
            throw IllegalArgumentException("TypeParameterDescriptor is not a type variable for constraint system: $typeVariable")
        }
        return typeParameterBounds[typeVariable]!!
    }

    fun getTypeBounds(parameterType: JetType): TypeBoundsImpl {
        assert (isMyTypeVariable(parameterType)) { "Type is not a type variable for constraint system: $parameterType" }
        return getTypeBounds(getMyTypeVariable(parameterType)!!)
    }

    fun isMyTypeVariable(typeVariable: TypeParameterDescriptor) = typeParameterBounds.contains(typeVariable)

    fun isMyTypeVariable(type: JetType): Boolean = getMyTypeVariable(type) != null

    fun getMyTypeVariable(type: JetType): TypeParameterDescriptor? {
        val typeParameterDescriptor = type.getConstructor().getDeclarationDescriptor() as? TypeParameterDescriptor
        return if (typeParameterDescriptor != null && isMyTypeVariable(typeParameterDescriptor)) typeParameterDescriptor else null
    }

    override fun getResultingSubstitutor() = replaceUninferredBySpecialErrorType().setApproximateCapturedTypes()

    override fun getCurrentSubstitutor() = replaceUninferredBy(TypeUtils.DONT_CARE).setApproximateCapturedTypes()
}

fun createTypeForFunctionPlaceholder(
        functionPlaceholder: JetType,
        expectedType: JetType
): JetType {
    if (!ErrorUtils.isFunctionPlaceholder(functionPlaceholder)) return functionPlaceholder

    val functionPlaceholderTypeConstructor = functionPlaceholder.getConstructor() as FunctionPlaceholderTypeConstructor

    val isExtension = KotlinBuiltIns.isExtensionFunctionType(expectedType)
    val newArgumentTypes = if (!functionPlaceholderTypeConstructor.hasDeclaredArguments()) {
        val typeParamSize = expectedType.getConstructor().getParameters().size()
        // the first parameter is receiver (if present), the last one is return type,
        // the remaining are function arguments
        val functionArgumentsSize = if (isExtension) typeParamSize - 2 else typeParamSize - 1
        val result = arrayListOf<JetType>()
        (1..functionArgumentsSize).forEach { result.add(DONT_CARE) }
        result
    }
    else {
        functionPlaceholderTypeConstructor.getArgumentTypes()
    }
    val receiverType = if (isExtension) DONT_CARE else null
    return KotlinBuiltIns.getInstance().getFunctionType(Annotations.EMPTY, receiverType, newArgumentTypes, DONT_CARE)
}

private fun TypeSubstitutor.setApproximateCapturedTypes(): TypeSubstitutor {
    return TypeSubstitutor.create(SubstitutionWithCapturedTypeApproximation(getSubstitution()))
}

private class SubstitutionWithCapturedTypeApproximation(val substitution: TypeSubstitution) : TypeSubstitution() {
    override fun get(key: TypeConstructor?) = substitution[key]
    override fun isEmpty() = substitution.isEmpty()
    override fun approximateCapturedTypes() = true
}
