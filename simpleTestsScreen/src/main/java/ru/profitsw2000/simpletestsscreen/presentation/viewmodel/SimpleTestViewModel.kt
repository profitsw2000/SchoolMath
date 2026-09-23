package ru.profitsw2000.simpletestsscreen.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveMathOperationType
import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveMathTaskModel
import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveTestResultModel
import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveTestSettingsModel
import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveTestUiStateModel
import ru.profitsw2000.simpletestsscreen.data.domain.usecase.PrimitiveTestSettingsUseCase
import ru.profitsw2000.simpletestsscreen.data.domain.usecase.PrimitiveTestTaskGeneratorUseCase
import ru.profitsw2000.simpletestsscreen.utils.FIVE_ASSESSMENT
import ru.profitsw2000.simpletestsscreen.utils.FIVE_ASSESSMENT_RIGHT_ANSWERS_PERCENTAGE
import ru.profitsw2000.simpletestsscreen.utils.FOUR_ASSESSMENT
import ru.profitsw2000.simpletestsscreen.utils.FOUR_ASSESSMENT_RIGHT_ANSWERS_PERCENTAGE
import ru.profitsw2000.simpletestsscreen.utils.THREE_ASSESSMENT
import ru.profitsw2000.simpletestsscreen.utils.THREE_ASSESSMENT_RIGHT_ANSWERS_PERCENTAGE
import ru.profitsw2000.simpletestsscreen.utils.TWO_ASSESSMENT
import ru.profitsw2000.simpletestsscreen.utils.TWO_ASSESSMENT_RIGHT_ANSWERS_PERCENTAGE

