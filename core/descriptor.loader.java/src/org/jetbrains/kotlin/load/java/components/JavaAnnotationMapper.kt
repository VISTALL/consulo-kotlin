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

package org.jetbrains.kotlin.load.java.components

import org.jetbrains.kotlin.builtins.KotlinBuiltIns
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.ValueParameterDescriptor
import org.jetbrains.kotlin.descriptors.annotations.AnnotationDescriptor
import org.jetbrains.kotlin.load.java.JvmAnnotationNames
import org.jetbrains.kotlin.load.java.lazy.LazyJavaResolverContext
import org.jetbrains.kotlin.load.java.lazy.descriptors.resolveAnnotation
import org.jetbrains.kotlin.load.java.structure.JavaAnnotation
import org.jetbrains.kotlin.load.java.structure.JavaAnnotationArgument
import org.jetbrains.kotlin.load.java.structure.JavaArrayAnnotationArgument
import org.jetbrains.kotlin.load.java.structure.JavaEnumValueAnnotationArgument
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.platform.JavaToKotlinClassMap
import org.jetbrains.kotlin.resolve.constants.ArrayValue
import org.jetbrains.kotlin.resolve.constants.ConstantValue
import org.jetbrains.kotlin.resolve.constants.EnumValue
import org.jetbrains.kotlin.types.ErrorUtils
import org.jetbrains.kotlin.types.JetType
import org.jetbrains.kotlin.descriptors.annotations.AnnotationTarget
import java.util.*

public object JavaAnnotationMapper {

    public fun mapJavaAnnotation(annotation: JavaAnnotation, c: LazyJavaResolverContext): AnnotationDescriptor? =
            when (annotation.classId?.let { JavaToKotlinClassMap.INSTANCE.mapJavaToKotlin(it.asSingleFqName()) } ) {
                c.module.builtIns.targetAnnotation -> JavaTargetAnnotationDescriptor(annotation, c.module.builtIns.targetAnnotation)
                else -> c.resolveAnnotation(annotation)
            }
}

class JavaTargetAnnotationDescriptor(annotation: JavaAnnotation, targetDescriptor: ClassDescriptor): AnnotationDescriptor {

    private val valueArguments by lazy {
        val first = annotation.arguments.firstOrNull()
        val targetArgument = when (first) {
            is JavaArrayAnnotationArgument -> JavaAnnotationTargetMapper.mapJavaTargetArguments(first.getElements())
            is JavaEnumValueAnnotationArgument -> JavaAnnotationTargetMapper.mapJavaTargetArguments(listOf(first))
            else -> return@lazy emptyMap<ValueParameterDescriptor, ConstantValue<*>>()
        }
        val parameterDescriptor = targetDescriptor.constructors.firstOrNull()?.valueParameters?.firstOrNull()
        parameterDescriptor?.let { mapOf(it to targetArgument) } ?: emptyMap()
    }

    override fun getAllValueArguments() = valueArguments

    private val type = targetDescriptor.defaultType

    override fun getType() = type
}

public object JavaAnnotationTargetMapper {
    private val targetNameLists = mapOf("PACKAGE"         to EnumSet.of(AnnotationTarget.PACKAGE),
                                        "TYPE"            to EnumSet.of(AnnotationTarget.CLASSIFIER),
                                        "ANNOTATION_TYPE" to EnumSet.of(AnnotationTarget.ANNOTATION_CLASS),
                                        "TYPE_PARAMETER"  to EnumSet.of(AnnotationTarget.TYPE_PARAMETER),
                                        "FIELD"           to EnumSet.of(AnnotationTarget.FIELD),
                                        "LOCAL_VARIABLE"  to EnumSet.of(AnnotationTarget.LOCAL_VARIABLE),
                                        "PARAMETER"       to EnumSet.of(AnnotationTarget.VALUE_PARAMETER),
                                        "CONSTRUCTOR"     to EnumSet.of(AnnotationTarget.CONSTRUCTOR),
                                        "METHOD"          to EnumSet.of(AnnotationTarget.FUNCTION,
                                                                        AnnotationTarget.PROPERTY_GETTER,
                                                                        AnnotationTarget.PROPERTY_SETTER),
                                        "TYPE_USE"        to EnumSet.of(AnnotationTarget.TYPE)
    )

    public fun mapJavaTargetArgumentByName(argumentName: String?): Set<AnnotationTarget> = targetNameLists[argumentName] ?: emptySet()

    public fun mapJavaTargetArguments(elements: List<JavaAnnotationArgument>): ConstantValue<*>? {
        // Generate kotlin.annotation.target, map arguments
        val kotlinAnnotationTargetClassDescriptor = KotlinBuiltIns.getInstance().annotationTargetEnum
        val kotlinTargets = ArrayList<ConstantValue<*>>()
        elements.filterIsInstance<JavaEnumValueAnnotationArgument>().forEach {
            mapJavaTargetArgumentByName(it.resolve()?.name?.asString()).forEach {
                val enumClassifier = kotlinAnnotationTargetClassDescriptor.unsubstitutedInnerClassesScope.getClassifier(Name.identifier(it.name()))
                if (enumClassifier is ClassDescriptor) {
                    kotlinTargets.add(EnumValue(enumClassifier))
                }
            }
        }
        val parameterDescriptor = DescriptorResolverUtils.getAnnotationParameterByName(JvmAnnotationNames.TARGET_ANNOTATION_MEMBER_NAME,
                                                                                       KotlinBuiltIns.getInstance().targetAnnotation)
        return ArrayValue(kotlinTargets, parameterDescriptor?.type ?: ErrorUtils.createErrorType("Error: AnnotationTarget[]"))
    }
}
