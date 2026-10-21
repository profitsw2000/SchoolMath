package ru.profitsw2000.mainscreen.data.data.repository

import ru.profitsw2000.core.room.dao.PrimitiveTestResultDao
import ru.profitsw2000.core.room.entity.PrimitiveTestResultEntity
import ru.profitsw2000.core.utils.PrimitiveMathOperationType
import ru.profitsw2000.mainscreen.data.domain.repository.TestHistoryRepository

class TestHistoryRepositoryImpl(
    private val primitiveTestResultDao: PrimitiveTestResultDao
): TestHistoryRepository {

    override suspend fun getLastTestsResults(
        primitiveMathOperationType: PrimitiveMathOperationType,
        number: Int
    ): List<PrimitiveTestResultEntity> {
        return primitiveTestResultDao.getLastDatabaseEntriesWithGivenOperationType(
            primitiveMathOperationType = primitiveMathOperationType,
            number = number
        )
    }

}