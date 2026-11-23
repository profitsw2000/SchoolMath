package ru.profitsw2000.mainscreen.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.profitsw2000.core.domain.usecase.PrimitiveTestSettingsUseCase
import ru.profitsw2000.core.model.PrimitiveTestSettingsModel
import ru.profitsw2000.core.utils.PrimitiveMathOperationType
import ru.profitsw2000.mainscreen.data.domain.usecase.TestCategoryUseCase
import ru.profitsw2000.mainscreen.data.domain.usecase.TestHistoryUseCase
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
import ru.profitsw2000.mainscreen.data.utils.LOADED_TEST_RESULTS_NUMBER
import ru.profitsw2000.core.utils.MULTIPLICATION_CATEGORY_CODE
import ru.profitsw2000.core.utils.SUBTRACTION_CATEGORY_CODE
import ru.profitsw2000.mainscreen.data.model.PrimitiveTestSettingsCardModel
import ru.profitsw2000.mainscreen.presentation.uiState.CategoryHistoryUiState
import ru.profitsw2000.mainscreen.presentation.uiState.CategorySettingsUiState
import ru.profitsw2000.mainscreen.presentation.uiState.MainUiState

class MainViewModel(
    private val testCategoryUseCase: TestCategoryUseCase,
    private val testHistoryUseCase: TestHistoryUseCase,
    private val primitiveTestSettingsUseCase: PrimitiveTestSettingsUseCase
) : ViewModel() {

    private var selectedPrimitiveMathOperationTypeCode = 0
    private val _categoryUiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val categoryUiState: StateFlow<MainUiState> = _categoryUiState.asStateFlow()
    private val _settingsUiState = MutableStateFlow<CategorySettingsUiState>(CategorySettingsUiState.Loading)
    val settingsUiState: StateFlow<CategorySettingsUiState> = _settingsUiState.asStateFlow()
    private val _historyUiState = MutableStateFlow<CategoryHistoryUiState>(CategoryHistoryUiState.Loading)
    val historyUiState: StateFlow<CategoryHistoryUiState> = _historyUiState.asStateFlow()

    fun loadCategories() {
        _categoryUiState.value = MainUiState.Loading
        viewModelScope.launch {
            val categoriesList = testCategoryUseCase.getTestCategoriesCodesList()
            _categoryUiState.value = MainUiState.Success(categoriesList)
        }
    }

    fun loadCategorySettings() {
        _settingsUiState.value = CategorySettingsUiState.Loading
        viewModelScope.launch {
            try {
                val categorySettings = primitiveTestSettingsUseCase.getTestSettings(
                    getOperationTypeFromCategoryCode(selectedPrimitiveMathOperationTypeCode)
                )
                _settingsUiState.value = CategorySettingsUiState.Success(
                    categorySettings
                )
            } catch (exc: Exception) {
                _settingsUiState.value = CategorySettingsUiState.Error
            }
        }
    }

    fun loadCategoryHistory() {
        _historyUiState.value = CategoryHistoryUiState.Loading
        viewModelScope.launch {
            try {
                val testResultHistory = testHistoryUseCase.getLastTestsResults(
                    getOperationTypeFromCategoryCode(
                        selectedPrimitiveMathOperationTypeCode
                    ),
                    LOADED_TEST_RESULTS_NUMBER
                )
                _historyUiState.value = CategoryHistoryUiState.Success(
                    testResultHistory
                )
            } catch (exc: Exception) {
                _historyUiState.value = CategoryHistoryUiState.Error
            }
        }
    }

    fun setCategory(categoryCode: Int) {
        selectedPrimitiveMathOperationTypeCode = categoryCode
    }

    private fun getOperationTypeFromCategoryCode(categoryCode: Int): PrimitiveMathOperationType {
        return when(categoryCode) {
            ADDITION_CATEGORY_CODE -> PrimitiveMathOperationType.ADDITION
            SUBTRACTION_CATEGORY_CODE -> PrimitiveMathOperationType.SUBTRACTION
            MULTIPLICATION_CATEGORY_CODE -> PrimitiveMathOperationType.MULTIPLICATION
            DIVISION_CATEGORY_CODE -> PrimitiveMathOperationType.DIVISION
            else -> PrimitiveMathOperationType.ADDITION
        }
    }

    private fun mapTestSettings(
        primitiveTestSettingsModel: PrimitiveTestSettingsModel
    ): PrimitiveTestSettingsCardModel {
        val taskDurationMinutes = primitiveTestSettingsModel.taskDurationTimeSeconds/60
        val taskDurationSeconds = primitiveTestSettingsModel.taskDurationTimeSeconds%60
        val taskDurationSecondsString = if (taskDurationSeconds == 0) "00"
        else taskDurationSeconds.toString()

        return PrimitiveTestSettingsCardModel(
            primitiveTestSettingsModel.testTasksNumber.toString(),
            "$taskDurationMinutes:$taskDurationSecondsString",
            getExtraFieldNumber(primitiveTestSettingsModel.testComplexityLevel)

        )
    }

    private fun getExtraFieldNumber(complexityCode: Int): Int {
        when(selectedPrimitiveMathOperationTypeCode) {
            ADDITION_CATEGORY_CODE -> getAdditionExtraFieldNumber(complexityCode)
            SUBTRACTION_CATEGORY_CODE -> {}
            MULTIPLICATION_CATEGORY_CODE -> {}
            DIVISION_CATEGORY_CODE -> {}
            else -> {}
        }
    }

    private fun getAdditionExtraFieldNumber(complexityCode: Int): Int {
        return when(complexityCode) {
            ADDITION_TEST_UNDER_TEN_RESULT_SIMPLE -> 10
            ADDITION_TEST_UNDER_TEN_RESULT_INTERMEDIATE -> 10
            ADDITION_TEST_UNDER_TEN_RESULT_COMPLEX -> 10
            ADDITION_TEST_UNDER_TEN_RESULT_HIGH_COMPLEXITY -> 10
            ADDITION_TEST_UNDER_TWENTY_RESULT_SIMPLE -> 20
            ADDITION_TEST_UNDER_TWENTY_RESULT_INTERMEDIATE -> 20
            ADDITION_TEST_UNDER_TWENTY_RESULT_COMPLEX -> 20
            ADDITION_TEST_UNDER_TWENTY_RESULT_HIGH_COMPLEXITY -> 20
            ADDITION_TEST_UNDER_HUNDRED_RESULT_SIMPLE -> 100
            ADDITION_TEST_UNDER_HUNDRED_RESULT_INTERMEDIATE -> 100
            ADDITION_TEST_UNDER_HUNDRED_RESULT_COMPLEX -> 100
            ADDITION_TEST_UNDER_HUNDRED_RESULT_HIGH_COMPLEXITY -> 100
            else -> 10
        }
    }

}