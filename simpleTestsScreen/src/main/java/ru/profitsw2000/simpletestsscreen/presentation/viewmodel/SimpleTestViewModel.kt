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
    private var testTaskTime: Int = 0
    private var testTaskNumber: Int = 0
    private var primitiveMathTaskModel: PrimitiveMathTaskModel =
        PrimitiveMathTaskModel(0, 0, PrimitiveMathOperationType.ADDITION)

    private val _primitiveTestUiStateFlow = MutableStateFlow(PrimitiveTestUiStateModel())
    val primitiveTestUiStateFlow: StateFlow<PrimitiveTestUiStateModel> = _primitiveTestUiStateFlow.asStateFlow()

    private var testJob: Job? = null

    fun startTest(primitiveMathOperationType: PrimitiveMathOperationType) {

        setInitialState(primitiveMathOperationType)
        testJob = viewModelScope.launch {
            loadNextTest(primitiveMathOperationType)
            while (true) {
                delay(1000L)
                val currentTaskTime = _primitiveTestUiStateFlow.value.taskTime + 1


                if (currentTaskTime >= totalTestTaskTime)
            }
        }
    }

    private suspend fun loadNextTest(primitiveMathOperationType: PrimitiveMathOperationType) {
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
    }

    private fun setInitialState(
        primitiveMathOperationType: PrimitiveMathOperationType
    ) {
        viewModelScope.launch {
            val primitiveTestSettingsModel = primitiveTestSettingsUseCase.getTestSettings(primitiveMathOperationType)
            _primitiveTestUiStateFlow.value =
                PrimitiveTestUiStateModel(
                    primitiveMathOperationType = primitiveMathOperationType,
                    totalTaskTime = primitiveTestSettingsModel.taskDurationTimeSeconds,
                    totalTaskNumber = primitiveTestSettingsModel.testTasksNumber,
                    testIsRunning = true
                )
        }
    }

    private suspend fun generateTask(testComplexityLevel: Int): PrimitiveMathTaskModel {
        primitiveTestTaskGeneratorUseCase.getAdditionTask(testComplexityLevel)
    }


}