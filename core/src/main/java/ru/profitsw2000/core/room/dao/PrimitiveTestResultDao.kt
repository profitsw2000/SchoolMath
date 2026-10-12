package ru.profitsw2000.core.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.profitsw2000.core.room.entity.PrimitiveTestResultEntity
import ru.profitsw2000.core.utils.PrimitiveMathOperationType

@Dao
interface PrimitiveTestResultDao {

    @Query("SELECT * FROM `test results` " +
            "WHERE (:primitiveMathOperationType IS NULL OR primitiveMathOperationType = :primitiveMathOperationType) " +
            "AND (:testTasksNumber IS NULL OR testTasksNumber = :testTasksNumber) " +
            "AND (:testComplexityLevel IS NULL OR testComplexityLevel = :testComplexityLevel) " +
            "AND (:testDurationTime IS NULL OR taskDurationTimeSeconds = :testDurationTime) " +
            "ORDER BY id DESC")
    suspend fun getPagedTestResults(
        primitiveMathOperationType: PrimitiveMathOperationType? = null,
        testTasksNumber: Int? = null,
        testComplexityLevel: Int? = null,
        testDurationTime: Int? = null
    ): PagingSource<Int, PrimitiveTestResultEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(primitiveTestResultEntity: PrimitiveTestResultEntity)

}