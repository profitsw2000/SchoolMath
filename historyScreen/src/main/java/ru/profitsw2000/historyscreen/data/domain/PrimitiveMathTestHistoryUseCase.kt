package ru.profitsw2000.historyscreen.data.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.profitsw2000.core.model.PrimitiveTestResultModel
import ru.profitsw2000.core.utils.PrimitiveMathOperationType
import ru.profitsw2000.historyscreen.data.domain.repository.PrimitiveMathTestHistoryRepository

class PrimitiveMathTestHistoryUseCase(
    private val primitiveMathTestHistoryRepository: PrimitiveMathTestHistoryRepository
) {

    operator fun invoke(
        primitiveMathOperationType: PrimitiveMathOperationType?,
        testTasksNumber: Int?,
        testComplexityLevel: Int?,
        testDurationTime: Int?
    ): Flow<PagingData<PrimitiveTestResultModel>> {
        return primitiveMathTestHistoryRepository.getPagedTestResult(
            primitiveMathOperationType,
            testTasksNumber,
            testComplexityLevel,
            testDurationTime
        )
    }

}