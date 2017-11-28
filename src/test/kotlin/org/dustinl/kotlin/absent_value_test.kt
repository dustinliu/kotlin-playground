package org.dustinl.kotlin

import org.testng.Assert.*
import org.testng.annotations.Test
import com.fasterxml.jackson.databind.ObjectMapper



class AbsentValueTest {
    class TestClass: MissingDataClass() {
        var a by delegator<String>()
        var b by delegator<Int>()
    }

    @Test(expectedExceptions = arrayOf(IllegalStateException::class))
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

    class Staff : MissingDataClass() {
        var name by delegator<String>(); private set
        var age by delegator<Int>(); private set
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
}