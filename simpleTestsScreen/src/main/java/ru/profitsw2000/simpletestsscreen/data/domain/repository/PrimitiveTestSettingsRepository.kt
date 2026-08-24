package ru.profitsw2000.simpletestsscreen.data.domain.repository

import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveTestSettingsModel

interface PrimitiveTestSettingsRepository {

    suspend fun getAdditionTestSettings(): PrimitiveTestSettingsModel

    suspend fun getSubtractionTestSettings(): PrimitiveTestSettingsModel

    suspend fun getMultiplicationTestSettings(): PrimitiveTestSettingsModel

    suspend fun getDivisionTestSettings(): PrimitiveTestSettingsModel

    suspend fun writeAdditionTestSettings(primitiveTestSettingsModel: PrimitiveTestSettingsModel)

    suspend fun writeSubtractionTestSettings(primitiveTestSettingsModel: PrimitiveTestSettingsModel)

    suspend fun writeMultiplicationTestSettings(primitiveTestSettingsModel: PrimitiveTestSettingsModel)

    suspend fun writeDivisionTestSettings(primitiveTestSettingsModel: PrimitiveTestSettingsModel)

}