package ru.profitsw2000.simpletestsscreen.data.domain.model

import ru.profitsw2000.core.model.PrimitiveTestSettingsModel
import ru.profitsw2000.core.utils.PrimitiveMathOperationType

data class PrimitiveTestResultModel(
    val settingsModel: PrimitiveTestSettingsModel,
    val correctAnswersNumber: Int,
    val testAssessment: Int,
    val totalTimeSeconds: Int,
    val primitiveMathOperationType: PrimitiveMathOperationType,
    val primitiveMathTaskModelList: List<PrimitiveMathTaskModel>,
    val testResultsList: List<Int>,
    val testTasksTimeList: List<Int>
)