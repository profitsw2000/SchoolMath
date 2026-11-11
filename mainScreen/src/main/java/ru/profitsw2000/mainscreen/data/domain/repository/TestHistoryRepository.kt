package ru.profitsw2000.mainscreen.data.domain.repository

import ru.profitsw2000.core.model.PrimitiveTestResultModel
import ru.profitsw2000.core.room.entity.PrimitiveTestResultEntity
import ru.profitsw2000.core.utils.PrimitiveMathOperationType

interface TestHistoryRepository {

    suspend fun getLastTestsResults(
        primitiveMathOperationType: PrimitiveMathOperationType,
        number: Int
    ): List<PrimitiveTestResultModel>

}