class A {
    companion object B {
        var state: String = "12345"
    }
}

fun box(): String {
    val p = A.B::state

    if (p.name != "state") return "Fail state: ${p.name}"
    if (p.get() != "12345") return "Fail value: ${p.get()}"
    p.set("OK")

    return p.get()
}
