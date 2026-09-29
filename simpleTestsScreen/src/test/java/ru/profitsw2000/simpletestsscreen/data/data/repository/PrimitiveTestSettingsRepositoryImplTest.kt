package ru.profitsw2000.simpletestsscreen.data.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveTestSettingsModel
import ru.profitsw2000.simpletestsscreen.utils.ADDITION_TEST_UNDER_TWENTY_RESULT_COMPLEX
import ru.profitsw2000.simpletestsscreen.utils.ADDITION_TEST_UNDER_TWENTY_RESULT_HIGH_COMPLEXITY
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class PrimitiveTestSettingsRepositoryImplTest {

    @get:Rule
    val temporaryFolder = TemporaryFolder()


    lateinit var tempFold: File

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher + Job())

    private lateinit var testDataStore: DataStore<Preferences>
    private val context: Context = mockk(relaxed = true)

    private lateinit var repository: PrimitiveTestSettingsRepositoryImpl

    @Before
    fun setUp() {
        testDataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = { File(temporaryFolder.newFolder(), "test_settings.preferences_pb") }
        )
        mockkStatic("ru.profitsw2000.simpletestsscreen.data.data.repository.PrimitiveTestSettingsRepositoryImplKt")

        every { context.settingsDataStore } returns testDataStore

        repository = PrimitiveTestSettingsRepositoryImpl(testDataStore)
    }

    @After
    fun tearDown() {
        io.mockk.unmockkStatic("ru.profitsw2000.simpletestsscreen.data.data.repository.PrimitiveTestSettingsRepositoryImplKt")
    }

    @Test
    fun `getAdditionTestSettings возвращает значение по умолчанию если хранилище настроек пустое `() = runTest(testDispatcher) {
        // Act
        val result = repository.getAdditionTestSettings()

        // Assert (ожидаем дефолтную пустую модель, так как в DataStore ничего нет)
        assertEquals(PrimitiveTestSettingsModel(), result)
    }

    @Test
    fun `запись настроек для примеров на сложение`() = runTest(testDispatcher) {
        val primitiveTestSettingsModel = PrimitiveTestSettingsModel(
            testTasksNumber = 15,
            testComplexityLevel = ADDITION_TEST_UNDER_TWENTY_RESULT_HIGH_COMPLEXITY,
            taskDurationTimeSeconds = 20
        )
        repository.writeAdditionTestSettings(primitiveTestSettingsModel)
        val actual = repository.getAdditionTestSettings()

        assertEquals(primitiveTestSettingsModel, actual)
    }

    @Test
    fun `функция getSettingsByKey возвращает настройки по умолчанию если json неверный`() = runTest(testDispatcher) {
        val additionKey = stringPreferencesKey("addition_test_settings_json")

        testDataStore.edit { preferences ->
            preferences[additionKey] = "{ invalid_json: [ } === сломанная строка ==="
        }

        val actual = repository.getAdditionTestSettings()
        val expected = PrimitiveTestSettingsModel()
        assertEquals(expected, actual)
    }

    @Test
    fun `возврат настроек по умолчанию при повреждении хранилища`() = runTest(testDispatcher) {
        val corruptedFolder = { File() }

    }

}