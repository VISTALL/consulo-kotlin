package foo

// NOTE THIS FILE IS AUTO-GENERATED by the generateTestDataForReservedWords.kt. DO NOT EDIT!

fun box(): String {
    fun static() { static() }

    testRenamed("static", { ::static })

    return "OK"
}