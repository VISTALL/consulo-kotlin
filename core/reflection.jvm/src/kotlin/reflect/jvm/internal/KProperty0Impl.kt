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

package kotlin.reflect.jvm.internal

import org.jetbrains.kotlin.descriptors.PropertyDescriptor
import java.lang.reflect.Method
import kotlin.jvm.internal.MutablePropertyReference0
import kotlin.jvm.internal.PropertyReference0
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty0

open class KProperty0Impl<out R> : DescriptorBasedProperty<R>, KProperty0<R>, KPropertyImpl<R> {
    constructor(container: KCallableContainerImpl, descriptor: PropertyDescriptor) : super(container, descriptor)

    constructor(container: KCallableContainerImpl, name: String, signature: String) : super(container, name, signature)

    override val getter by ReflectProperties.lazy { Getter(this) }

    override val javaGetter: Method get() = super.javaGetter!!

    @suppress("UNCHECKED_CAST")
    override fun get(): R = reflectionCall {
        return javaGetter.invoke(null) as R
    }

    override fun call(vararg args: Any?): R {
        require(args.isEmpty()) { "Property $name expects no arguments, but ${args.size()} were provided." }
        return get()
    }

    class Getter<out R>(override val property: KProperty0Impl<R>) : KPropertyImpl.Getter<R>(), KProperty0.Getter<R> {
        override fun invoke(): R = property.get()
    }
}

open class KMutableProperty0Impl<R> : KProperty0Impl<R>, KMutableProperty0<R>, KMutablePropertyImpl<R> {
    constructor(container: KCallableContainerImpl, descriptor: PropertyDescriptor) : super(container, descriptor)

    constructor(container: KCallableContainerImpl, name: String, signature: String) : super(container, name, signature)

    override val setter by ReflectProperties.lazy { Setter(this) }

    override val javaSetter: Method get() = super.javaSetter!!

    override fun set(value: R) {
        reflectionCall {
            javaSetter.invoke(null, value)
        }
    }

    class Setter<R>(override val property: KMutableProperty0Impl<R>) : KMutablePropertyImpl.Setter<R>(), KMutableProperty0.Setter<R> {
        override fun invoke(value: R): Unit = property.set(value)

        @suppress("UNCHECKED_CAST")
        override fun call(vararg args: Any?) {
            require(args.size() == 1) { "Property setter for ${property.name} expects one argument, but ${args.size()} were provided." }
            property.set(args.single() as R)
        }
    }
}


class KProperty0FromReferenceImpl(
        val reference: PropertyReference0
) : KProperty0Impl<Any?>(
        reference.getOwner() as KCallableContainerImpl,
        reference.getName(),
        reference.getSignature()
) {
    override val name: String get() = reference.getName()

    override fun get(): Any? = reference.get()
}


class KMutableProperty0FromReferenceImpl(
        val reference: MutablePropertyReference0
) : KMutableProperty0Impl<Any?>(
        reference.getOwner() as KCallableContainerImpl,
        reference.getName(),
        reference.getSignature()
) {
    override val name: String get() = reference.getName()

    override fun get(): Any? = reference.get()

    override fun set(value: Any?) {
        reference.set(value)
    }
}
