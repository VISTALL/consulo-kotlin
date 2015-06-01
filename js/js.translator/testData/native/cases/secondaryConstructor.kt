package foo

native
class A {
    constructor()
    constructor(s: String)
    constructor(i: Int)

    val value: Any?
}

fun test(a: A, expectedValue: Any?, expectedTypeOfValue: String) {
    assertTrue(a is A)
    assertEquals(expectedValue, a.value)
    assertEquals(expectedTypeOfValue, typeof(a.value))
}

fun box(): String {
    test(A(), undefined, "undefined")
    test(A("foo"), "foo", "string")
    test(A(124), 124, "number")

    return "OK"
}