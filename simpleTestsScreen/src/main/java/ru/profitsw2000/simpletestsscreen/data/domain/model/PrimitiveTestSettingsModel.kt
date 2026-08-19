package ru.profitsw2000.simpletestsscreen.data.domain.model

data class PrimitiveTestSettingsModel(
    val testTasksNumber: Int = 10,
    val testComplexityLevel: Int = 0,
    val taskTimeSeconds: Long = 10
)
