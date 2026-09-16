package ru.profitsw2000.simpletestsscreen.data.domain.model

data class PrimitiveTestResultModel(
    val settingsModel: PrimitiveTestSettingsModel,
    val correctAnswersNumber: Int,
    val testAssessment: Int,
    val totalTimeSeconds: Int,
    val primitiveMathOperationType: PrimitiveMathOperationType,
    val primitiveTestTaskResultModelList: List<PrimitiveMathTaskModel>,
    val testResultsList: List<Int>,
    val testTasksTime: List<Int>
)
