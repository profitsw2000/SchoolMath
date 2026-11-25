package ru.profitsw2000.simpletestsscreen.data.domain.model

import ru.profitsw2000.core.utils.PrimitiveMathOperationType

data class PrimitiveMathTaskModel(
    val firstOperand: Int,
    val secondOperand: Int,
    val primitiveMathOperationType: PrimitiveMathOperationType
)