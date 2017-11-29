package org.dustinl.kotlin.data

import com.fasterxml.jackson.databind.ObjectMapper
import org.testng.Assert.*
import org.testng.annotations.Test


class AbsentValueTest {
    class TestClass: OptionalDataClass() {
        var a by delegator<String>()
        var b by delegator<Int>()
    }

    @Test(expectedExceptions = [IllegalStateException::class])
    fun `test absent value`() {
        val testClass = TestClass()
        testClass.b = 1
        assertFalse(testClass.hasValue(testClass::a.name))
        testClass.a
    }

    @Test
    fun `test illegal property`() {
        val testClass = TestClass()
        assertFalse(testClass.hasValue("c"))
    }

    @Test
    fun `test normal value setted`() {
        val testClass = TestClass()
        testClass.a = "fdfdf"
        assertTrue(testClass.hasValue(TestClass::a.name))
        assertEquals(testClass.a, "fdfdf")
    }

    @Test
    fun `test null value setted`() {
        val testClass = TestClass()
        testClass.a = null
        testClass.b = 2
        assertTrue(testClass.hasValue(TestClass::a.name))
        assertTrue(testClass.a == null)
        assertTrue(testClass.b == 2)
    }

    class Address(zip: Int) : OptionalDataClass() {
        var zip by delegator<Int>()
        init {
            this.zip = zip
        }
    }

    data class Data(val a: String, val b: Int)


    class Staff() : OptionalDataClass() {
        constructor(name: String, age: Int): this() {
            this.name = name
            this.age = age
        }

        var name by delegator<String>()
        var age by delegator<Int>()
        var addresses by delegator<Array<Address>>()
        var data by delegator<Data>()
    }

    @Test
    fun `test json binding`() {
        val mapper = ObjectMapper()
        val jsonString = """{"name": "gggg", "age": 12}"""
        val staff = mapper.readValue(jsonString, Staff::class.java)
        assertEquals(staff.name, "gggg")
        assertEquals(staff.age, 12)
    }

    @Test
    fun `test json binding with value absent`() {
        val mapper = ObjectMapper()
        val jsonString = """{"name": "gggg"}"""
        val staff = mapper.readValue(jsonString, Staff::class.java)
        assertEquals(staff.name, "gggg")
        assertFalse(staff.hasValue("age"))
    }

    @Test
    fun `test json binding with null value`() {
        val reader = ObjectMapper().readerFor(Staff::class.java)
        val jsonString = """{"name": null, "age": "12"}"""
        val staff = reader.readValue<Staff>(jsonString)
        assertTrue(staff.hasValue("name"))
        assertEquals(staff.name, null)
        assertEquals(staff.age, 12)
    }

    @Test
    fun `test empty obj to json`() {
        val staff = Staff().apply { data = Data("Fdsf", 123); name = "fdsffdf"; age = null; addresses = arrayOf(Address(45), Address(78)) }
        println(ObjectMapper().writeValueAsString(staff))
    }
}
