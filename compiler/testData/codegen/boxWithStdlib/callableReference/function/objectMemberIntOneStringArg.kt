object O {
    fun foo(s: String): Int = s.length()
}

fun box(): String {
    val f = O::foo
    return if (f("abc") == 3) "OK" else "Fail"
}
