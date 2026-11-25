package ru.profitsw2000.mainscreen.presentation.uiState

import ru.profitsw2000.core.model.PrimitiveTestResultModel

sealed class CategoryHistoryUiState {
    data object Loading: CategoryHistoryUiState()
    data class Success(val primitiveTestResultModelList: List<PrimitiveTestResultModel>): CategoryHistoryUiState()
    data object Error: CategoryHistoryUiState()
}