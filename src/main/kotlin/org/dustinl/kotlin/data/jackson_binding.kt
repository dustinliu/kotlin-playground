package org.dustinl.kotlin.data

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer

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
    is None -> gen.writeOmittedField(name)
    is OptionalDataClass -> {
        gen.writeObjectFieldStart(name)
        writeDataClass(gen, value)
        gen.writeEndObject()
    }
    else -> gen.writeObjectField(name, value)
}
