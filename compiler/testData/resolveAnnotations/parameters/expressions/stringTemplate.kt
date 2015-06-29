package test

annotation class Ann(
        val s1: String,
        val s2: String,
        val s3: String,
        val s4: String
)

val i = 1

Ann(
    s1 = "a$i",
    s2 = "a$i b",
    s3 = "$i",
    s4 = "a${i}a$i"
) class MyClass

// EXPECTED: Ann(s1 = "a1": kotlin.String, s2 = "a1 b": kotlin.String, s3 = "1": kotlin.String, s4 = "a1a1": kotlin.String)
