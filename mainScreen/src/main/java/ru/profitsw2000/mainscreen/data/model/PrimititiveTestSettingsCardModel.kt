package ru.profitsw2000.mainscreen.data.model

data class PrimitiveTestSettingsCardModel(
    val testTaskNumber: Int = 10,
    val testTaskTime: String = "0:10",
    val extraFieldNumber: Int? = null,
    val complexityLevelCode: Int = 0
)
