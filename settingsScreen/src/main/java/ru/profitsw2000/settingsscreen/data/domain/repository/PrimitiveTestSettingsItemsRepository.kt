package ru.profitsw2000.settingsscreen.data.domain.repository

import ru.profitsw2000.core.utils.PrimitiveMathOperationType
import ru.profitsw2000.settingsscreen.data.domain.model.PrimitiveTestSettingsItemsModel

interface PrimitiveTestSettingsItemsRepository {

    suspend fun getPrimitiveTestSettingsItems(
        primitiveMathOperationType: PrimitiveMathOperationType
    ): PrimitiveTestSettingsItemsModel

}