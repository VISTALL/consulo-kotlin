fun doSomething<T>(a: T) {}

fun foo() {
    fun test(): Boolean { return false }
    val a = true
    if (test() <caret>&& a) {
        doSomething("test")
    }
}
