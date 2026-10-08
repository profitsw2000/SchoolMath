package ru.profitsw2000.simpletestsscreen.data.domain.usecase

import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveMathOperationType
import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveMathTaskModel

class PrimitiveTestResultUseCaseTest {

    private val primitiveTestResultUseCase = PrimitiveTestResultUseCase()

    @Test
    fun `проверка правильного ответа пользователя примера на сложение`() = runTest() {
        val expected =
            primitiveTestResultUseCase.checkCalculationResult(
                PrimitiveMathTaskModel(
                    34,
                    45,
                    PrimitiveMathOperationType.ADDITION
                ),
                79
            )

        assertEquals(expected, true)
    }

    @Test
    fun `проверка неверного ответа пользователя примера на сложение`() = runTest() {
        val expected =
            primitiveTestResultUseCase.checkCalculationResult(
                PrimitiveMathTaskModel(
                    4,
                    2,
                    PrimitiveMathOperationType.ADDITION
                ),
                3
            )

        assertEquals(expected, false)
    }

    @Test
    fun `проверка правильного ответа пользователя примера на вычитание`() = runTest() {
        val expected =
            primitiveTestResultUseCase.checkCalculationResult(
                PrimitiveMathTaskModel(
                    8,
                    3,
                    PrimitiveMathOperationType.SUBTRACTION
                ),
                5
            )

        assertEquals(expected, true)
    }

    @Test
    fun `проверка неверного ответа пользователя примера на вычитание`() = runTest() {
        val expected =
            primitiveTestResultUseCase.checkCalculationResult(
                PrimitiveMathTaskModel(
                    50,
                    33,
                    PrimitiveMathOperationType.SUBTRACTION
                ),
                16
            )

        assertEquals(expected, false)
    }

    @Test
    fun `проверка правильного ответа пользователя примера на умножение`() = runTest() {
        val expected =
            primitiveTestResultUseCase.checkCalculationResult(
                PrimitiveMathTaskModel(
                    7,
                    4,
                    PrimitiveMathOperationType.MULTIPLICATION
                ),
                28
            )

        assertEquals(expected, true)
    }

    @Test
    fun `проверка неверного ответа пользователя примера на умножение`() = runTest() {
        val expected =
            primitiveTestResultUseCase.checkCalculationResult(
                PrimitiveMathTaskModel(
                    2,
                    2,
                    PrimitiveMathOperationType.MULTIPLICATION
                ),
                5
            )

        assertEquals(expected, false)
    }

    @Test
    fun `проверка правильного ответа пользователя примера на деление`() = runTest() {
        val expected =
            primitiveTestResultUseCase.checkCalculationResult(
                PrimitiveMathTaskModel(
                    72,
                    8,
                    PrimitiveMathOperationType.DIVISION
                ),
                9
            )

        assertEquals(expected, true)
    }

    @Test
    fun `проверка неверного ответа пользователя примера на деление`() = runTest() {
        val expected =
            primitiveTestResultUseCase.checkCalculationResult(
                PrimitiveMathTaskModel(
                    49,
                    7,
                    PrimitiveMathOperationType.DIVISION
                ),
                6
            )

        assertEquals(expected, false)
    }

}