package ru.profitsw2000.mainscreen.presentation.uiState

import ru.profitsw2000.core.model.PrimitiveTestSettingsModel

sealed class CategorySettingsUiState {
    data object Loading: CategorySettingsUiState()
    data class Success(val primitiveTestSettingsModel: PrimitiveTestSettingsModel): CategorySettingsUiState()
    data object Error: CategorySettingsUiState()
}