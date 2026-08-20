package ru.profitsw2000.simpletestsscreen.data.domain.usecase

import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveMathOperationType
import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveTestSettingsModel
import ru.profitsw2000.simpletestsscreen.data.domain.repository.PrimitiveTestSettingsRepository

class PrimitiveTestSettingsUseCase(
    private val primitiveTestSettingsRepository: PrimitiveTestSettingsRepository
) {

    fun getTestSettings(operationType: PrimitiveMathOperationType): PrimitiveTestSettingsModel {
        return when(operationType) {
            PrimitiveMathOperationType.ADDITION -> primitiveTestSettingsRepository.getAdditionTestSettings()
            PrimitiveMathOperationType.SUBTRACTION -> primitiveTestSettingsRepository.getSubtractionTestSettings()
            PrimitiveMathOperationType.MULTIPLICATION -> primitiveTestSettingsRepository.getMultiplicationTestSettings()
            PrimitiveMathOperationType.DIVISION -> primitiveTestSettingsRepository.getDivisionTestSettings()
        }
    }
}