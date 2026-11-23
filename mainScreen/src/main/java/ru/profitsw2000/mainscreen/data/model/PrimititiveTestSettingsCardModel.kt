package ru.profitsw2000.mainscreen.data.model

data class PrimitiveTestSettingsCardModel(
    val testTaskNumber: String = "10",
    val testTaskTime: String = "0:10",
    val extraField: Int? = null,
    val complexityLevelCode: Int = 0
)
