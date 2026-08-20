package ru.profitsw2000.simpletestsscreen.data.data.repository
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveTestSettingsModel
import ru.profitsw2000.simpletestsscreen.data.domain.repository.PrimitiveTestSettingsRepository
import java.io.IOException

val Context.settingsDataStore by preferencesDataStore(name = "app_settings_pref")

class PrimitiveTestSettingsRepositoryImpl(
    private val context: Context,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
): PrimitiveTestSettingsRepository {

    private val json = Json { ignoreUnknownKeys = true }

    val additionTestSettingsFlow: StateFlow<PrimitiveTestSettingsModel> = context.settingsDataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }
        .map { preferences ->
            val jsonString = preferences[ADDITION_TEST_SETTINGS_JSON_KEY]
            if (jsonString != null) {
                try {
                    json.decodeFromString<PrimitiveTestSettingsModel>(jsonString)
                } catch (e: Exception) {
                    PrimitiveTestSettingsModel()
                }
            } else PrimitiveTestSettingsModel()
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Eagerly,
            initialValue = PrimitiveTestSettingsModel()
        )

    override fun getAdditionTestSettings(): PrimitiveTestSettingsModel {
        return additionTestSettingsFlow.value
    }

    override fun getSubtractionTestSettings(): PrimitiveTestSettingsModel {
        TODO("Not yet implemented")
    }

    override fun getMultiplicationTestSettings(): PrimitiveTestSettingsModel {
        TODO("Not yet implemented")
    }

    override fun getDivisionTestSettings(): PrimitiveTestSettingsModel {
        TODO("Not yet implemented")
    }

    override suspend fun writeAdditionTestSettings(primitiveTestSettingsModel: PrimitiveTestSettingsModel) {
        context.settingsDataStore.edit { preferences ->
            val jsonString = json.encodeToString(primitiveTestSettingsModel)
            preferences[ADDITION_TEST_SETTINGS_JSON_KEY] = jsonString
        }
    }

    override suspend fun writeSubtractionTestSettings(primitiveTestSettingsModel: PrimitiveTestSettingsModel) {
        TODO("Not yet implemented")
    }

    override suspend fun writeMultiplicationTestSettings(primitiveTestSettingsModel: PrimitiveTestSettingsModel) {
        TODO("Not yet implemented")
    }

    override suspend fun writeDivisionTestSettings(primitiveTestSettingsModel: PrimitiveTestSettingsModel) {
        TODO("Not yet implemented")
    }

    companion object {
        private val ADDITION_TEST_SETTINGS_JSON_KEY = stringPreferencesKey("addition_test_settings_json")
    }
}