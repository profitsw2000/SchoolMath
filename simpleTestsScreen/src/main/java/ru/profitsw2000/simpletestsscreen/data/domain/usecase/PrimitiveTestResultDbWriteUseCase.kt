package ru.profitsw2000.simpletestsscreen.data.domain.usecase

import ru.profitsw2000.core.model.PrimitiveTestResultModel
import ru.profitsw2000.simpletestsscreen.data.domain.repository.PrimitiveTestResultDatabaseRepository
import ru.profitsw2000.core.mappers.PrimitiveTestResultMapper

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