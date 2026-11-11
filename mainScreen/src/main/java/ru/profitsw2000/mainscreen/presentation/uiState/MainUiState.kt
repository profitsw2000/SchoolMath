package ru.profitsw2000.mainscreen.presentation.uiState

sealed class MainUiState {
    data object Loading: MainUiState()
    data class Success(val categoriesCodesList: List<Int>): MainUiState()
    data object Error: MainUiState()
}