package ru.profitsw2000.simpletestsscreen.data.domain.repository

import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveMathTaskModel

interface PrimitiveTestTaskGeneratorRepository {

    fun generateTask(taskComplexityLevel: Int): PrimitiveMathTaskModel

}