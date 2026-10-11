package ru.profitsw2000.simpletestsscreen.data.domain.repository

import ru.profitsw2000.core.room.entity.PrimitiveTestResultEntity

interface PrimitiveTestResultDatabaseRepository {

    suspend fun writeTestResultToDatabase(primitiveTestResultEntity: PrimitiveTestResultEntity)

}