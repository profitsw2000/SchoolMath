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
import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveTestSettingsModel
import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveTestUiStateModel
import ru.profitsw2000.simpletestsscreen.data.domain.usecase.PrimitiveTestSettingsUseCase
import ru.profitsw2000.simpletestsscreen.data.domain.usecase.PrimitiveTestTaskGeneratorUseCase

class SimpleTestViewModel(
    private val primitiveTestSettingsUseCase: PrimitiveTestSettingsUseCase,
    private val primitiveTestTaskGeneratorUseCase: PrimitiveTestTaskGeneratorUseCase
): ViewModel() {
    private var primitiveMathTaskModel: PrimitiveMathTaskModel =
        PrimitiveMathTaskModel(0, 0, PrimitiveMathOperationType.ADDITION)
    val primitiveTestTaskResultModelList: MutableList<PrimitiveMathTaskModel> = mutableListOf()
    val testResultsList: MutableList<Int> = mutableListOf()

    private val _primitiveTestUiStateFlow = MutableStateFlow(PrimitiveTestUiStateModel())
    val primitiveTestUiStateFlow: StateFlow<PrimitiveTestUiStateModel> = _primitiveTestUiStateFlow.asStateFlow()

    private var testJob: Job? = null

    fun startTest(primitiveMathOperationType: PrimitiveMathOperationType) {

        setInitialState(primitiveMathOperationType)
        testJob = viewModelScope.launch {
            updateTask(primitiveTestUiStateFlow.value.testComplexity)
            while (true) {
                delay(1000L)
                val currentTaskTime = _primitiveTestUiStateFlow.value.taskTime + 1
                val totalTaskTime = _primitiveTestUiStateFlow.value.totalTaskTime

                if (currentTaskTime >= totalTaskTime) {
                    nextTask(-1)
                } else {
                    _primitiveTestUiStateFlow.update {
                        it.copy(
                            taskTime = primitiveTestUiStateFlow.value.taskTime + 1
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
        }
    }

    private fun saveTask(taskResult: Int) {
        primitiveTestTaskResultModelList.add(primitiveMathTaskModel)
        testResultsList.add(taskResult)
    }

    private fun finishTest() {
        saveResultToDatabase()
        resetAll()
    }

    private suspend fun loadNextTest(primitiveMathOperationType: PrimitiveMathOperationType) {
        _primitiveTestUiStateFlow.update {
            it.copy(taskNumber = primitiveTestUiStateFlow.value.taskNumber + 1)
        }
        primitiveMathTaskModel =
            primitiveTestTaskGeneratorUseCase.getAdditionTask(
                primitiveTestUiStateFlow.value.testComplexity
            )
        _primitiveTestUiStateFlow.update {
            it.copy(
                firstOperand = primitiveMathTaskModel.firstOperand,
                secondOperand = primitiveMathTaskModel.secondOperand,
                taskTime = 0
            )
        }

        if (primitiveTestUiStateFlow.value.taskNumber >= primitiveTestUiStateFlow.value.totalTaskNumber) {

        } else {

        }
    }

    private fun setInitialState(
        primitiveMathOperationType: PrimitiveMathOperationType
    ) {
        viewModelScope.launch {
            val primitiveTestSettingsModel = primitiveTestSettingsUseCase.getTestSettings(primitiveMathOperationType)
            primitiveTestTaskResultModelList.clear()
            testResultsList.clear()
            primitiveMathTaskModel = generateTask(testComplexityLevel = primitiveTestSettingsModel.testComplexityLevel)
            _primitiveTestUiStateFlow.value =
                PrimitiveTestUiStateModel(
                    primitiveMathOperationType = primitiveMathOperationType,
                    totalTaskTime = primitiveTestSettingsModel.taskDurationTimeSeconds,
                    totalTaskNumber = primitiveTestSettingsModel.testTasksNumber,
                    firstOperand = primitiveMathTaskModel.firstOperand,
                    secondOperand = primitiveMathTaskModel.secondOperand,
                    taskTime = 0,
                    testComplexity = primitiveTestSettingsModel.testComplexityLevel,
                    testIsRunning = true
                )
        }
    }

    private suspend fun generateTask(testComplexityLevel: Int): PrimitiveMathTaskModel {
        return primitiveTestTaskGeneratorUseCase.getAdditionTask(testComplexityLevel)
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