/*
 * Copyright 2010-2014 JetBrains s.r.o.
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

package kotlin.jvm

/**
 * Marks the JVM backing field of the annotated property as `volatile`, meaning that writes to this field
 * are immediately made visible to other threads.
 */
target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
public annotation(retention = AnnotationRetention.SOURCE) class volatile

/**
 * Marks the JVM backing field of the annotated property as `transient`, meaning that it is not
 * part of the default serialized form of the object.
 */
target(AnnotationTarget.PROPERTY)
public annotation(retention = AnnotationRetention.SOURCE) class transient

/**
 * Marks the JVM method generated from the annotated function as `strictfp`, meaning that the precision
 * of floating point operations performed inside the method needs to be restricted in order to
 * achieve better portability.
 */
target(AnnotationTarget.FUNCTION, AnnotationTarget.CONSTRUCTOR, AnnotationTarget.PROPERTY)
public annotation(retention = AnnotationRetention.SOURCE) class strictfp

/**
 * Marks the JVM method generated from the annotated function as `synchronized`, meaning that the method
 * will be protected from concurrent execution by multiple threads by the monitor of the instance (or,
 * for static methods, the class) on which the method is defined.
 */
target(AnnotationTarget.FUNCTION)
public annotation(retention = AnnotationRetention.SOURCE) class synchronized

/**
 * Marks the JVM method generated from the annotated function as `native`, meaning that it's not implemented
 * in Java but rather in a different language (for example, in C/C++ using JNI).
 */
target(AnnotationTarget.FUNCTION, AnnotationTarget.CONSTRUCTOR, AnnotationTarget.VALUE_PARAMETER,
       AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
public annotation(retention = AnnotationRetention.SOURCE) class native
