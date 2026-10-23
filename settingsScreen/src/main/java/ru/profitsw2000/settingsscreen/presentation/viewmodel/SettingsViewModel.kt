package ru.profitsw2000.settingsscreen.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.profitsw2000.core.domain.usecase.PrimitiveTestSettingsUseCase
import ru.profitsw2000.core.model.PrimitiveTestSettingsModel
import ru.profitsw2000.core.utils.ADDITION_CATEGORY_CODE
import ru.profitsw2000.core.utils.DIVISION_CATEGORY_CODE
import ru.profitsw2000.core.utils.MULTIPLICATION_CATEGORY_CODE
import ru.profitsw2000.core.utils.PrimitiveMathOperationType
import ru.profitsw2000.core.utils.SUBTRACTION_CATEGORY_CODE
import ru.profitsw2000.settingsscreen.data.domain.model.PrimitiveTestSettingsPairModel
import ru.profitsw2000.settingsscreen.data.domain.usecase.PrimitiveTestSettingsItemsUseCase

class SettingsViewModel(
    private val primitiveTestSettingsItemsUseCase: PrimitiveTestSettingsItemsUseCase,
    private val primitiveTestSettingsUseCase: PrimitiveTestSettingsUseCase
): ViewModel() {

    private val _primitiveTestSettingsPairFlow = MutableStateFlow<PrimitiveTestSettingsPairModel?>(null)
    val primitiveTestSettingsPairModel: StateFlow<PrimitiveTestSettingsPairModel?> = _primitiveTestSettingsPairFlow.asStateFlow()

    fun loadSettings(categoryCode: Int) {
        viewModelScope.launch {
            val settingsItemsDeferred = async {
                primitiveTestSettingsItemsUseCase.getPrimitiveTestSettingsItems(
                    getPrimitiveMathOperationTypeFromCode(categoryCode)
                )
            }
            val selectedSettingsDeferred = async {
                primitiveTestSettingsUseCase.getTestSettings(
                    getPrimitiveMathOperationTypeFromCode(categoryCode)
                )
            }

            val settingsItems = settingsItemsDeferred.await()
            val selectedSettings = selectedSettingsDeferred.await()
        }
    }

    private fun updateTaskTime(time: Int) {

    }

    private fun getPrimitiveMathOperationTypeFromCode(categoryCode: Int): PrimitiveMathOperationType {
        return when(categoryCode) {
            ADDITION_CATEGORY_CODE -> PrimitiveMathOperationType.ADDITION
            SUBTRACTION_CATEGORY_CODE -> PrimitiveMathOperationType.SUBTRACTION
            MULTIPLICATION_CATEGORY_CODE -> PrimitiveMathOperationType.MULTIPLICATION
            DIVISION_CATEGORY_CODE -> PrimitiveMathOperationType.DIVISION
            else -> PrimitiveMathOperationType.ADDITION
        }
    }

    private fun createSettingsPair(
        settingsItems: PrimitiveTestSettingsPairModel,
        selectedSettings: PrimitiveTestSettingsModel
    ) {
        _primitiveTestSettingsPairFlow.value = PrimitiveTestSettingsPairModel(
            testTaskTimePairList = setSelectedPairList(settingsItems.testTaskTimePairList, selectedSettings.taskDurationTimeSeconds),
            testTaskComplexityPairList = setSelectedPairList(settingsItems.testTaskComplexityPairList, selectedSettings.testComplexityLevel),
            testTasksNumberPairList = setSelectedPairList(settingsItems.testTasksNumberPairList, selectedSettings.testTasksNumber)
        )
    }

    private fun setSelectedPairList(
        pairList: List<Pair<Int, Boolean>>,
        targetNumber: Int
    ): List<Pair<Int, Boolean>> {
        return pairList.map { pair ->
            pair.copy(second = pair.first == targetNumber)
        }
    }

}