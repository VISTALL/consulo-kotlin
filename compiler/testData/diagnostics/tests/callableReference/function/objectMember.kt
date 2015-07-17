object O {
    fun foo(s: String): Int = s.length()

    fun Int.bar() {}
    fun notAllowed() = Int::<!EXTENSION_IN_CLASS_REFERENCE_NOT_ALLOWED!>bar<!>
}

fun f1(): (String) -> Int = O::foo
