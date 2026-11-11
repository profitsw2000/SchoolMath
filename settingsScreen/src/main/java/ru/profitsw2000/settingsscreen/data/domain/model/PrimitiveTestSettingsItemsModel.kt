package ru.profitsw2000.settingsscreen.data.domain.model

import ru.profitsw2000.core.utils.PrimitiveMathOperationType

data class PrimitiveTestSettingsItemsModel(
    val taskTimesList: List<Int>,
    val primitiveMathOperationType: PrimitiveMathOperationType,
    val extraFieldNumbersList: List<Int>,
    val taskComplexityCodesList: List<Int>,
    val taskNumberList: List<Int>
)
