package ru.profitsw2000.settingsscreen.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.profitsw2000.core.domain.usecase.PrimitiveTestSettingsUseCase
import ru.profitsw2000.core.model.PrimitiveTestSettingsModel
import ru.profitsw2000.core.utils.ADDITION_CATEGORY_CODE
import ru.profitsw2000.core.utils.ADDITION_TEST_UNDER_HUNDRED_RESULT_COMPLEX
import ru.profitsw2000.core.utils.ADDITION_TEST_UNDER_HUNDRED_RESULT_HIGH_COMPLEXITY
import ru.profitsw2000.core.utils.ADDITION_TEST_UNDER_HUNDRED_RESULT_INTERMEDIATE
import ru.profitsw2000.core.utils.ADDITION_TEST_UNDER_HUNDRED_RESULT_SIMPLE
import ru.profitsw2000.core.utils.ADDITION_TEST_UNDER_TEN_RESULT_COMPLEX
import ru.profitsw2000.core.utils.ADDITION_TEST_UNDER_TEN_RESULT_HIGH_COMPLEXITY
import ru.profitsw2000.core.utils.ADDITION_TEST_UNDER_TEN_RESULT_INTERMEDIATE
import ru.profitsw2000.core.utils.ADDITION_TEST_UNDER_TEN_RESULT_SIMPLE
import ru.profitsw2000.core.utils.ADDITION_TEST_UNDER_TWENTY_RESULT_COMPLEX
import ru.profitsw2000.core.utils.ADDITION_TEST_UNDER_TWENTY_RESULT_HIGH_COMPLEXITY
import ru.profitsw2000.core.utils.ADDITION_TEST_UNDER_TWENTY_RESULT_INTERMEDIATE
import ru.profitsw2000.core.utils.ADDITION_TEST_UNDER_TWENTY_RESULT_SIMPLE
import ru.profitsw2000.core.utils.DIVISION_CATEGORY_CODE
import ru.profitsw2000.core.utils.GENERAL_TEST_HARD_COMPLEXITY_CODE
import ru.profitsw2000.core.utils.GENERAL_TEST_LIGHT_COMPLEXITY_CODE
import ru.profitsw2000.core.utils.GENERAL_TEST_MIDDLE_COMPLEXITY_CODE
import ru.profitsw2000.core.utils.GENERAL_TEST_NORMAL_COMPLEXITY_CODE
import ru.profitsw2000.core.utils.MULTIPLICATION_CATEGORY_CODE
import ru.profitsw2000.core.utils.PrimitiveMathOperationType
import ru.profitsw2000.core.utils.SUBTRACTION_CATEGORY_CODE
import ru.profitsw2000.settingsscreen.data.domain.model.PrimitiveTestSettingsItemsModel
import ru.profitsw2000.settingsscreen.data.domain.model.PrimitiveTestSettingsPairModel
import ru.profitsw2000.settingsscreen.data.domain.usecase.PrimitiveTestSettingsItemsUseCase
import ru.profitsw2000.settingsscreen.presentation.utils.PrimitiveMathOperationSettingsMapper

