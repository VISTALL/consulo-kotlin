fun box(){
    test(test("1", "2"),
                fail())

    test(fail(),
                test("1", "2"))
}

public fun checkEquals(p1: String, p2: String) {
    throw AssertionError("fail")
}

inline fun test(p: String, s: String) : String {
    return "123"
}

fun fail() : String {
    throw AssertionError("fail")
}

//2 14 2 14 5 14 5 14 7 10 14 18