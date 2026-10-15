package ru.profitsw2000.simpletestsscreen.data.domain.model

import ru.profitsw2000.core.utils.PrimitiveMathOperationType

data class PrimitiveTestUiStateModel(
    val firstOperand: Int = 0,
    val secondOperand: Int = 0,
    val primitiveMathOperationType: PrimitiveMathOperationType = PrimitiveMathOperationType.ADDITION,
    val taskTime: Int = 0,
    val totalTaskTime: Int = 10,
    val taskNumber: Int = 1,
    val totalTaskNumber: Int = 10,
    val testComplexity: Int = 0,
    val taskIsLoaded: Boolean = false,
    val testIsRunning: Boolean = false
)
