package ru.profitsw2000.settingsscreen.data.domain.usecase

import ru.profitsw2000.core.utils.PrimitiveMathOperationType
import ru.profitsw2000.settingsscreen.data.domain.model.PrimitiveTestSettingsItemsModel
import ru.profitsw2000.settingsscreen.data.domain.repository.PrimitiveTestSettingsItemsRepository

class PrimitiveTestSettingsItemsUseCase(
    private val primitiveTestSettingsItemsRepository: PrimitiveTestSettingsItemsRepository
) {

    suspend fun getPrimitiveTestSettingsItems(
        primitiveMathOperationType: PrimitiveMathOperationType
    ): PrimitiveTestSettingsItemsModel {
        return primitiveTestSettingsItemsRepository.getPrimitiveTestSettingsItems(
            primitiveMathOperationType = primitiveMathOperationType
        )
    }

}