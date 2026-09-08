package ru.profitsw2000.simpletestsscreen.data.data.repository.taskgenerator

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveMathOperationType
import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveMathTaskModel
import ru.profitsw2000.simpletestsscreen.data.domain.model.taskgenerator.PrimitiveTaskTablePairGenerator
import ru.profitsw2000.simpletestsscreen.data.domain.repository.PrimitiveTestTaskGeneratorRepository
import ru.profitsw2000.simpletestsscreen.utils.ADDITION_TEST_UNDER_TEN_RESULT_COMPLEX
import ru.profitsw2000.simpletestsscreen.utils.ADDITION_TEST_UNDER_TEN_RESULT_HIGH_COMPLEXITY
import ru.profitsw2000.simpletestsscreen.utils.ADDITION_TEST_UNDER_TEN_RESULT_INTERMEDIATE
import ru.profitsw2000.simpletestsscreen.utils.ADDITION_TEST_UNDER_TEN_RESULT_SIMPLE
import kotlin.random.Random

class AdditionTestTaskGeneratorRepository(
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default
): PrimitiveTestTaskGeneratorRepository {

    override suspend fun generateTask(taskComplexityLevel: Int): PrimitiveMathTaskModel {
        return withContext(coroutineDispatcher) {
            val taskPair = getTaskPair(taskComplexityLevel)

            return@withContext PrimitiveMathTaskModel(
                firstOperand = taskPair.first,
                secondOperand = taskPair.second,
                primitiveMathOperationType = PrimitiveMathOperationType.ADDITION
            )
        }
    }

    private fun getTaskPair(taskComplexityLevel: Int): Pair<Int, Int> {
        return when(taskComplexityLevel) {
            ADDITION_TEST_UNDER_TEN_RESULT_SIMPLE ->
                Pair(
                    Random.nextInt(ADDITION_TEST_MIN_NUMBER, SIMPLE_ADDITION_TEST_UNDER_10_MAX_RESULT_NUMBER),
                    Random.nextInt(ADDITION_TEST_MIN_NUMBER, SIMPLE_ADDITION_TEST_UNDER_10_MAX_RESULT_NUMBER)
                )
            ADDITION_TEST_UNDER_TEN_RESULT_INTERMEDIATE ->
                getConditionedProbTaskPair(
                    ADDITION_TEST_UNDER_10_MAX_RESULT_NUMBER,
                    0.75
                )
            ADDITION_TEST_UNDER_TEN_RESULT_COMPLEX ->
                getConditionedProbTaskPair(
                    ADDITION_TEST_UNDER_10_MAX_RESULT_NUMBER,
                    0.5
                )
            ADDITION_TEST_UNDER_TEN_RESULT_HIGH_COMPLEXITY ->
                getConditionedProbTaskPair(
                    ADDITION_TEST_UNDER_10_MAX_RESULT_NUMBER,
                    0.25
                )
            else ->
                Pair(
                    Random.nextInt(ADDITION_TEST_MIN_NUMBER, SIMPLE_ADDITION_TEST_UNDER_10_MAX_RESULT_NUMBER),
                    Random.nextInt(ADDITION_TEST_MIN_NUMBER, SIMPLE_ADDITION_TEST_UNDER_10_MAX_RESULT_NUMBER)
                )
        }
    }

    private fun getConditionedProbTaskPair(
        maxSum: Int,
        simpleTaskProb: Double
    ): Pair<Int, Int> {
        val primitiveTaskTablePairGenerator = PrimitiveTaskTablePairGenerator(
            maxSum,
            UNDER_TEN_SIMPLE_TASK_NUMBERS_SET,
            simpleTaskProb
        )

        return primitiveTaskTablePairGenerator.nextPair()
    }

    private fun getSingleDigitWeightedNumber(): Int {
        val items = mutableListOf<Int>()
        val weights = mutableListOf<Double>()
        val totalSum: Double = ((1.0 + 9.0)*9.0)/2

        for (number in 1..9) {
            items.add(number)
            weights.add((10 - number)/totalSum)
        }

        val randomValue = Random.nextDouble()
        var cumulativeWeight = 0.0

        for (i in items.indices) {
            cumulativeWeight += weights[i]

            if (randomValue <= cumulativeWeight) {
                return items[i]
            }
        }
        return items.last()
    }

    companion object {
        private const val ADDITION_TEST_MIN_NUMBER = 1
        private const val SIMPLE_ADDITION_TEST_UNDER_10_MAX_RESULT_NUMBER = 5
        private const val ADDITION_TEST_UNDER_10_MAX_RESULT_NUMBER = 10
        private val UNDER_TEN_SIMPLE_TASK_NUMBERS_SET = setOf(1, 2, 8, 9)
    }



}
