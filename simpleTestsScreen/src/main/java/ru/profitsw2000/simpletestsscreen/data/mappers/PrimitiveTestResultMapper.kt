package ru.profitsw2000.simpletestsscreen.data.mappers

import ru.profitsw2000.core.room.entity.PrimitiveTestResultEntity
import ru.profitsw2000.core.utils.PrimitiveMathOperationType
import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveMathTaskModel
import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveTestResultModel

class PrimitiveTestResultMapper {

    fun map(primitiveTestResultModel: PrimitiveTestResultModel): PrimitiveTestResultEntity {
        return PrimitiveTestResultEntity(
            id = 0,
            settings = primitiveTestResultModel.settingsModel,
            correctAnswersNumber = primitiveTestResultModel.correctAnswersNumber,
            testAssessment = primitiveTestResultModel.testAssessment,
            totalTimeSeconds = primitiveTestResultModel.totalTimeSeconds,
            primitiveMathOperationType = primitiveTestResultModel.primitiveMathOperationType,
            firstOperandsList = firstOperandListFromModel(primitiveTestResultModel.primitiveMathTaskModelList),
            secondOperandList = secondOperandListFromModel(primitiveTestResultModel.primitiveMathTaskModelList),
            testResultList = primitiveTestResultModel.testResultsList,
            testTasksTimeList = primitiveTestResultModel.testTasksTimeList
        )
    }

    fun map(primitiveTestResultEntity: PrimitiveTestResultEntity): PrimitiveTestResultModel {
        return PrimitiveTestResultModel(
            settingsModel = primitiveTestResultEntity.settings,
            correctAnswersNumber = primitiveTestResultEntity.correctAnswersNumber,
            testAssessment = primitiveTestResultEntity.testAssessment,
            totalTimeSeconds = primitiveTestResultEntity.totalTimeSeconds,
            primitiveMathOperationType = primitiveTestResultEntity.primitiveMathOperationType,
            primitiveMathTaskModelList = modelFromParams(
                primitiveTestResultEntity.firstOperandsList,
                primitiveTestResultEntity.secondOperandList,
                primitiveTestResultEntity.primitiveMathOperationType
            ),
            testResultsList = primitiveTestResultEntity.testResultList,
            testTasksTimeList = primitiveTestResultEntity.testTasksTimeList
        )
    }

    private fun firstOperandListFromModel(
        primitiveMathTaskModelList: List<PrimitiveMathTaskModel>
    ): List<Int> {
        val paramList = mutableListOf<Int>()

        primitiveMathTaskModelList.forEach {
            paramList.add(it.firstOperand)
        }
        return paramList
    }

    private fun secondOperandListFromModel(
        primitiveMathTaskModelList: List<PrimitiveMathTaskModel>
    ): List<Int> {
        val paramList = mutableListOf<Int>()

        primitiveMathTaskModelList.forEach {
            paramList.add(it.secondOperand)
        }
        return paramList
    }

    private fun modelFromParams(
        firstOperandList: List<Int>,
        secondOperandList: List<Int>,
        primitiveMathOperationType: PrimitiveMathOperationType
    ): List<PrimitiveMathTaskModel> {
        val primitiveMathTaskModelList = mutableListOf<PrimitiveMathTaskModel>()

        if (firstOperandList.size != secondOperandList.size) throw IllegalStateException("Size of first and second operand list has to be equal")

        firstOperandList.forEachIndexed  { index, operand ->
            primitiveMathTaskModelList.add(
                PrimitiveMathTaskModel(
                    firstOperand = operand,
                    secondOperand = secondOperandList[index],
                    primitiveMathOperationType
                )
            )
        }

        return primitiveMathTaskModelList
    }

}