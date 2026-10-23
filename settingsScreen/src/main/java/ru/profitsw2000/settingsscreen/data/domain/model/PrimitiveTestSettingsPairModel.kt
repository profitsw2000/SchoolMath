package ru.profitsw2000.settingsscreen.data.domain.model

data class PrimitiveTestSettingsPairModel(
    val testTaskTimePairList: List<Pair<Int, Boolean>>,
    val testTaskComplexityPairList: List<Pair<Int, Boolean>>,
    val testTasksNumberPairList: List<Pair<Int, Boolean>>
)
