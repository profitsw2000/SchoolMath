package ru.profitsw2000.settingsscreen.presentation.utils

import ru.profitsw2000.core.model.PrimitiveTestSettingsModel
import ru.profitsw2000.core.utils.ADDITION_CATEGORY_CODE
import ru.profitsw2000.core.utils.ADDITION_TEST_UNDER_HUNDRED_RESULT_COMPLEX
import ru.profitsw2000.core.utils.ADDITION_TEST_UNDER_HUNDRED_RESULT_HIGH_COMPLEXITY
import ru.profitsw2000.core.utils.ADDITION_TEST_UNDER_HUNDRED_RESULT_INTERMEDIATE
import ru.profitsw2000.core.utils.ADDITION_TEST_UNDER_HUNDRED_RESULT_SIMPLE
import ru.profitsw2000.core.utils.ADDITION_TEST_UNDER_TEN_RESULT_COMPLEX
import ru.profitsw2000.core.utils.ADDITION_TEST_UNDER_TEN_RESULT_HIGH_COMPLEXITY
import ru.profitsw2000.core.utils.ADDITION_TEST_UNDER_TEN_RESULT_INTERMEDIATE
import ru.profitsw2000.core.utils.ADDITION_TEST_UNDER_TEN_RESULT_SIMPLE
import ru.profitsw2000.core.utils.ADDITION_TEST_UNDER_TWENTY_RESULT_COMPLEX
import ru.profitsw2000.core.utils.ADDITION_TEST_UNDER_TWENTY_RESULT_HIGH_COMPLEXITY
import ru.profitsw2000.core.utils.ADDITION_TEST_UNDER_TWENTY_RESULT_INTERMEDIATE
import ru.profitsw2000.core.utils.ADDITION_TEST_UNDER_TWENTY_RESULT_SIMPLE
import ru.profitsw2000.core.utils.DIVISION_CATEGORY_CODE
import ru.profitsw2000.core.utils.GENERAL_TEST_HARD_COMPLEXITY_CODE
import ru.profitsw2000.core.utils.GENERAL_TEST_LIGHT_COMPLEXITY_CODE
import ru.profitsw2000.core.utils.GENERAL_TEST_MIDDLE_COMPLEXITY_CODE
import ru.profitsw2000.core.utils.GENERAL_TEST_NORMAL_COMPLEXITY_CODE
import ru.profitsw2000.core.utils.MULTIPLICATION_CATEGORY_CODE
import ru.profitsw2000.core.utils.PrimitiveMathOperationType
import ru.profitsw2000.core.utils.SUBTRACTION_CATEGORY_CODE
import ru.profitsw2000.settingsscreen.data.domain.model.PrimitiveTestSettingsItemsModel
import ru.profitsw2000.settingsscreen.data.domain.model.PrimitiveTestSettingsPairModel

class PrimitiveMathOperationSettingsMapper {

    fun settingsItemsToPairsList(
        primitiveTestSettingsItemsModel: PrimitiveTestSettingsItemsModel
    ): PrimitiveTestSettingsPairModel {
        return PrimitiveTestSettingsPairModel(
            testTaskTimePairList = intListToPairs(primitiveTestSettingsItemsModel.taskTimesList),
            primitiveMathOperationType = primitiveTestSettingsItemsModel.primitiveMathOperationType,
            extraFieldNumbersList = intListToPairs(primitiveTestSettingsItemsModel.extraFieldNumbersList),
            testTaskComplexityCodesPairList = intListToPairs(primitiveTestSettingsItemsModel.taskComplexityCodesList),
            testTasksNumberPairList = intListToPairs(primitiveTestSettingsItemsModel.taskNumberList)
        )
    }

    private fun intListToPairs(intList: List<Int>): List<Pair<Int, Boolean>> {
        val resultPairList: MutableList<Pair<Int, Boolean>> = mutableListOf()

        intList.forEach { item ->
            resultPairList.add(Pair(item, false))
        }
        return resultPairList
    }

    fun getPrimitiveMathOperationTypeFromCode(categoryCode: Int): PrimitiveMathOperationType {
        return when(categoryCode) {
            ADDITION_CATEGORY_CODE -> PrimitiveMathOperationType.ADDITION
            SUBTRACTION_CATEGORY_CODE -> PrimitiveMathOperationType.SUBTRACTION
            MULTIPLICATION_CATEGORY_CODE -> PrimitiveMathOperationType.MULTIPLICATION
            DIVISION_CATEGORY_CODE -> PrimitiveMathOperationType.DIVISION
            else -> PrimitiveMathOperationType.ADDITION
        }
    }

