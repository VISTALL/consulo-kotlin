class MyClass

// In principle it is not correct, MyClass? is not a subtype of MyClass
fun MyClass.inc(): MyClass? { return null }

public fun box() {
    var i : MyClass? 
    i = MyClass()
    // Type of j should be inferred as MyClass?
    var j = ++<!DEBUG_INFO_SMARTCAST!>i<!>
    // j is null so call is unsafe
    j<!UNSAFE_CALL!>.<!>hashCode()
}