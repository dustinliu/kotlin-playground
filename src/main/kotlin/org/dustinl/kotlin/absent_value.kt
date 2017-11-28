package org.dustinl.kotlin

import java.util.*
import kotlin.reflect.KProperty

private object AbsentValue

open class MissingDataClass protected constructor() {
    private val propertiesMap: MutableMap<String, Any?> = HashMap()

    fun hasValue(name: String) = propertiesMap.contains(name) && propertiesMap[name] !is AbsentValue

    protected fun <T> delegator() = AbsentValueLoader<T>(propertiesMap)
}

class AbsentDelegator<T>(private val map: MutableMap<String, Any?>) {
    operator fun getValue(thisRef: Any, property: KProperty<*>): T? {
        val result = map[property.name]

        @Suppress("UNCHECKED_CAST")
        return if (result !is AbsentValue) result as T
        else throw IllegalStateException("the value of property '${property.name}' is absent")
    }

    operator fun setValue(thisRef: Any, property: KProperty<*>, v: T?) {
        map[property.name] = v
    }
}

class AbsentValueLoader<T>(private val map: MutableMap<String, Any?>) {
    operator fun provideDelegate(thisRef: Any?, prop: KProperty<*>): AbsentDelegator<T> {
        map[prop.name] = AbsentValue
        return AbsentDelegator(map)
    }
}
