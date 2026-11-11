package ru.profitsw2000.mainscreen.data.domain.usecase

import ru.profitsw2000.core.model.PrimitiveTestResultModel
import ru.profitsw2000.core.room.entity.PrimitiveTestResultEntity
import ru.profitsw2000.core.utils.PrimitiveMathOperationType
import ru.profitsw2000.mainscreen.data.domain.repository.TestHistoryRepository

class TestHistoryUseCase(
    private val testHistoryRepository: TestHistoryRepository
) {

    suspend fun getLastTestsResults(
        primitiveMathOperationType: PrimitiveMathOperationType,
        number: Int
    ): List<PrimitiveTestResultModel> {
        return testHistoryRepository.getLastTestsResults(
            primitiveMathOperationType,
            number
        )
    }

}