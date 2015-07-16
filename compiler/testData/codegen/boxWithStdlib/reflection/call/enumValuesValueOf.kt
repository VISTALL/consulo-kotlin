enum class E {
    OK
}

fun box(): String {
    if ((E::values).call() != arrayOf(E.OK)) return "Fail values"
    return ((E::valueOf).call("OK")).name()
}
