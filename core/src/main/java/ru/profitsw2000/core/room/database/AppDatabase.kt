package ru.profitsw2000.core.room.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.profitsw2000.core.room.dao.PrimitiveTestResultDao
import ru.profitsw2000.core.room.mappers.IntListConverter
import ru.profitsw2000.core.room.mappers.MathOperationTypeConverter

@TypeConverters(
    MathOperationTypeConverter::class,
    IntListConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract val primitiveTestResultDao: PrimitiveTestResultDao

    companion object {
        private const val DB_NAME = "database.db"
        private var instance: AppDatabase? = null

        fun getInstance() = instance ?: throw RuntimeException("Database has not been created. Please call create(context)")

        fun create(context: Context) {
            if (instance == null) {
                instance = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                    .build()
            }
        }
    }
}