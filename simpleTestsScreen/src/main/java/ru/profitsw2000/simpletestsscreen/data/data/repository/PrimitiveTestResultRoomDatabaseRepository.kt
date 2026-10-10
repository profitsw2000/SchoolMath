package ru.profitsw2000.simpletestsscreen.data.data.repository

import ru.profitsw2000.core.room.dao.PrimitiveTestResultDao
import ru.profitsw2000.core.room.entity.PrimitiveTestResultEntity
import ru.profitsw2000.simpletestsscreen.data.domain.repository.PrimitiveTestResultDatabaseRepository

class PrimitiveTestResultRoomDatabaseRepository(
    private val primitiveTestResultDao: PrimitiveTestResultDao
) : PrimitiveTestResultDatabaseRepository {
    override suspend fun writeTestResultToDatabase(primitiveTestResultEntity: PrimitiveTestResultEntity) {
        primitiveTestResultDao.insert(primitiveTestResultEntity)
    }
}