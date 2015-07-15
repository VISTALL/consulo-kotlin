// FILE: type.java

//package test;

import java.lang.annotation.*;

Target(ElementType.TYPE)
public @interface type {

}

// FILE: type.kt

//package test

type class My {
    type fun foo() {}
}
