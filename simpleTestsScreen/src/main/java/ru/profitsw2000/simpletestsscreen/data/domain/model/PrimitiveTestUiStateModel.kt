package ru.profitsw2000.simpletestsscreen.data.domain.model

data class PrimitiveTestUiStateModel(
    val firstOperand: Int = 0,
    val secondOperand: Int = 0,
    val primitiveMathOperationType: PrimitiveMathOperationType = PrimitiveMathOperationType.ADDITION,
    val taskTime: Int = 0,
    val totalTaskTime: Int = 10,
    val taskNumber: Int = 1,
    val totalTaskNumber: Int = 10,
    val testIsRunning: Boolean = false
)
