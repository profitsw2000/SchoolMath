package ru.profitsw2000.core.domain.usecase

import ru.profitsw2000.core.domain.repository.PrimitiveTestSettingsRepository
import ru.profitsw2000.core.model.PrimitiveTestSettingsModel
import ru.profitsw2000.core.utils.PrimitiveMathOperationType

class PrimitiveTestSettingsUseCase(
    private val primitiveTestSettingsRepository: PrimitiveTestSettingsRepository
) {

    suspend fun getTestSettings(operationType: PrimitiveMathOperationType): PrimitiveTestSettingsModel {
        return when(operationType) {
            PrimitiveMathOperationType.ADDITION -> primitiveTestSettingsRepository.getAdditionTestSettings()
            PrimitiveMathOperationType.SUBTRACTION -> primitiveTestSettingsRepository.getSubtractionTestSettings()
            PrimitiveMathOperationType.MULTIPLICATION -> primitiveTestSettingsRepository.getMultiplicationTestSettings()
            PrimitiveMathOperationType.DIVISION -> primitiveTestSettingsRepository.getDivisionTestSettings()
        }
    }

    suspend fun writeTestSettings(
        primitiveMathOperationType: PrimitiveMathOperationType,
        primitiveTestSettingsModel: PrimitiveTestSettingsModel
    ) {
        when(primitiveMathOperationType) {
            PrimitiveMathOperationType.ADDITION -> primitiveTestSettingsRepository.writeAdditionTestSettings(primitiveTestSettingsModel)
            PrimitiveMathOperationType.SUBTRACTION -> primitiveTestSettingsRepository.writeSubtractionTestSettings(primitiveTestSettingsModel)
            PrimitiveMathOperationType.MULTIPLICATION -> primitiveTestSettingsRepository.writeMultiplicationTestSettings(primitiveTestSettingsModel)
            PrimitiveMathOperationType.DIVISION -> primitiveTestSettingsRepository.writeDivisionTestSettings(primitiveTestSettingsModel)
        }
    }
}