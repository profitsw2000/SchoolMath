package ru.profitsw2000.mainscreen.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.profitsw2000.core.domain.usecase.PrimitiveTestSettingsUseCase
import ru.profitsw2000.core.utils.PrimitiveMathOperationType
import ru.profitsw2000.mainscreen.data.domain.usecase.TestCategoryUseCase
import ru.profitsw2000.mainscreen.data.domain.usecase.TestHistoryUseCase
import ru.profitsw2000.mainscreen.data.utils.ADDITION_CATEGORY_CODE
import ru.profitsw2000.mainscreen.data.utils.DIVISION_CATEGORY_CODE
import ru.profitsw2000.mainscreen.data.utils.LOADED_TEST_RESULTS_NUMBER
import ru.profitsw2000.mainscreen.data.utils.MULTIPLICATION_CATEGORY_CODE
import ru.profitsw2000.mainscreen.data.utils.SUBTRACTION_CATEGORY_CODE
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
    val categoryUiState: StateFlow<MainUiState> by this :: _categoryUiState
    private val _settingsUiState = MutableStateFlow<CategorySettingsUiState>(CategorySettingsUiState.Loading)
    val settingsUiState: StateFlow<CategorySettingsUiState> by this :: _settingsUiState
    private val _historyUiState = MutableStateFlow<CategoryHistoryUiState>(CategoryHistoryUiState.Loading)
    val historyUiState: StateFlow<CategoryHistoryUiState> by this :: _historyUiState

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
                _settingsUiState.value = CategorySettingsUiState.Success(categorySettings)
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

}