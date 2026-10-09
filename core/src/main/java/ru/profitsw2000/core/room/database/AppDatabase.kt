package ru.profitsw2000.core.room.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.profitsw2000.core.room.dao.PrimitiveTestResultDao

abstract class AppDatabase : RoomDatabase() {
    abstract val primitiveTestResultDao: PrimitiveTestResultDao

    companion object {
        private val DB_NAME = "database.db"
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