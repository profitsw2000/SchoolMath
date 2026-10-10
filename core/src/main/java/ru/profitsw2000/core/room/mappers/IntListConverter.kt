package ru.profitsw2000.core.room.mappers

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class IntListConverter {

    @TypeConverter
    fun fromIntList(value: List<Int>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toIntList(value: String): List<Int> {
        return try {
            Json.decodeFromString(value)
        } catch (exc: Exception) {
            emptyList()
        }
    }
}