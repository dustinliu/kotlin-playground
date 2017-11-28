package org.dustinl.kotlin

import kotlin.reflect.KProperty

class AbsentDelegator<T> {
    private var value: T? = null
    private var absent = true

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return if (!absent) value as T else throw IllegalStateException("property '${property.name}' is absent")
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, v: T?) {
        absent = false
        value = v
    }
}

class TestClass() {
    var a by AbsentDelegator<String>()
}

fun main(args: Array<String>) {
    val t = TestClass()
    println(t.a)
}
