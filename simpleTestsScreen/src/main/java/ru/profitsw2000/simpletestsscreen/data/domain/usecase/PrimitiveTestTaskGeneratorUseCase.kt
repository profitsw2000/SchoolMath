package ru.profitsw2000.simpletestsscreen.data.domain.usecase

import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveMathTaskModel

class PrimitiveTestTaskGeneratorUseCase {

    suspend fun getAdditionTask(taskComplexityLevel: Int): PrimitiveMathTaskModel {
        return TODO()
    }

    suspend fun getSubtractionTask(taskComplexityLevel: Int): PrimitiveMathTaskModel {
        return TODO()
    }

    suspend fun getMultiplicationTask(taskComplexityLevel: Int): PrimitiveMathTaskModel {
        return TODO()
    }

    suspend fun getDivisionTask(taskComplexityLevel: Int): PrimitiveMathTaskModel {
        return TODO()
    }
}