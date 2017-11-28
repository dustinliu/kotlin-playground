package org.dustinl.kotlin

import org.testng.Assert.assertEquals
import org.testng.Assert.assertTrue
import org.testng.annotations.Test

class TestClass() {
    var a by AbsentDelegator<String>()
}

class AbsentValueTest {
    @Test(expectedExceptions = arrayOf(IllegalStateException::class))
    fun `test absent value`() {
        val testClass = TestClass()
        testClass.a
    }

    @Test
    fun `test normal value setted`() {
        val testClass = TestClass()
        testClass.a = "fdfdf"
        assertEquals(testClass.a, "fdfdf")
    }

    @Test
    fun `test null value setted`() {
        val testClass = TestClass()
        testClass.a = null
        assertTrue(testClass.a == null)
    }
}