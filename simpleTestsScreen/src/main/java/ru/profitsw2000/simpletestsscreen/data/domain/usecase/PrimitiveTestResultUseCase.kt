package ru.profitsw2000.simpletestsscreen.data.domain.usecase

import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveMathTaskModel

class PrimitiveTestResultUseCase {

    suspend fun checkCalculationResult(
        primitiveMathTaskModel: PrimitiveMathTaskModel,
        calculationResult: Int
    ): Boolean {
        return TODO()
    }

}