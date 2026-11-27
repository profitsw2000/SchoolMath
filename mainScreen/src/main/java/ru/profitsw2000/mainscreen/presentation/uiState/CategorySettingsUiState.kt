package ru.profitsw2000.mainscreen.presentation.uiState

import ru.profitsw2000.core.model.PrimitiveTestSettingsModel
import ru.profitsw2000.mainscreen.data.model.PrimitiveTestSettingsCardModel

sealed class CategorySettingsUiState {
    data object Loading: CategorySettingsUiState()
    data class Success(val primitiveTestSettingsCardModel: PrimitiveTestSettingsCardModel): CategorySettingsUiState()
    data object Error: CategorySettingsUiState()
}