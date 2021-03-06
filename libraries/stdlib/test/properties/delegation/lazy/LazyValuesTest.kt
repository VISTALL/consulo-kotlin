package test.properties.delegation.lazy

import org.junit.Test as test
import kotlin.properties.*
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class LazyValTest {
    var result = 0
    val a by lazy {
        ++result
    }

    test fun doTest() {
        a
        assertTrue(a == 1, "fail: initializer should be invoked only once")
    }
}

class UnsafeLazyValTest {
    var result = 0
    val a by lazy(LazyThreadSafetyMode.NONE) {
        ++result
    }

    test fun doTest() {
        a
        assertTrue(a == 1, "fail: initializer should be invoked only once")
    }
}

class NullableLazyValTest {
    var resultA = 0
    var resultB = 0

    val a: Int? by lazy { resultA++; null}
    val b by lazy { foo() }

    test fun doTest() {
        a
        b

        assertTrue(a == null, "fail: a should be null")
        assertTrue(b == null, "fail: b should be null")
        assertTrue(resultA == 1, "fail: initializer for a should be invoked only once")
        assertTrue(resultB == 1, "fail: initializer for b should be invoked only once")
    }

    fun foo(): String? {
        resultB++
        return null
    }
}

class UnsafeNullableLazyValTest {
    var resultA = 0
    var resultB = 0

    val a: Int? by lazy(LazyThreadSafetyMode.NONE) { resultA++; null}
    val b by lazy(LazyThreadSafetyMode.NONE) { foo() }

    test fun doTest() {
        a
        b

        assertTrue(a == null, "fail: a should be null")
        assertTrue(b == null, "fail: a should be null")
        assertTrue(resultA == 1, "fail: initializer for a should be invoked only once")
        assertTrue(resultB == 1, "fail: initializer for b should be invoked only once")
    }

    fun foo(): String? {
        resultB++
        return null
    }
}

class UnsafeLazyValDeprecatedTest {
    var result = 0
    val a by Delegates.lazy {
        ++result
    }

    test fun doTest() {
        a
        assertTrue(a == 1, "fail: initializer should be invoked only once")
    }
}

class BlockingLazyValDeprecatedTest {
    var result = 0
    val a by Delegates.blockingLazy {
        ++result
    }

    test fun doTest() {
        a
        assertTrue(a == 1, "fail: initializer should be invoked only once")
    }
}

class UnsafeNullableLazyValDeprecatedTest  {
    var resultA = 0
    var resultB = 0

    val a: Int? by Delegates.lazy { resultA++; null}
    val b by Delegates.lazy { foo() }

    test fun doTest() {
        a
        b

        assertTrue(a == null, "fail: a should be null")
        assertTrue(b == null, "fail: b should be null")
        assertTrue(resultA == 1, "fail: initializer for a should be invoked only once")
        assertTrue(resultB == 1, "fail: initializer for b should be invoked only once")
    }

    fun foo(): String? {
        resultB++
        return null
    }
}

class BlockingNullableLazyValDeprecatedTest {
    var resultA = 0
    var resultB = 0

    val a: Int? by Delegates.blockingLazy { resultA++; null}
    val b by Delegates.blockingLazy { foo() }

    test fun doTest() {
        a
        b

        assertTrue(a == null, "fail: a should be null")
        assertTrue(b == null, "fail: a should be null")
        assertTrue(resultA == 1, "fail: initializer for a should be invoked only once")
        assertTrue(resultB == 1, "fail: initializer for b should be invoked only once")
    }

    fun foo(): String? {
        resultB++
        return null
    }
}

class IdentityEqualsIsUsedToUnescapeLazyValTest {
    var equalsCalled = 0
    val a by lazy { ClassWithCustomEquality { equalsCalled++ } }

    test fun doTest() {
        a
        a
        assertTrue(equalsCalled == 0, "fail: equals called $equalsCalled times.")
    }
}

private class ClassWithCustomEquality(private val onEqualsCalled: () -> Unit) {
    override fun equals(other: Any?): Boolean {
        onEqualsCalled()
        return super.equals(other)
    }
}