package ru.profitsw2000.simpletestsscreen.data.domain.usecase

import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveMathOperationType
import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveMathTaskModel

class PrimitiveTestResultUseCase {

    suspend fun checkCalculationResult(
        primitiveMathTaskModel: PrimitiveMathTaskModel,
        calculationResult: Int
    ): Boolean {
        return when(primitiveMathTaskModel.primitiveMathOperationType) {
            PrimitiveMathOperationType.ADDITION ->
                checkAdditionTestResult(
                    primitiveMathTaskModel.firstOperand,
                    primitiveMathTaskModel.secondOperand,
                    calculationResult
                )
            PrimitiveMathOperationType.SUBTRACTION -> TODO()
            PrimitiveMathOperationType.MULTIPLICATION -> TODO()
            PrimitiveMathOperationType.DIVISION -> TODO()
        }
    }

    private fun checkAdditionTestResult(
        firstOperand: Int,
        secondOperand: Int,
        calculationResult: Int
    ): Boolean {
        return (firstOperand + secondOperand) == calculationResult
    }

}