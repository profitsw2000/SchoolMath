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
import ru.profitsw2000.simpletestsscreen.utils.ADDITION_TEST_UNDER_TWENTY_RESULT_COMPLEX
import ru.profitsw2000.simpletestsscreen.utils.ADDITION_TEST_UNDER_TWENTY_RESULT_HIGH_COMPLEXITY
import ru.profitsw2000.simpletestsscreen.utils.ADDITION_TEST_UNDER_TWENTY_RESULT_INTERMEDIATE
import ru.profitsw2000.simpletestsscreen.utils.ADDITION_TEST_UNDER_TWENTY_RESULT_SIMPLE
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
            ADDITION_TEST_UNDER_TWENTY_RESULT_SIMPLE ->
                getUnderTwentyResultTaskPair(
                    0.5, 0.5, 0.0
                )
            ADDITION_TEST_UNDER_TWENTY_RESULT_INTERMEDIATE ->
                getUnderTwentyResultTaskPair(
                    0.0, 0.5, 0.5
                )
            ADDITION_TEST_UNDER_TWENTY_RESULT_COMPLEX ->
                getUnderTwentyResultTaskPair(
                    0.0, 0.25, 0.75
                )
            ADDITION_TEST_UNDER_TWENTY_RESULT_HIGH_COMPLEXITY ->
                getUnderTwentyResultTaskPair(
                    0.0, 0.0, 1.0
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

    private fun getUnderTwentyResultTaskPair(
        simpleTaskProb: Double,
        middleTaskProbe: Double,
        hardTaskProb: Double
    ): Pair<Int, Int> {
        if ((simpleTaskProb + middleTaskProbe + hardTaskProb) > 1.0) throw IllegalStateException("Probability sum should be lower than 1.0")

        val randomValue = Random.nextDouble()
        return if (randomValue <= simpleTaskProb) getUnderTwentySumSimpleTaskPair()
        else if (randomValue <= (simpleTaskProb + middleTaskProbe)) getUnderTwentySumMiddleTaskPair()
        else getUnderTwentySumHardTaskPair()
    }

    private fun getUnderTwentySumSimpleTaskPair(): Pair<Int, Int> {
        val numbersRange = 1..9
        val firstOperand = getProbWeightedNumber(
            numbersRange.toList(),
            numbersWeightList(numbersRange, true)
        )
        val secondOperand = Random.nextInt(1, 11 - firstOperand)

        return Pair(firstOperand, secondOperand)
    }

    private fun getUnderTwentySumMiddleTaskPair(): Pair<Int, Int> {
        val randomDouble = Random.nextDouble()
        val numbersRange = 0..9
        val firstOperand = getProbWeightedNumber(
            numbersRange.toList(),
            numbersWeightList(numbersRange, true)
        )
        val secondOperand = Random.nextInt(1, 11 - firstOperand)

        return when {
            randomDouble <= 0.5 -> getUnderTwentySumSimpleTaskPair()
            firstOperand == 0 -> Pair(10, secondOperand)
            secondOperand == 0 -> Pair(firstOperand, 10)
            randomDouble <= 0.75 -> Pair(firstOperand + 10, secondOperand)
            randomDouble <= 1 -> Pair(firstOperand, secondOperand + 10)
            else -> getUnderTwentySumSimpleTaskPair()
        }
    }

    private fun getUnderTwentySumHardTaskPair(): Pair<Int, Int> {
        val numbersRange = 2..9
        val firstOperand = getProbWeightedNumber(
            numbersRange.toList(),
            numbersWeightList(numbersRange, false)
        )
        val secondOperand = Random.nextInt(11 - firstOperand, 10)

        return Pair(firstOperand, secondOperand)
    }

    private fun numbersWeightList(numberRange: IntRange, isAscendingWeights: Boolean): List<Double> {
        val totalSum: Double = ((1 + numberRange.count())*numberRange.count())/2.0
        val weights = mutableListOf<Double>()

        for (number in numberRange) {
            weights.add((10 - number)/totalSum)
        }

        return if (isAscendingWeights) weights
        else weights.asReversed()
    }

    private fun getProbWeightedNumber(numbersList: List<Int>, numbersProbList: List<Double>): Int {
        require(numbersList.size == numbersProbList.size) {"Lists size should be equal"}
        require(numbersList.isNotEmpty()) {"Empty lists is not allowed"}

        val randomValue = Random.nextDouble()
        var cumulativeWeight = 0.0

        for (i in numbersList.indices) {
            cumulativeWeight += numbersProbList[i]

            if (randomValue <= cumulativeWeight) {
                return numbersList[i]
            }
        }
        return numbersList.last()
    }

    companion object {
        private const val ADDITION_TEST_MIN_NUMBER = 1
        private const val SIMPLE_ADDITION_TEST_UNDER_10_MAX_RESULT_NUMBER = 5
        private const val ADDITION_TEST_UNDER_10_MAX_RESULT_NUMBER = 10
        private val UNDER_TEN_SIMPLE_TASK_NUMBERS_SET = setOf(1, 2, 8, 9)
    }



}
