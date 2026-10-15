package ru.profitsw2000.simpletestsscreen.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.profitsw2000.core.model.PrimitiveTestSettingsModel
import ru.profitsw2000.core.utils.PrimitiveMathOperationType
import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveMathTaskModel
import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveTestResultModel
import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveTestUiStateModel
import ru.profitsw2000.simpletestsscreen.data.domain.usecase.PrimitiveTestResultDbWriteUseCase
import ru.profitsw2000.simpletestsscreen.data.domain.usecase.PrimitiveTestResultUseCase
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
    private val primitiveTestTaskGeneratorUseCase: PrimitiveTestTaskGeneratorUseCase,
    private val primitiveTestResultUseCase: PrimitiveTestResultUseCase,
    private val primitiveTestResultDbWriteUseCase: PrimitiveTestResultDbWriteUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val externalScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
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
        testJob?.cancel()

        viewModelScope.launch {
            setInitialState(primitiveMathOperationType)
            updateTask(primitiveTestUiStateFlow.value.testComplexity)
            runTimerLoop()
        }
    }

    private fun runTimerLoop() {
        testJob?.cancel()

        testJob = viewModelScope.launch {
            while (isActive) {
                delay(1000L)
                val currentTaskTime = primitiveTestUiStateFlow.value.taskTime + 1
                val totalTaskTime = primitiveTestUiStateFlow.value.totalTaskTime

                if (currentTaskTime >= totalTaskTime) {
                    nextTask(-1)
                    break
                } else {
                    _primitiveTestUiStateFlow.update {
                        it.copy(taskTime = currentTaskTime)
                    }
                }
            }
        }
    }

    fun appendAnswerResult(taskResult: Int) {
        testJob?.cancel()

        viewModelScope.launch {
            nextTask(taskResult)
        }
    }

    private suspend fun nextTask(taskResult: Int) {
        saveTask(taskResult)

        val currentTaskNumber = primitiveTestUiStateFlow.value.taskNumber + 1
        val totalTaskNumber = primitiveTestUiStateFlow.value.totalTaskNumber

        if (currentTaskNumber > totalTaskNumber) {
            finishTest()
        } else {
            updateTask(primitiveTestUiStateFlow.value.testComplexity)
            _primitiveTestUiStateFlow.update {
                it.copy(
                    taskTime = 0,
                    taskNumber = currentTaskNumber
                )
            }
            runTimerLoop()
        }
    }

    private fun saveTask(taskResult: Int) {
        primitiveTestTaskResultModelList.add(primitiveMathTaskModel)
        testResultsList.add(taskResult)
        taskTimeList.add(primitiveTestUiStateFlow.value.taskTime)
    }

    private suspend fun finishTest() {
        testJob?.cancel()
        saveResultToDatabase()
        resetAll()
    }

    private suspend fun getPrimitiveTestResultModel(): PrimitiveTestResultModel {
        return PrimitiveTestResultModel(
            settingsModel = primitiveTestSettingsModel,
            correctAnswersNumber = getCorrectAnswersNumber(),
            testAssessment = getTestAssessment(),
            totalTimeSeconds = getTestTotalTime(),
            primitiveMathOperationType = primitiveMathTaskModel.primitiveMathOperationType,
            primitiveMathTaskModelList = primitiveTestTaskResultModelList,
            testResultsList = testResultsList,
            testTasksTimeList = taskTimeList
        )
    }

    private suspend fun getCorrectAnswersNumber(): Int {
        var correctAnswersNumber = 0
        primitiveTestTaskResultModelList.forEachIndexed { index, model ->
            if (primitiveTestResultUseCase.checkCalculationResult(model, testResultsList[index])) correctAnswersNumber++
        }

        return correctAnswersNumber
    }

    private suspend fun getTestAssessment(): Int {
        if (primitiveTestSettingsModel.testTasksNumber == 0) return TWO_ASSESSMENT
        val correctAnswersPercentage = getCorrectAnswersNumber().toDouble()/primitiveTestSettingsModel.testTasksNumber

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

    private fun getTestTotalTime(): Int = taskTimeList.sum()

    private suspend fun setInitialState(
        primitiveMathOperationType: PrimitiveMathOperationType
    ) {
        primitiveTestSettingsModel = primitiveTestSettingsUseCase.getTestSettings(primitiveMathOperationType)

        primitiveTestTaskResultModelList.clear()
        testResultsList.clear()
        taskTimeList.clear()

        _primitiveTestUiStateFlow.update {
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

    private suspend fun saveResultToDatabase() {
        withContext(ioDispatcher) {
            primitiveTestResultDbWriteUseCase.writeTestResultToDatabase(
                getPrimitiveTestResultModel()
            )
        }
    }

    private fun resetAll() {
        testJob?.cancel()
        testJob = null
        _primitiveTestUiStateFlow.update { PrimitiveTestUiStateModel() }
    }

    override fun onCleared() {
        super.onCleared()

        externalScope.launch(ioDispatcher) {
            withContext(NonCancellable) {
                saveResultToDatabase()
            }
        }
    }

}