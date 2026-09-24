package ru.profitsw2000.simpletestsscreen.data.data.repository
import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveTestSettingsModel
import ru.profitsw2000.simpletestsscreen.data.domain.repository.PrimitiveTestSettingsRepository
import java.io.IOException

val Context.settingsDataStore by preferencesDataStore(name = "app_settings_pref")

class PrimitiveTestSettingsRepositoryImpl(
    private val context: Context
) : PrimitiveTestSettingsRepository {

    private val json = Json { ignoreUnknownKeys = true }

    private suspend fun getSettingsByKey(key: Preferences.Key<String>): PrimitiveTestSettingsModel {
        return context.settingsDataStore.data
            .catch { exception ->
                if (exception is IOException) emit(emptyPreferences())
                else throw exception
            }
            .map { preferences ->
                val jsonString = preferences[key] ?: return@map PrimitiveTestSettingsModel()
                try {
                    json.decodeFromString<PrimitiveTestSettingsModel>(jsonString)
                } catch (e: Exception) {
                    PrimitiveTestSettingsModel()
                }
            }
            .first()
    }

    override suspend fun getAdditionTestSettings(): PrimitiveTestSettingsModel =
        getSettingsByKey(ADDITION_KEY)

    override suspend fun getSubtractionTestSettings(): PrimitiveTestSettingsModel =
        getSettingsByKey(SUBTRACTION_KEY)

    override suspend fun getMultiplicationTestSettings(): PrimitiveTestSettingsModel =
        getSettingsByKey(MULTIPLICATION_KEY)

    override suspend fun getDivisionTestSettings(): PrimitiveTestSettingsModel =
        getSettingsByKey(DIVISION_KEY)

    private suspend fun writeSettings(key: Preferences.Key<String>, model: PrimitiveTestSettingsModel) {
        context.settingsDataStore.edit { preferences ->
            preferences[key] = json.encodeToString(model)
        }
    }

    override suspend fun writeAdditionTestSettings(primitiveTestSettingsModel: PrimitiveTestSettingsModel) = writeSettings(ADDITION_KEY, primitiveTestSettingsModel)
    override suspend fun writeSubtractionTestSettings(primitiveTestSettingsModel: PrimitiveTestSettingsModel) = writeSettings(SUBTRACTION_KEY, primitiveTestSettingsModel)
    override suspend fun writeMultiplicationTestSettings(primitiveTestSettingsModel: PrimitiveTestSettingsModel) = writeSettings(MULTIPLICATION_KEY, primitiveTestSettingsModel)
    override suspend fun writeDivisionTestSettings(primitiveTestSettingsModel: PrimitiveTestSettingsModel) = writeSettings(DIVISION_KEY, primitiveTestSettingsModel)

    companion object {
        private val ADDITION_KEY = stringPreferencesKey("addition_test_settings_json")
        private val SUBTRACTION_KEY = stringPreferencesKey("subtraction_test_settings_json")
        private val MULTIPLICATION_KEY = stringPreferencesKey("multiplication_test_settings_json")
        private val DIVISION_KEY = stringPreferencesKey("division_test_settings_json")
    }
}
