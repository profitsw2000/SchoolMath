package ru.profitsw2000.historyscreen.data.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.profitsw2000.core.model.PrimitiveTestResultModel
import ru.profitsw2000.core.utils.PrimitiveMathOperationType

interface PrimitiveMathTestHistoryRepository {

    fun getPagedTestResult(
        primitiveMathOperationType: PrimitiveMathOperationType? = null,
        testTasksNumber: Int? = null,
        testComplexityLevel: Int? = null,
        testDurationTime: Int? = null
    ): Flow<PagingData<PrimitiveTestResultModel>>

}