    fun getExtraFieldTargetNumber(
        primitiveMathOperationType: PrimitiveMathOperationType,
        testComplexityLevel: Int
    ): Int {
        return when(primitiveMathOperationType) {
            PrimitiveMathOperationType.ADDITION -> getAdditionSettingsExtraFieldTargetNumber(testComplexityLevel)
            PrimitiveMathOperationType.SUBTRACTION -> getSubtractionSettingsExtraFieldTargetNumber(testComplexityLevel)
            PrimitiveMathOperationType.MULTIPLICATION -> TODO()
            PrimitiveMathOperationType.DIVISION -> TODO()
        }
    }

    fun getComplexityCode(
        primitiveMathOperationType: PrimitiveMathOperationType,
        testComplexityLevel: Int
    ): Int {
        return when(primitiveMathOperationType) {
            PrimitiveMathOperationType.ADDITION -> getAdditionTestComplexityCode(testComplexityLevel)
            PrimitiveMathOperationType.SUBTRACTION -> TODO()
            PrimitiveMathOperationType.MULTIPLICATION -> TODO()
            PrimitiveMathOperationType.DIVISION -> TODO()
        }
    }

    private fun getAdditionSettingsExtraFieldTargetNumber(
        testComplexityLevel: Int
    ): Int {
        return when(testComplexityLevel) {
            ADDITION_TEST_UNDER_TEN_RESULT_SIMPLE -> 10
            ADDITION_TEST_UNDER_TEN_RESULT_INTERMEDIATE -> 10
            ADDITION_TEST_UNDER_TEN_RESULT_COMPLEX -> 10
            ADDITION_TEST_UNDER_TEN_RESULT_HIGH_COMPLEXITY -> 10
            ADDITION_TEST_UNDER_TWENTY_RESULT_SIMPLE -> 20
            ADDITION_TEST_UNDER_TWENTY_RESULT_INTERMEDIATE -> 20
            ADDITION_TEST_UNDER_TWENTY_RESULT_COMPLEX -> 20
            ADDITION_TEST_UNDER_TWENTY_RESULT_HIGH_COMPLEXITY -> 20
            ADDITION_TEST_UNDER_HUNDRED_RESULT_SIMPLE -> 100
            ADDITION_TEST_UNDER_HUNDRED_RESULT_INTERMEDIATE -> 100
            ADDITION_TEST_UNDER_HUNDRED_RESULT_COMPLEX -> 100
            ADDITION_TEST_UNDER_HUNDRED_RESULT_HIGH_COMPLEXITY -> 100
            else -> 10
        }
    }

    private fun getSubtractionSettingsExtraFieldTargetNumber(
        testComplexityLevel: Int
    ): Int {
        return 0
    }

    private fun getAdditionTestComplexityCode(
        testComplexityLevel: Int
    ): Int {
        return when(testComplexityLevel) {
            ADDITION_TEST_UNDER_TEN_RESULT_SIMPLE -> GENERAL_TEST_LIGHT_COMPLEXITY_CODE
            ADDITION_TEST_UNDER_TEN_RESULT_INTERMEDIATE -> GENERAL_TEST_NORMAL_COMPLEXITY_CODE
            ADDITION_TEST_UNDER_TEN_RESULT_COMPLEX -> GENERAL_TEST_MIDDLE_COMPLEXITY_CODE
            ADDITION_TEST_UNDER_TEN_RESULT_HIGH_COMPLEXITY -> GENERAL_TEST_HARD_COMPLEXITY_CODE
            ADDITION_TEST_UNDER_TWENTY_RESULT_SIMPLE -> GENERAL_TEST_LIGHT_COMPLEXITY_CODE
            ADDITION_TEST_UNDER_TWENTY_RESULT_INTERMEDIATE -> GENERAL_TEST_NORMAL_COMPLEXITY_CODE
            ADDITION_TEST_UNDER_TWENTY_RESULT_COMPLEX -> GENERAL_TEST_MIDDLE_COMPLEXITY_CODE
            ADDITION_TEST_UNDER_TWENTY_RESULT_HIGH_COMPLEXITY -> GENERAL_TEST_HARD_COMPLEXITY_CODE
            ADDITION_TEST_UNDER_HUNDRED_RESULT_SIMPLE -> GENERAL_TEST_LIGHT_COMPLEXITY_CODE
            ADDITION_TEST_UNDER_HUNDRED_RESULT_INTERMEDIATE -> GENERAL_TEST_NORMAL_COMPLEXITY_CODE
            ADDITION_TEST_UNDER_HUNDRED_RESULT_COMPLEX -> GENERAL_TEST_MIDDLE_COMPLEXITY_CODE
            ADDITION_TEST_UNDER_HUNDRED_RESULT_HIGH_COMPLEXITY -> GENERAL_TEST_HARD_COMPLEXITY_CODE
            else -> GENERAL_TEST_LIGHT_COMPLEXITY_CODE
        }
    }

