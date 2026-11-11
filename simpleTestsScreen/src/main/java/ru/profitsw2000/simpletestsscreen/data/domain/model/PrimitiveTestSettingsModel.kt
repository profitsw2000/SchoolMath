package ru.profitsw2000.simpletestsscreen.data.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PrimitiveTestSettingsModel(
    val testTasksNumber: Int = 10,
    val testComplexityLevel: Int = 0,
    val taskDurationTimeSeconds: Int = 10
)
