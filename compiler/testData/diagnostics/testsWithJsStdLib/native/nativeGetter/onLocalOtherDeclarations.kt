// !DIAGNOSTICS: -UNUSED_PARAMETER -UNUSED_VARIABLE -NON_TOPLEVEL_CLASS_DECLARATION

fun foo() {
    <!NATIVE_ANNOTATIONS_ALLOWED_ONLY_ON_MEMBER_OR_EXTENSION_FUN, NATIVE_INDEXER_WRONG_PARAMETER_COUNT!>@nativeGetter
    fun toplevelFun(): <!NATIVE_GETTER_RETURN_TYPE_SHOULD_BE_NULLABLE!>Any<!><!> = 0

    <!NATIVE_ANNOTATIONS_ALLOWED_ONLY_ON_MEMBER_OR_EXTENSION_FUN!><!WRONG_ANNOTATION_TARGET!>@nativeGetter<!>
    val toplevelVal<!> = 0

    @nativeGetter
    class <!NATIVE_ANNOTATIONS_ALLOWED_ONLY_ON_MEMBER_OR_EXTENSION_FUN!>Foo<!> {}
}