    fun getTestComplexityLevel(
        primitiveMathOperationType: PrimitiveMathOperationType,
        testComplexityCode: Int,
        extraFieldNumber: Int
    ): Int {
        return when(primitiveMathOperationType) {
            PrimitiveMathOperationType.ADDITION -> getAdditionTestComplexityLevel(testComplexityCode, extraFieldNumber)
            PrimitiveMathOperationType.SUBTRACTION -> TODO()
            PrimitiveMathOperationType.MULTIPLICATION -> TODO()
            PrimitiveMathOperationType.DIVISION -> TODO()
        }
    }

    private fun getAdditionTestComplexityLevel(
        testComplexityCode: Int,
        extraFieldNumber: Int
    ): Int {
        return when(testComplexityCode) {
            GENERAL_TEST_LIGHT_COMPLEXITY_CODE -> getLightAdditionTestComplexityLevel(extraFieldNumber)
            GENERAL_TEST_NORMAL_COMPLEXITY_CODE -> getNormalAdditionTestComplexityLevel(extraFieldNumber)
            GENERAL_TEST_MIDDLE_COMPLEXITY_CODE -> getMiddleAdditionTestComplexityLevel(extraFieldNumber)
            GENERAL_TEST_HARD_COMPLEXITY_CODE -> getHardAdditionTestComplexityLevel(extraFieldNumber)
            else -> getLightAdditionTestComplexityLevel(extraFieldNumber)
        }
    }

    private fun getLightAdditionTestComplexityLevel(
        extraFieldNumber: Int
    ): Int {
        return when(extraFieldNumber) {
            10 -> ADDITION_TEST_UNDER_TEN_RESULT_SIMPLE
            20 -> ADDITION_TEST_UNDER_TWENTY_RESULT_SIMPLE
            100 -> ADDITION_TEST_UNDER_HUNDRED_RESULT_SIMPLE
            else -> ADDITION_TEST_UNDER_TEN_RESULT_SIMPLE
        }
    }

    private fun getNormalAdditionTestComplexityLevel(
        extraFieldNumber: Int
    ): Int {
        return when(extraFieldNumber) {
            10 -> ADDITION_TEST_UNDER_TEN_RESULT_INTERMEDIATE
            20 -> ADDITION_TEST_UNDER_TWENTY_RESULT_INTERMEDIATE
            100 -> ADDITION_TEST_UNDER_HUNDRED_RESULT_INTERMEDIATE
            else -> ADDITION_TEST_UNDER_TEN_RESULT_INTERMEDIATE
        }
    }

    private fun getMiddleAdditionTestComplexityLevel(
        extraFieldNumber: Int
    ): Int {
        return when(extraFieldNumber) {
            10 -> ADDITION_TEST_UNDER_TEN_RESULT_COMPLEX
            20 -> ADDITION_TEST_UNDER_TWENTY_RESULT_COMPLEX
            100 -> ADDITION_TEST_UNDER_HUNDRED_RESULT_COMPLEX
            else -> ADDITION_TEST_UNDER_TEN_RESULT_COMPLEX
        }
    }

    private fun getHardAdditionTestComplexityLevel(
        extraFieldNumber: Int
    ): Int {
        return when(extraFieldNumber) {
            10 -> ADDITION_TEST_UNDER_TEN_RESULT_HIGH_COMPLEXITY
            20 -> ADDITION_TEST_UNDER_TWENTY_RESULT_HIGH_COMPLEXITY
            100 -> ADDITION_TEST_UNDER_HUNDRED_RESULT_HIGH_COMPLEXITY
            else -> ADDITION_TEST_UNDER_TEN_RESULT_HIGH_COMPLEXITY
        }
    }

    fun setSelectedPairList(
        pairList: List<Pair<Int, Boolean>>,
        targetNumber: Int
    ): List<Pair<Int, Boolean>> {
        return pairList.map { pair ->
            pair.copy(second = pair.first == targetNumber)
        }
    }

    fun getSelectedFromPairList(
        pairList: List<Pair<Int, Boolean>>?
    ): Int {
        pairList?.let {
            pairList.forEach { item ->
                if (item.second) return item.first
            }
            return pairList[0].first
        }
        return 0
    }

}