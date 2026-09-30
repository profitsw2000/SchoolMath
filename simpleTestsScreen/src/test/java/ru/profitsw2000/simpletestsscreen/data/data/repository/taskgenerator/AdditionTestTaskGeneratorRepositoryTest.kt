package ru.profitsw2000.simpletestsscreen.data.data.repository.taskgenerator

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveMathOperationType
import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveMathTaskModel

@OptIn(ExperimentalCoroutinesApi::class)
class AdditionTestTaskGeneratorRepositoryTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: AdditionTestTaskGeneratorRepository

    @Before
    fun setUp() {
        repository = AdditionTestTaskGeneratorRepository(testDispatcher)
    }

    @Test
    fun `простейший пример с суммой менее 5`() = runTest {
        val iterations = 1000
        val complexity = 1
        var previousTask: PrimitiveMathTaskModel? = null

        repeat(iterations) {
            val currentTask = repository.generateTask(1)
            val currentTaskSum = currentTask.firstOperand + currentTask.secondOperand

            if (previousTask != null) {
                assertNotEquals(
                    "Одинаковые задачи два раза подряд",
                    previousTask,
                    currentTask
                )
            }

            assertEquals(PrimitiveMathOperationType.ADDITION, currentTask.primitiveMathOperationType)

            assertTrue(
                "Первое слагаемое вне диапазона от 1 до 4",
                currentTask.firstOperand in 1..5
            )
            assertTrue(
                "Второе слагаемое вне диапазона от 1 до 4",
                currentTask.secondOperand in 1..5
            )
            assertTrue(
                "Сумма вне диапазона от 2 до 10",
                currentTaskSum in 2..10
            )
            previousTask = currentTask
        }
    }

}