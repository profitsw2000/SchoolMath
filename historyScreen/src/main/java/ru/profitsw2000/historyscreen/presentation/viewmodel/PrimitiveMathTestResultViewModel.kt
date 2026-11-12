package ru.profitsw2000.historyscreen.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import ru.profitsw2000.core.model.PrimitiveTestResultModel
import ru.profitsw2000.historyscreen.data.domain.PrimitiveMathTestHistoryUseCase
import ru.profitsw2000.historyscreen.data.model.PrimitiveMathTestHistoryFilter

class PrimitiveMathTestResultViewModel(
    private val primitiveMathTestHistoryUseCase: PrimitiveMathTestHistoryUseCase
) : ViewModel() {

    private val _filterParameters = MutableStateFlow(PrimitiveMathTestHistoryFilter())
    val filterParameters: StateFlow<PrimitiveMathTestHistoryFilter> = _filterParameters.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagedPrimitiveTestResult: Flow<PagingData<PrimitiveTestResultModel>> = _filterParameters
        .flatMapLatest { params ->
            primitiveMathTestHistoryUseCase(
                primitiveMathOperationType = params.primitiveMathOperationType,
                testTasksNumber = params.testTasksNumber,
                testComplexityLevel = params.testComplexityLevel,
                testDurationTime = params.testTaskDuration
            )
        }
        .cachedIn(viewModelScope)

}