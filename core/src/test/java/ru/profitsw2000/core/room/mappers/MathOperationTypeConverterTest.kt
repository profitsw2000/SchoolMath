package ru.profitsw2000.core.room.mappers

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ru.profitsw2000.core.utils.PrimitiveMathOperationType

class MathOperationTypeConverterTest {

    private lateinit var mathOperationTypeConverter: MathOperationTypeConverter

    @Before
    fun setUp() {
        mathOperationTypeConverter = MathOperationTypeConverter()
    }

    @Test
    fun `из переменной класса перечисления получаем соответствующую строку`() {
        val primitiveMathOperationType = PrimitiveMathOperationType.MULTIPLICATION
        val result = mathOperationTypeConverter.fromMathOperationType(primitiveMathOperationType)

        assertEquals("MULTIPLICATION", result)
    }

    @Test
    fun `из строки получаем переменную класса перечисления`() {
        val operationString = "SUBTRACTION"
        val result = mathOperationTypeConverter.toMathOperationType(operationString)

        assertEquals(PrimitiveMathOperationType.SUBTRACTION, result)
    }

    @Test
    fun `из неправильной строки получаем объект класса перечисления по умолчанию`() {

        val invalidString = "UNKNOWN_OR_BROKEN_TYPE"
        val result = mathOperationTypeConverter.toMathOperationType(invalidString)

        assertEquals(PrimitiveMathOperationType.ADDITION, result)

    }

    @Test
    fun `из пустой строки получаем объект класса перечисления по умолчанию`() {

        val invalidString = ""
        val result = mathOperationTypeConverter.toMathOperationType(invalidString)

        assertEquals(PrimitiveMathOperationType.ADDITION, result)

    }

}