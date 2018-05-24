package org.dustinl.kotlin.data

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import kotlin.reflect.KProperty

internal object None

@JsonSerialize(using = OptionalDataClassSerializer::class)
abstract class OptionalDataClass {
    internal val propertiesMap: MutableMap<String, Any?> = HashMap()

    private val fields by lazy { propertiesMap.keys }

    fun hasValue(name: String) = propertiesMap.contains(name) && propertiesMap[name] != None

    fun clearField(name: String) {
        if (!fields.contains(name)) {
            throw IllegalAccessException("no field '$name' defined")
        }

        propertiesMap.remove(name)
    }

    protected fun <T> delegate(default: Any? = None) = Delegate<T>(propertiesMap, default)
}

class Delegate<T>(private val map: MutableMap<String, Any?>, private val default: Any?) {
    operator fun provideDelegate(thisRef: Any?, prop: KProperty<*>): Delegate<T> {
        map[prop.name] = default
        return this
    }

    operator fun getValue(thisRef: Any, property: KProperty<*>): T? {
        val result = map[property.name]

        @Suppress("UNCHECKED_CAST")
        return if (result != None) result as T
        else throw IllegalStateException("the value of property '${property.name}' is absent")
    }

    operator fun setValue(thisRef: Any, property: KProperty<*>, v: T?) {
        map[property.name] = v
    }
}
