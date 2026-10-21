package ru.profitsw2000.mainscreen.data.data.repository

import ru.profitsw2000.core.mappers.PrimitiveTestResultMapper
import ru.profitsw2000.core.model.PrimitiveTestResultModel
import ru.profitsw2000.core.room.dao.PrimitiveTestResultDao
import ru.profitsw2000.core.room.entity.PrimitiveTestResultEntity
import ru.profitsw2000.core.utils.PrimitiveMathOperationType
import ru.profitsw2000.mainscreen.data.domain.repository.TestHistoryRepository

class TestHistoryRepositoryImpl(
    private val primitiveTestResultDao: PrimitiveTestResultDao,
    private val mapper: PrimitiveTestResultMapper
): TestHistoryRepository {

    override suspend fun getLastTestsResults(
        primitiveMathOperationType: PrimitiveMathOperationType,
        number: Int
    ): List<PrimitiveTestResultModel> {
        return mapper.map(
            primitiveTestResultDao.getLastDatabaseEntriesWithGivenOperationType(
                primitiveMathOperationType = primitiveMathOperationType,
                number = number
            )
        )
    }

}