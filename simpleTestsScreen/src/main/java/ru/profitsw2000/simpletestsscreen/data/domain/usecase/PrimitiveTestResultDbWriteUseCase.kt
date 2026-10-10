package ru.profitsw2000.simpletestsscreen.data.domain.usecase

import ru.profitsw2000.core.room.entity.PrimitiveTestResultEntity
import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveTestResultModel
import ru.profitsw2000.simpletestsscreen.data.domain.repository.PrimitiveTestResultDatabaseRepository
import ru.profitsw2000.simpletestsscreen.data.mappers.PrimitiveTestResultMapper

class PrimitiveTestResultDbWriteUseCase(
    private val primitiveTestResultDatabaseRepository: PrimitiveTestResultDatabaseRepository,
    private val primitiveTestResultMapper: PrimitiveTestResultMapper
) {

    suspend fun writeTestResultToDatabase(primitiveTestResultModel: PrimitiveTestResultModel) {
        primitiveTestResultDatabaseRepository.writeTestResultToDatabase(
            primitiveTestResultMapper.map(primitiveTestResultModel)
        )
    }

}