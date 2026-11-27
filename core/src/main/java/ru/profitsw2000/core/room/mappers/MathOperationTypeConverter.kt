package ru.profitsw2000.core.room.mappers

import androidx.room.TypeConverter
import ru.profitsw2000.core.utils.PrimitiveMathOperationType

class MathOperationTypeConverter {
    @TypeConverter
    fun fromMathOperationType(mathOperationType: PrimitiveMathOperationType): String {
        return mathOperationType.name
    }

    @TypeConverter
    fun toMathOperationType(mathOperationType: String): PrimitiveMathOperationType {
        return try {
            PrimitiveMathOperationType.valueOf(mathOperationType)
        } catch (exc: Exception) {
            PrimitiveMathOperationType.ADDITION
        }
    }
}