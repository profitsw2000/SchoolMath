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

    val subtractionTestSettingsFlow: StateFlow<PrimitiveTestSettingsModel> = context.settingsDataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }
        .map { preferences ->
            val jsonString = preferences[SUBTRACTION_TEST_SETTINGS_JSON_KEY]
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

    val multiplicationTestSettingsFlow: StateFlow<PrimitiveTestSettingsModel> = context.settingsDataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }
        .map { preferences ->
            val jsonString = preferences[MULTIPLICATION_TEST_SETTINGS_JSON_KEY]
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

    val divisionTestSettingsFlow: StateFlow<PrimitiveTestSettingsModel> = context.settingsDataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }
        .map { preferences ->
            val jsonString = preferences[DIVISION_TEST_SETTINGS_JSON_KEY]
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
        return subtractionTestSettingsFlow.value
    }

    override fun getMultiplicationTestSettings(): PrimitiveTestSettingsModel {
        return multiplicationTestSettingsFlow.value
    }

    override fun getDivisionTestSettings(): PrimitiveTestSettingsModel {
        return divisionTestSettingsFlow.value
    }

    override suspend fun writeAdditionTestSettings(primitiveTestSettingsModel: PrimitiveTestSettingsModel) {
        context.settingsDataStore.edit { preferences ->
            val jsonString = json.encodeToString(primitiveTestSettingsModel)
            preferences[ADDITION_TEST_SETTINGS_JSON_KEY] = jsonString
        }
    }

    override suspend fun writeSubtractionTestSettings(primitiveTestSettingsModel: PrimitiveTestSettingsModel) {
        context.settingsDataStore.edit { preferences ->
            val jsonString = json.encodeToString(primitiveTestSettingsModel)
            preferences[SUBTRACTION_TEST_SETTINGS_JSON_KEY] = jsonString
        }
    }

    override suspend fun writeMultiplicationTestSettings(primitiveTestSettingsModel: PrimitiveTestSettingsModel) {
        context.settingsDataStore.edit { preferences ->
            val jsonString = json.encodeToString(primitiveTestSettingsModel)
            preferences[MULTIPLICATION_TEST_SETTINGS_JSON_KEY] = jsonString
        }
    }

    override suspend fun writeDivisionTestSettings(primitiveTestSettingsModel: PrimitiveTestSettingsModel) {
        context.settingsDataStore.edit { preferences ->
            val jsonString = json.encodeToString(primitiveTestSettingsModel)
            preferences[DIVISION_TEST_SETTINGS_JSON_KEY] = jsonString
        }
    }

    companion object {
        private val ADDITION_TEST_SETTINGS_JSON_KEY = stringPreferencesKey("addition_test_settings_json")
        private val SUBTRACTION_TEST_SETTINGS_JSON_KEY = stringPreferencesKey("subtraction_test_settings_json")
        private val MULTIPLICATION_TEST_SETTINGS_JSON_KEY = stringPreferencesKey("multiplication_test_settings_json")
        private val DIVISION_TEST_SETTINGS_JSON_KEY = stringPreferencesKey("division_test_settings_json")
    }
}

/*
class PrimitiveTestSettingsRepositoryImpl(
    private val context: Context
) : PrimitiveTestSettingsRepository {

    private val json = Json { ignoreUnknownKeys = true }

    // 1. Универсальный suspend-хелпер для чтения.
    // .first() берет первое доступное значение из Flow и закрывает подписку.
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
            .first() // Гарантирует ожидание загрузки с диска
    }

    // 2. Все геттеры теперь стали suspend функциями
    override suspend fun getAdditionTestSettings(): PrimitiveTestSettingsModel =
        getSettingsByKey(ADDITION_KEY)

    override suspend fun getSubtractionTestSettings(): PrimitiveTestSettingsModel =
        getSettingsByKey(SUBTRACTION_KEY)

    override suspend fun getMultiplicationTestSettings(): PrimitiveTestSettingsModel =
        getSettingsByKey(MULTIPLICATION_KEY)

    override suspend fun getDivisionTestSettings(): PrimitiveTestSettingsModel =
        getSettingsByKey(DIVISION_KEY)

    // 3. Универсальный хелпер для записи
    private suspend fun writeSettings(key: Preferences.Key<String>, model: PrimitiveTestSettingsModel) {
        context.settingsDataStore.edit { preferences ->
            preferences[key] = json.encodeToString(model)
        }
    }

    override suspend fun writeAdditionTestSettings(model: PrimitiveTestSettingsModel) = writeSettings(ADDITION_KEY, model)
    override suspend fun writeSubtractionTestSettings(model: PrimitiveTestSettingsModel) = writeSettings(SUBTRACTION_KEY, model)
    override suspend fun writeMultiplicationTestSettings(model: PrimitiveTestSettingsModel) = writeSettings(MULTIPLICATION_KEY, model)
    override suspend fun writeDivisionTestSettings(model: PrimitiveTestSettingsModel) = writeSettings(DIVISION_KEY, model)

    companion object {
        private val ADDITION_KEY = stringPreferencesKey("addition_test_settings_json")
        private val SUBTRACTION_KEY = stringPreferencesKey("subtraction_test_settings_json")
        private val MULTIPLICATION_KEY = stringPreferencesKey("multiplication_test_settings_json")
        private val DIVISION_KEY = stringPreferencesKey("division_test_settings_json")
    }
}*/
