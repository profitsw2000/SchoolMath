package ru.profitsw2000.simpletestsscreen.data.domain.model.taskgenerator

import kotlin.random.Random

class PrimitiveTaskTablePairGenerator(
    private val maxSum: Int,
    private val simpleTaskNumbers: Set<Int>,
    private val simpleTasksProb: Double,
    private val minNumber: Int = 1
) {

    private val simpleTaskPairs = mutableListOf<Pair<Int, Int>>()
    private val complexTaskPairs = mutableListOf<Pair<Int, Int>>()

    init {
        require(simpleTasksProb in 0.0..1.0) {"Probability should be between in range 0.0 to 1.0"}
        require(maxSum <= MAX_ALLOWED_SUM) {"maxSum ${maxSum} is over allowed limit ${MAX_ALLOWED_SUM}"}

        for (a in minNumber..maxSum) {
            for (b in minNumber..(maxSum - a)) {
                val pair = Pair(a, b)
                if (a in simpleTaskNumbers || b in simpleTaskNumbers) {
                    simpleTaskPairs.add(pair)
                } else complexTaskPairs.add(pair)
            }
        }


    }

    private fun nextPair(): Pair<Int, Int> {
        if (simpleTaskPairs.isEmpty() && complexTaskPairs.isEmpty()) {
            throw IllegalStateException("No available pairs for selected conditions")
        }
        if (simpleTaskPairs.isEmpty()) return complexTaskPairs.random()
        if (complexTaskPairs.isEmpty()) return simpleTaskPairs.random()

        val selectSimpleTask = Random.nextDouble() < simpleTasksProb
        return if (selectSimpleTask) simpleTaskPairs.random() else complexTaskPairs.random()
    }

    companion object{
        private const val MAX_ALLOWED_SUM = 100
    }
}