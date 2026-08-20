package ru.profitsw2000.simpletestsscreen.data.domain.repository

import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveTestSettingsModel

interface PrimitiveTestSettingsRepository {

    fun getAdditionTestSettings(): PrimitiveTestSettingsModel

    fun getSubtractionTestSettings(): PrimitiveTestSettingsModel

    fun getMultiplicationTestSettings(): PrimitiveTestSettingsModel

    fun getDivisionTestSettings(): PrimitiveTestSettingsModel

    fun writeAdditionTestSettings(primitiveTestSettingsModel: PrimitiveTestSettingsModel)

    fun writeSubtractionTestSettings(primitiveTestSettingsModel: PrimitiveTestSettingsModel)

    fun writeMultiplicationTestSettings(primitiveTestSettingsModel: PrimitiveTestSettingsModel)

    fun writeDivisionTestSettings(primitiveTestSettingsModel: PrimitiveTestSettingsModel)

}