class SimpleTestViewModel(
    private val primitiveTestSettingsUseCase: PrimitiveTestSettingsUseCase,
    private val primitiveTestTaskGeneratorUseCase: PrimitiveTestTaskGeneratorUseCase
): ViewModel() {
    private var primitiveMathTaskModel: PrimitiveMathTaskModel =
        PrimitiveMathTaskModel(0, 0, PrimitiveMathOperationType.ADDITION)
    private var primitiveTestSettingsModel = PrimitiveTestSettingsModel()
    val primitiveTestTaskResultModelList: MutableList<PrimitiveMathTaskModel> = mutableListOf()
    val testResultsList: MutableList<Int> = mutableListOf()
    val taskTimeList: MutableList<Int> = mutableListOf()

    private val _primitiveTestUiStateFlow = MutableStateFlow(PrimitiveTestUiStateModel())
    val primitiveTestUiStateFlow: StateFlow<PrimitiveTestUiStateModel> = _primitiveTestUiStateFlow.asStateFlow()

    private var testJob: Job? = null

    fun startTest(primitiveMathOperationType: PrimitiveMathOperationType) {

        setInitialState(primitiveMathOperationType)
        testJob = viewModelScope.launch {
            updateTask(primitiveTestUiStateFlow.value.testComplexity)
            while (true) {
                delay(1000L)
                val currentTaskTime = primitiveTestUiStateFlow.value.taskTime + 1
                val totalTaskTime = primitiveTestUiStateFlow.value.totalTaskTime

                if (currentTaskTime >= totalTaskTime) {
                    nextTask(-1)
                } else {
                    _primitiveTestUiStateFlow.update {
                        it.copy(
                            taskTime = currentTaskTime
                        )
                    }
                }
            }
        }
    }

    private suspend fun nextTask(taskResult: Int) {
        val currentTaskNumber = primitiveTestUiStateFlow.value.taskNumber + 1
        val totalTaskNumber = primitiveTestUiStateFlow.value.totalTaskNumber

        if (currentTaskNumber > totalTaskNumber) {
            finishTest()
        } else {
            saveTask(taskResult)
            updateTask(primitiveTestUiStateFlow.value.testComplexity)
            _primitiveTestUiStateFlow.update {
                it.copy(
                    taskNumber = currentTaskNumber
                )
            }
        }
    }

    private fun saveTask(taskResult: Int) {
        primitiveTestTaskResultModelList.add(primitiveMathTaskModel)
        testResultsList.add(taskResult)
        taskTimeList.add(primitiveTestUiStateFlow.value.taskTime)
    }

    private fun finishTest() {
        saveResultToDatabase()
        resetAll()
    }

    private fun getPrimitiveTestResultModel(): PrimitiveTestResultModel {
        return PrimitiveTestResultModel(
            settingsModel = primitiveTestSettingsModel,
            correctAnswersNumber = getCorrectAnswersNumber(),
            testAssessment = getTestAssessment(),
            totalTimeSeconds = getTestTotalTime(),
            primitiveMathOperationType = primitiveMathTaskModel.primitiveMathOperationType,

        )
    }

    private fun getCorrectAnswersNumber(): Int {
        var correctAnswersNumber = 0
        primitiveTestTaskResultModelList.forEachIndexed { index, model ->
            val testResult = testResultsList[index]
            val calculatedResult = getTaskCalculationResult(model)

            if (testResult == calculatedResult) correctAnswersNumber++
        }

        return correctAnswersNumber
    }

    private fun getTestAssessment(): Int {
        val correctAnswersPercentage = getCorrectAnswersNumber()/primitiveTestSettingsModel.testTasksNumber

        return when {
            correctAnswersPercentage <= TWO_ASSESSMENT_RIGHT_ANSWERS_PERCENTAGE -> TWO_ASSESSMENT
            correctAnswersPercentage <= THREE_ASSESSMENT_RIGHT_ANSWERS_PERCENTAGE -> THREE_ASSESSMENT
            correctAnswersPercentage <= FOUR_ASSESSMENT_RIGHT_ANSWERS_PERCENTAGE -> FOUR_ASSESSMENT
            correctAnswersPercentage <= FIVE_ASSESSMENT_RIGHT_ANSWERS_PERCENTAGE -> FIVE_ASSESSMENT
            else -> TWO_ASSESSMENT
        }
    }

    private fun getTaskCalculationResult(primitiveMathTaskModel: PrimitiveMathTaskModel): Int {

        return when(primitiveMathTaskModel.primitiveMathOperationType) {
            PrimitiveMathOperationType.ADDITION ->
                primitiveMathTaskModel.firstOperand + primitiveMathTaskModel.secondOperand
            PrimitiveMathOperationType.SUBTRACTION ->
                primitiveMathTaskModel.firstOperand - primitiveMathTaskModel.secondOperand
            PrimitiveMathOperationType.MULTIPLICATION ->
                primitiveMathTaskModel.firstOperand * primitiveMathTaskModel.secondOperand
            PrimitiveMathOperationType.DIVISION ->
                primitiveMathTaskModel.firstOperand / primitiveMathTaskModel.secondOperand
        }
    }

    private fun getTestTotalTime(): Int {
        var testTotalTime = 0

        taskTimeList.forEach { time ->
            testTotalTime += time
        }
        return testTotalTime
    }

    private fun setInitialState(
        primitiveMathOperationType: PrimitiveMathOperationType
    ) {
        viewModelScope.launch {
            primitiveTestSettingsModel = primitiveTestSettingsUseCase.getTestSettings(primitiveMathOperationType)
            primitiveTestTaskResultModelList.clear()
            testResultsList.clear()
            _primitiveTestUiStateFlow.value =
                PrimitiveTestUiStateModel(
                    primitiveMathOperationType = primitiveMathOperationType,
                    totalTaskTime = primitiveTestSettingsModel.taskDurationTimeSeconds,
                    totalTaskNumber = primitiveTestSettingsModel.testTasksNumber,
                    taskTime = 0,
                    taskNumber = 1,
                    testComplexity = primitiveTestSettingsModel.testComplexityLevel,
                    testIsRunning = true
                )
        }
    }

    private suspend fun updateTask(testComplexityLevel: Int) {
        primitiveMathTaskModel = primitiveTestTaskGeneratorUseCase.getAdditionTask(testComplexityLevel)
        _primitiveTestUiStateFlow.update {
            it.copy(
                firstOperand = primitiveMathTaskModel.firstOperand,
                secondOperand = primitiveMathTaskModel.secondOperand
            )
        }
    }

    private fun saveResultToDatabase() {

    }

    private fun resetAll() {
        testJob?.cancel()
        testJob = null
        _primitiveTestUiStateFlow.value = PrimitiveTestUiStateModel()
    }

}