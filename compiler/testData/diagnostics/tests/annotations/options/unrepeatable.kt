annotation(repeatable = false) class unrepann(val x: Int)

annotation class ann(val y: Int)

unrepann(1) <!REPEATED_ANNOTATION!>unrepann(2)<!> class DoubleAnnotated

ann(3) <!REPEATED_ANNOTATION!>ann(7)<!> <!REPEATED_ANNOTATION!>ann(42)<!> class TripleAnnotated

target(AnnotationTarget.EXPRESSION) annotation class annexpr

ann(0) <!REPEATED_ANNOTATION!>ann(1)<!> fun foo(@ann(7) <!REPEATED_ANNOTATION!>@ann(2)<!> x: Int): Int {
    @annexpr <!REPEATED_ANNOTATION!>@annexpr<!> return x
}