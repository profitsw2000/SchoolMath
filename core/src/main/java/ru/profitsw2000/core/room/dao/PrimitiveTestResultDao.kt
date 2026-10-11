package ru.profitsw2000.core.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ru.profitsw2000.core.room.entity.PrimitiveTestResultEntity

@Dao
interface PrimitiveTestResultDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(primitiveTestResultEntity: PrimitiveTestResultEntity)

}