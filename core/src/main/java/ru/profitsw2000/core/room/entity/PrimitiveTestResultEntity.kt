package ru.profitsw2000.core.room.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.profitsw2000.core.model.PrimitiveMathTaskOperandsModel
import ru.profitsw2000.core.model.PrimitiveTestSettingsModel
import ru.profitsw2000.core.room.mappers.MathOperationTypeConverter
import ru.profitsw2000.core.utils.PrimitiveMathOperationType

@Entity(tableName = "test results")
data class PrimitiveTestResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @Embedded
    val settings: PrimitiveTestSettingsModel,
    val correctAnswersNumber: Int,
    val testAssessment: Int,
    val totalTimeSeconds: Int,
    @TypeConverters(MathOperationTypeConverter::class)
    val primitiveMathOperationType: PrimitiveMathOperationType,
    val firstOperandsList: List<Int>,
    val secondOperandList: List<Int>,
    val testResultList: List<Int>,
    val testTasksTimeList: List<Int>
)
