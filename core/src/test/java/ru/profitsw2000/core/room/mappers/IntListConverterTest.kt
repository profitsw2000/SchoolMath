package ru.profitsw2000.core.room.mappers

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class IntListConverterTest {

    private lateinit var intListConverter: IntListConverter

    @Before
    fun setUp() {
        intListConverter = IntListConverter()
    }

    @Test
    fun `успешно из списка чисел в жсон строку`() {
        val intList = listOf(5, 10, 15, 20)
        val result = intListConverter.fromIntList(intList)

        assertEquals("[5,10,15,20]", result)
    }

    @Test
    fun `пустой список чисел возвращает пустую строку жсон`() {
        val intList = emptyList<Int>()
        val result = intListConverter.fromIntList(intList)

        assertEquals("[]", result)
    }

    @Test
    fun `успешно из жсон строки в список чисел`() {
        val jsonString = "[10,11,12,15]"
        val result = intListConverter.toIntList(jsonString)

        assertEquals(4, result.size)
        assertEquals(listOf(10, 11, 12, 15), result)
    }

    @Test
    fun `неправильная жсон строка возвращается пустой список чисел`() {
        val invalidJson = "{not a valid json}"
        val result = intListConverter.toIntList(invalidJson)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `пустая строка жсон возвращается пустой список чисел`() {
        val emptyString = ""
        val result = intListConverter.toIntList(emptyString)

        assertTrue(result.isEmpty())
    }

}