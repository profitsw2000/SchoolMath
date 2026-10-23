package ru.profitsw2000.settingsscreen.data.data.repository

import ru.profitsw2000.core.utils.GENERAL_TEST_HARD_COMPLEXITY_CODE
import ru.profitsw2000.core.utils.GENERAL_TEST_LIGHT_COMPLEXITY_CODE
import ru.profitsw2000.core.utils.GENERAL_TEST_MIDDLE_COMPLEXITY_CODE
import ru.profitsw2000.core.utils.GENERAL_TEST_NORMAL_COMPLEXITY_CODE
import ru.profitsw2000.core.utils.PrimitiveMathOperationType
import ru.profitsw2000.settingsscreen.data.domain.model.PrimitiveTestSettingsItemsModel
import ru.profitsw2000.settingsscreen.data.domain.repository.PrimitiveTestSettingsItemsRepository

class PrimitiveTestSettingsItemsRepositoryImpl : PrimitiveTestSettingsItemsRepository {
    override suspend fun getPrimitiveTestSettingsItems(
        primitiveMathOperationType: PrimitiveMathOperationType
    ): PrimitiveTestSettingsItemsModel {
        return PrimitiveTestSettingsItemsModel(
            testTaskTimeList(),
            testComplexityLevelList(primitiveMathOperationType),
            testTaskNumberList()
        )
    }

    private fun testTaskTimeList(): List<Int> {
        return listOf(10, 20, 30, 45, 60, 90, 120)
    }

    private fun testComplexityLevelList(primitiveMathOperationType: PrimitiveMathOperationType): List<Int> {
        return when(primitiveMathOperationType) {
            PrimitiveMathOperationType.ADDITION -> listOf(
                GENERAL_TEST_LIGHT_COMPLEXITY_CODE,
                GENERAL_TEST_NORMAL_COMPLEXITY_CODE,
                GENERAL_TEST_MIDDLE_COMPLEXITY_CODE,
                GENERAL_TEST_HARD_COMPLEXITY_CODE
            )
            PrimitiveMathOperationType.SUBTRACTION -> listOf(
                GENERAL_TEST_LIGHT_COMPLEXITY_CODE,
                GENERAL_TEST_NORMAL_COMPLEXITY_CODE,
                GENERAL_TEST_MIDDLE_COMPLEXITY_CODE,
                GENERAL_TEST_HARD_COMPLEXITY_CODE
            )
            PrimitiveMathOperationType.MULTIPLICATION -> listOf(
                GENERAL_TEST_LIGHT_COMPLEXITY_CODE,
                GENERAL_TEST_NORMAL_COMPLEXITY_CODE,
                GENERAL_TEST_MIDDLE_COMPLEXITY_CODE,
                GENERAL_TEST_HARD_COMPLEXITY_CODE
            )
            PrimitiveMathOperationType.DIVISION -> listOf(
                GENERAL_TEST_LIGHT_COMPLEXITY_CODE,
                GENERAL_TEST_NORMAL_COMPLEXITY_CODE,
                GENERAL_TEST_MIDDLE_COMPLEXITY_CODE,
                GENERAL_TEST_HARD_COMPLEXITY_CODE
            )
        }
    }

    private fun testTaskNumberList(): List<Int> {
        return listOf(5, 10, 15, 20, 25, 30, 40, 50, 60)
    }
}