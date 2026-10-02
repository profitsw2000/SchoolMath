package ru.profitsw2000.simpletestsscreen.data.data.repository.taskgenerator

import android.util.Log
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
    private val equiprobablePairsUnderTen: Set<Pair<Int, Int>> = (1..9).flatMap { x ->
        (1..9).map { y -> x to y }
    }.filter{ (x,y) -> x + y <= 10 }.toSet()
    private val equiprobablePairsFirstAboveTen: Set<Pair<Int, Int>> = (10..19).flatMap { x ->
        (1..10).map { y -> x to y }
    }.filter{ (x,y) -> x + y <= 20 }.toSet()
    private val equiprobablePairsSecondAboveTen: Set<Pair<Int, Int>> = (1..10).flatMap { x ->
        (10..19).map { y -> x to y }
    }.filter{ (x,y) -> x + y <= 20 }.toSet()
    private val equiprobablePairsHardUnderTen: Set<Pair<Int, Int>> = (2..9).flatMap { x ->
        (2..9).map { y -> x to y }
    }.filter{ (x,y) -> x + y > 10 }.toSet()

    @Before
    fun setUp() {
        repository = AdditionTestTaskGeneratorRepository(testDispatcher)
    }

    @Test
    fun `простейший пример с суммой менее 10`() = runTest {
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

    @Test
    fun `генерация примера с суммой менее 10 средней сложности`() = runTest {
        val iterations = 1000
        val complexity = 2
        var previousTask: PrimitiveMathTaskModel? = null
        var simpleTaskNumbersCount = 0

        repeat(iterations) {
            val currentTask = repository.generateTask(complexity)
            val currentTaskSum = currentTask.firstOperand + currentTask.secondOperand
            val isSimpleTaskNumber =
                currentTask.firstOperand in setOf(1,2,8,9) ||
                        currentTask.secondOperand in setOf(1,2,8,9)

            if (previousTask != null) {
                assertNotEquals(
                    "Одинаковые задачи два раза подряд",
                    previousTask,
                    currentTask
                )
            }
            if (isSimpleTaskNumber) simpleTaskNumbersCount++

            assertEquals(PrimitiveMathOperationType.ADDITION, currentTask.primitiveMathOperationType)

            assertTrue(
                "Первое слагаемое вне диапазона от 1 до 9",
                currentTask.firstOperand in 1..9
            )
            assertTrue(
                "Второе слагаемое вне диапазона от 1 до 9",
                currentTask.secondOperand in 1..9
            )
            assertTrue(
                "Сумма вне диапазона от 2 до 10",
                currentTaskSum in 2..10
            )
            previousTask = currentTask
        }
        val simpleTaskPercentage = (simpleTaskNumbersCount.toDouble()/iterations)*100

        assertTrue(
            "Вероятность появления примера средней сложности с суммой менее 10 вышла за пределы 75+-2.5%",
            simpleTaskPercentage in 72.5..77.5
        )
    }

    @Test
    fun `генерация сложного примера с суммой менее 10`() = runTest {
        val iterations = 1000
        val complexity = 3
        var previousTask: PrimitiveMathTaskModel? = null
        var simpleTaskNumbersCount = 0

        repeat(iterations) {
            val currentTask = repository.generateTask(complexity)
            val currentTaskSum = currentTask.firstOperand + currentTask.secondOperand
            val isSimpleTaskNumber =
                currentTask.firstOperand in setOf(1,2,8,9) ||
                        currentTask.secondOperand in setOf(1,2,8,9)

            if (previousTask != null) {
                assertNotEquals(
                    "Одинаковые задачи два раза подряд",
                    previousTask,
                    currentTask
                )
            }
            if (isSimpleTaskNumber) simpleTaskNumbersCount++

            assertEquals(PrimitiveMathOperationType.ADDITION, currentTask.primitiveMathOperationType)

            assertTrue(
                "Первое слагаемое вне диапазона от 1 до 9",
                currentTask.firstOperand in 1..9
            )
            assertTrue(
                "Второе слагаемое вне диапазона от 1 до 9",
                currentTask.secondOperand in 1..9
            )
            assertTrue(
                "Сумма вне диапазона от 2 до 10",
                currentTaskSum in 2..10
            )
            previousTask = currentTask
        }
        val simpleTaskPercentage = (simpleTaskNumbersCount.toDouble()/iterations)*100

        assertTrue(
            "Вероятность появления примера средней сложности с суммой менее 10 вышла за пределы 50+-2.5% (${simpleTaskPercentage})",
            simpleTaskPercentage in 46.0..54.0
        )
    }

    @Test
    fun `генерация супер сложного примера с суммой менее 10`() = runTest {
        val iterations = 1000
        val complexity = 4
        var previousTask: PrimitiveMathTaskModel? = null
        var simpleTaskNumbersCount = 0

        repeat(iterations) {
            val currentTask = repository.generateTask(complexity)
            val currentTaskSum = currentTask.firstOperand + currentTask.secondOperand
            val isSimpleTaskNumber =
                currentTask.firstOperand in setOf(1,2,8,9) ||
                        currentTask.secondOperand in setOf(1,2,8,9)

            if (previousTask != null) {
                assertNotEquals(
                    "Одинаковые задачи два раза подряд",
                    previousTask,
                    currentTask
                )
            }
            if (isSimpleTaskNumber) simpleTaskNumbersCount++

            assertEquals(PrimitiveMathOperationType.ADDITION, currentTask.primitiveMathOperationType)

            assertTrue(
                "Первое слагаемое вне диапазона от 1 до 9",
                currentTask.firstOperand in 1..9
            )
            assertTrue(
                "Второе слагаемое вне диапазона от 1 до 9",
                currentTask.secondOperand in 1..9
            )
            assertTrue(
                "Сумма вне диапазона от 2 до 10",
                currentTaskSum in 2..10
            )
            previousTask = currentTask
        }
        val simpleTaskPercentage = (simpleTaskNumbersCount.toDouble()/iterations)*100

        assertTrue(
            "Вероятность появления примера средней сложности с суммой менее 10 вышла за пределы 25+-3% (${simpleTaskPercentage})",
            simpleTaskPercentage in 22.0..28.0
        )
    }

    @Test
    fun `простой пример с суммой менее 20`() = runTest(testDispatcher) {
        val iterations = 1000
        val complexity = 5
        var previousTask: PrimitiveMathTaskModel? = null
        var simpleTaskPairs = 0
        var middleTaskPairsFirstOperandAbove = 0
        var middleTaskPairsSecondOperandAbove = 0

        repeat(iterations) {
            val currentTask = repository.generateTask(complexity)
            val taskPair = Pair(currentTask.firstOperand, currentTask.secondOperand)
            val currentTaskSum = currentTask.firstOperand + currentTask.secondOperand

            if (previousTask != null) {
                assertNotEquals(
                    "Одинаковые задачи два раза подряд",
                    previousTask,
                    currentTask
                )
            }
            assertTrue("Сумма больше 20: ${currentTask.firstOperand} + ${currentTask.secondOperand} = ${currentTaskSum}", currentTaskSum <= 20)
            if (taskPair in equiprobablePairsUnderTen) {
                simpleTaskPairs++
            }
            if (taskPair in equiprobablePairsFirstAboveTen) {
                middleTaskPairsFirstOperandAbove++
            }
            if (taskPair in equiprobablePairsSecondAboveTen) {
                middleTaskPairsSecondOperandAbove++
            }
        }

        val simpleTaskPercentage = (simpleTaskPairs.toDouble()/iterations)*100
        val middleTaskFirstAbovePercentage = (middleTaskPairsFirstOperandAbove.toDouble()/iterations)*100
        val middleTaskSecondAbovePercentage = (middleTaskPairsSecondOperandAbove.toDouble()/iterations)*100

        assertTrue(
            "Процент простых примеров с суммой меньше 10 вышел за пределы 75+-5%: ${simpleTaskPercentage}",
            simpleTaskPercentage in 70.0..80.0
        )
        assertTrue(
            "Процент простых примеров с суммой меньше 10 вышел за пределы 12.5+-5%: ${middleTaskFirstAbovePercentage}",
            middleTaskFirstAbovePercentage in 7.5..17.5
        )
        assertTrue(
            "Процент простых примеров с суммой меньше 10 вышел за пределы 12.5+-5%: ${middleTaskSecondAbovePercentage}",
            middleTaskSecondAbovePercentage in 7.5..17.5
        )
    }

}