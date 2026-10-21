package ru.profitsw2000.mainscreen.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.profitsw2000.mainscreen.data.domain.usecase.TestCategoryUseCase

class MainViewModel(
    private val testCategoryUseCase: TestCategoryUseCase,
    private val testHistoryUseCase: TestCategoryUseCase
) : ViewModel() {

    private var selectedPrimitiveMathOperationTypeCode = 0


    fun loadCategories() {
        viewModelScope.launch {
            val categoriesList = testCategoryUseCase.getTestCategoriesCodesList()


        }
    }

    fun loadCategorySettings() {
        viewModelScope.launch {

        }
    }

}