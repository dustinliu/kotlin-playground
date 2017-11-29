package org.dustinl.kotlin.data

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import kotlin.reflect.KProperty

private object AbsentValue

@JsonSerialize(using = OptionalDataClassSerializer::class)
open class OptionalDataClass protected constructor() {
    internal val propertiesMap: MutableMap<String, Any?> = HashMap()

    fun hasValue(name: String) = propertiesMap.contains(name) && propertiesMap[name] !is AbsentValue

    protected fun <T> delegator() = AbsentValueLoader<T>(propertiesMap)
}

class OptionalDataClassSerializer : StdSerializer<OptionalDataClass>(OptionalDataClass::class.java) {
    override fun serialize(value: OptionalDataClass, jgen: JsonGenerator, provider: SerializerProvider?) {
        jgen.writeStartObject()
        writeDataClass(jgen, value)
        jgen.writeEndObject()
    }
}

internal fun writeDataClass(gen: JsonGenerator, data: OptionalDataClass) {
    for ((name, v) in data.propertiesMap) {
        writeValue(gen, name, v)
    }
}

internal fun writeValue(gen: JsonGenerator, name: String, value: Any?): Unit = when (value) {
    is AbsentValue -> gen.writeOmittedField(name)
    is OptionalDataClass -> {
        gen.writeObjectFieldStart(name)
        writeDataClass(gen, value)
        gen.writeEndObject()
    }
    else -> gen.writeObjectField(name, value)
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
