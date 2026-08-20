package ru.profitsw2000.simpletestsscreen.data.domain.repository

import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveTestSettingsModel

interface PrimitiveTestSettingsRepository {

    fun getAdditionTestSettings(): PrimitiveTestSettingsModel

    fun getSubtractionTestSettings(): PrimitiveTestSettingsModel

    fun getMultiplicationTestSettings(): PrimitiveTestSettingsModel

    fun getDivisionTestSettings(): PrimitiveTestSettingsModel

    suspend fun writeAdditionTestSettings(primitiveTestSettingsModel: PrimitiveTestSettingsModel)

    suspend fun writeSubtractionTestSettings(primitiveTestSettingsModel: PrimitiveTestSettingsModel)

    suspend fun writeMultiplicationTestSettings(primitiveTestSettingsModel: PrimitiveTestSettingsModel)

    suspend fun writeDivisionTestSettings(primitiveTestSettingsModel: PrimitiveTestSettingsModel)

}