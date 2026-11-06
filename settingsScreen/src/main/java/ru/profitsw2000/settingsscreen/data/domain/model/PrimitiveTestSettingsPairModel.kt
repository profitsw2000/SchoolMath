package ru.profitsw2000.settingsscreen.data.domain.model

import ru.profitsw2000.core.utils.PrimitiveMathOperationType

data class PrimitiveTestSettingsPairModel(
    val testTaskTimePairList: List<Pair<Int, Boolean>>,
    val primitiveMathOperationType: PrimitiveMathOperationType,
    val extraFieldNumbersList: List<Pair<Int, Boolean>>,
    val testTaskComplexityCodesPairList: List<Pair<Int, Boolean>>,
    val testTasksNumberPairList: List<Pair<Int, Boolean>>
)
