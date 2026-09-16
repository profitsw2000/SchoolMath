package ru.profitsw2000.simpletestsscreen.data.domain.model

data class PrimitiveTestUiStateModel(
    val firstOperand: Int,
    val secondOperand: Int,
    val primitiveMathOperationType: PrimitiveMathOperationType,
    val taskTime: Int,
    val totalTaskTime: Int,
    val taskNumber: Int,
    val totalTaskNumber: Int,
    val testIsRunning: Boolean
)
