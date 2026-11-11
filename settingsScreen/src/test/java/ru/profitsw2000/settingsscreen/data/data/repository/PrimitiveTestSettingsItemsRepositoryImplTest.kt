package ru.profitsw2000.settingsscreen.data.data.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import ru.profitsw2000.core.utils.GENERAL_TEST_HARD_COMPLEXITY_CODE
import ru.profitsw2000.core.utils.GENERAL_TEST_LIGHT_COMPLEXITY_CODE
import ru.profitsw2000.core.utils.GENERAL_TEST_MIDDLE_COMPLEXITY_CODE
import ru.profitsw2000.core.utils.GENERAL_TEST_NORMAL_COMPLEXITY_CODE
import ru.profitsw2000.core.utils.PrimitiveMathOperationType

@OptIn(ExperimentalCoroutinesApi::class)
class PrimitiveTestSettingsItemsRepositoryImplTest {

    private val primitiveTestSettingsItemsRepository = PrimitiveTestSettingsItemsRepositoryImpl()

    private val expectedTimeList = listOf(10, 20, 30, 45, 60, 90, 120)
    private val expectedTaskNumberList = listOf(5, 10, 15, 20, 25, 30, 40, 50, 60)
    private val expectedComplexityList = listOf(
        GENERAL_TEST_LIGHT_COMPLEXITY_CODE,
        GENERAL_TEST_NORMAL_COMPLEXITY_CODE,
        GENERAL_TEST_MIDDLE_COMPLEXITY_CODE,
        GENERAL_TEST_HARD_COMPLEXITY_CODE
    )

    @Test
    fun `возврат вариантов настроек для теста сложения`() = runTest {
        val result = primitiveTestSettingsItemsRepository.getPrimitiveTestSettingsItems(PrimitiveMathOperationType.ADDITION)

        assertEquals(expectedTimeList, result.taskTimesList)
        assertEquals(PrimitiveMathOperationType.ADDITION, result.primitiveMathOperationType)
        assertEquals(listOf(10, 20, 100), result.extraFieldNumbersList)
        assertEquals(expectedComplexityList, result.taskComplexityCodesList)
        assertEquals(expectedTaskNumberList, result.taskNumberList)
    }

    @Test
    fun `возврат вариантов настроек для теста вычитания`() = runTest {
        val result = primitiveTestSettingsItemsRepository.getPrimitiveTestSettingsItems(PrimitiveMathOperationType.SUBTRACTION)

        assertEquals(expectedTimeList, result.taskTimesList)
        assertEquals(PrimitiveMathOperationType.SUBTRACTION, result.primitiveMathOperationType)
        assertEquals(listOf(10, 20, 100), result.extraFieldNumbersList)
        assertEquals(expectedComplexityList, result.taskComplexityCodesList)
        assertEquals(expectedTaskNumberList, result.taskNumberList)
    }

    @Test
    fun `возврат вариантов настроек для теста умножения`() = runTest {
        val result = primitiveTestSettingsItemsRepository.getPrimitiveTestSettingsItems(PrimitiveMathOperationType.MULTIPLICATION)

        assertEquals(expectedTimeList, result.taskTimesList)
        assertEquals(PrimitiveMathOperationType.MULTIPLICATION, result.primitiveMathOperationType)
        assertEquals(emptyList<Int>(), result.extraFieldNumbersList)
        assertEquals(expectedComplexityList, result.taskComplexityCodesList)
        assertEquals(expectedTaskNumberList, result.taskNumberList)
    }

    @Test
    fun `возврат вариантов настроек для теста деления`() = runTest {
        val result = primitiveTestSettingsItemsRepository.getPrimitiveTestSettingsItems(PrimitiveMathOperationType.DIVISION)

        assertEquals(expectedTimeList, result.taskTimesList)
        assertEquals(PrimitiveMathOperationType.DIVISION, result.primitiveMathOperationType)
        assertEquals(emptyList<Int>(), result.extraFieldNumbersList)
        assertEquals(expectedComplexityList, result.taskComplexityCodesList)
        assertEquals(expectedTaskNumberList, result.taskNumberList)
    }

}