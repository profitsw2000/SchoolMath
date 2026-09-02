package ru.profitsw2000.simpletestsscreen.data.data.repository.taskgenerator

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveMathTaskModel
import ru.profitsw2000.simpletestsscreen.data.domain.repository.PrimitiveTestTaskGeneratorRepository

class AdditionTestTaskGeneratorRepository(
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default
): PrimitiveTestTaskGeneratorRepository {

    override suspend fun generateTask(taskComplexityLevel: Int): PrimitiveMathTaskModel {
        withContext(coroutineDispatcher) {
            return PrimitiveMathTaskModel(

            )
        }
    }

    private fun getFirstOperand(taskComplexityLevel: Int): Int {
        return when(taskComplexityLevel) {

        }
    }

}