class SettingsViewModel(
    private val primitiveTestSettingsItemsUseCase: PrimitiveTestSettingsItemsUseCase,
    private val primitiveTestSettingsUseCase: PrimitiveTestSettingsUseCase
): ViewModel() {

    private val mapper = PrimitiveMathOperationSettingsMapper()
    private val _primitiveTestSettingsPairFlow = MutableStateFlow<PrimitiveTestSettingsPairModel?>(null)
    val primitiveTestSettingsPairModel: StateFlow<PrimitiveTestSettingsPairModel?> = _primitiveTestSettingsPairFlow.asStateFlow()

    fun loadSettings(categoryCode: Int) {
        viewModelScope.launch {
            val settingsItemsDeferred = async {
                primitiveTestSettingsItemsUseCase.getPrimitiveTestSettingsItems(
                    mapper.getPrimitiveMathOperationTypeFromCode(categoryCode)
                )
            }
            val selectedSettingsDeferred = async {
                primitiveTestSettingsUseCase.getTestSettings(
                    mapper.getPrimitiveMathOperationTypeFromCode(categoryCode)
                )
            }

            val settingsItems = settingsItemsDeferred.await()
            val selectedSettings = selectedSettingsDeferred.await()

            createSettingsPair(
                mapper.settingsItemsToPairsList(settingsItems),
                selectedSettings,
                categoryCode
            )
        }
    }

    fun updateTaskTime(time: Int) {
        _primitiveTestSettingsPairFlow.update {
            it?.copy(
                testTaskTimePairList = mapper.setSelectedPairList(it.testTaskTimePairList, time)
            )
        }
    }

    fun updateExtraFieldNumber(number: Int) {
        _primitiveTestSettingsPairFlow.update {
            it?.copy(
                extraFieldNumbersList = mapper.setSelectedPairList(it.extraFieldNumbersList, number)
            )
        }
    }

    fun updateTestComplexityCode(complexity: Int) {
        _primitiveTestSettingsPairFlow.update {
            it?.copy(
                testTaskComplexityCodesPairList = mapper.setSelectedPairList(it.testTaskComplexityCodesPairList, complexity)
            )
        }
    }

    fun updateTestTasksNumber(number: Int) {
        _primitiveTestSettingsPairFlow.update {
            it?.copy(
                testTasksNumberPairList = mapper.setSelectedPairList(it.testTasksNumberPairList, number)
            )
        }
    }

    fun writeTestSettings() {
        val operation =
            primitiveTestSettingsPairModel.value?.primitiveMathOperationType ?: PrimitiveMathOperationType.ADDITION
        val testTaskNumber =
            mapper.getSelectedFromPairList(
                primitiveTestSettingsPairModel.value?.testTasksNumberPairList
            )
        val testComplexityLevel =
            mapper.getTestComplexityLevel(
                operation,
                mapper.getSelectedFromPairList(
                    _primitiveTestSettingsPairFlow.value?.testTaskComplexityCodesPairList
                ),
                mapper.getSelectedFromPairList(
                    _primitiveTestSettingsPairFlow.value?.extraFieldNumbersList
                )
            )
        val testTaskTime =
            mapper.getSelectedFromPairList(
                _primitiveTestSettingsPairFlow.value?.testTaskTimePairList
            )

        viewModelScope.launch {
            primitiveTestSettingsUseCase.writeTestSettings(
                operation,
                PrimitiveTestSettingsModel(
                    testTasksNumber = testTaskNumber,
                    testComplexityLevel = testComplexityLevel,
                    taskDurationTimeSeconds = testTaskTime
                )
            )
        }
    }

    private fun createSettingsPair(
        settingsItems: PrimitiveTestSettingsPairModel,
        selectedSettings: PrimitiveTestSettingsModel,
        categoryCode: Int
    ) {
        _primitiveTestSettingsPairFlow.value = PrimitiveTestSettingsPairModel(
            testTaskTimePairList = mapper.setSelectedPairList(settingsItems.testTaskTimePairList, selectedSettings.taskDurationTimeSeconds),
            primitiveMathOperationType = settingsItems.primitiveMathOperationType,
            extraFieldNumbersList = mapper.setSelectedPairList(
                settingsItems.extraFieldNumbersList,
                mapper.getExtraFieldTargetNumber(mapper.getPrimitiveMathOperationTypeFromCode(categoryCode), selectedSettings.testComplexityLevel)
            ),
            testTaskComplexityCodesPairList = mapper.setSelectedPairList(
                settingsItems.testTaskComplexityCodesPairList,
                mapper.getComplexityCode(mapper.getPrimitiveMathOperationTypeFromCode(categoryCode), selectedSettings.testComplexityLevel)
            ),
            testTasksNumberPairList = mapper.setSelectedPairList(settingsItems.testTasksNumberPairList, selectedSettings.testTasksNumber)
        )
    }

}