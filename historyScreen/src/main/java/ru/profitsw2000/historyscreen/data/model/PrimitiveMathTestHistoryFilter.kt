package ru.profitsw2000.historyscreen.data.model

import ru.profitsw2000.core.utils.PrimitiveMathOperationType

data class PrimitiveMathTestHistoryFilter(
    val primitiveMathOperationType: PrimitiveMathOperationType? = null,
    val testTasksNumber: Int? = null,
    val testComplexityLevel: Int? = null,
    val testTaskDuration: Int? = null
)
