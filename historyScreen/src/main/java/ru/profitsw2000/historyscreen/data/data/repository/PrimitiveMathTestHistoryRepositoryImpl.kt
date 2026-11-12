package ru.profitsw2000.historyscreen.data.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSourceFactory
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.profitsw2000.core.mappers.PrimitiveTestResultMapper
import ru.profitsw2000.core.model.PrimitiveTestResultModel
import ru.profitsw2000.core.room.dao.PrimitiveTestResultDao
import ru.profitsw2000.core.utils.PrimitiveMathOperationType
import ru.profitsw2000.historyscreen.data.domain.repository.PrimitiveMathTestHistoryRepository

class PrimitiveMathTestHistoryRepositoryImpl(
    private val dao: PrimitiveTestResultDao,
    private val mapper: PrimitiveTestResultMapper
) : PrimitiveMathTestHistoryRepository {

    override fun getPagedTestResult(
        primitiveMathOperationType: PrimitiveMathOperationType?,
        testTasksNumber: Int?,
        testComplexityLevel: Int?,
        testDurationTime: Int?
    ): Flow<PagingData<PrimitiveTestResultModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 40
            )
        ) {
            dao.getPagedTestResults(
                primitiveMathOperationType,
                testTasksNumber,
                testComplexityLevel,
                testDurationTime
            )
        }.flow.map { pagingData ->
            pagingData.map { entity ->
                mapper.map(entity)
            }
        }

    }
}