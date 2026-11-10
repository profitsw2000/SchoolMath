package ru.profitsw2000.settingsscreen.presentation.viewmodel

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import ru.profitsw2000.core.domain.usecase.PrimitiveTestSettingsUseCase
import ru.profitsw2000.core.model.PrimitiveTestSettingsModel
import ru.profitsw2000.core.utils.PrimitiveMathOperationType
import ru.profitsw2000.coroutines.MainDispatcherRule
import ru.profitsw2000.settingsscreen.data.domain.model.PrimitiveTestSettingsItemsModel
import ru.profitsw2000.settingsscreen.data.domain.usecase.PrimitiveTestSettingsItemsUseCase
import ru.profitsw2000.settingsscreen.presentation.utils.PrimitiveMathOperationSettingsMapper

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val primitiveTestSettingsItemsUseCase: PrimitiveTestSettingsItemsUseCase = mockk()
    private val primitiveTestSettingsUseCase: PrimitiveTestSettingsUseCase = mockk()
    private val settingsViewModel by lazy {
        SettingsViewModel(
            primitiveTestSettingsItemsUseCase,
            primitiveTestSettingsUseCase
        )
    }
    private val mapper = PrimitiveMathOperationSettingsMapper()

    private val settingsItems = PrimitiveTestSettingsItemsModel(
        taskTimesList = listOf(10, 20, 30),
        primitiveMathOperationType = PrimitiveMathOperationType.ADDITION,
        extraFieldNumbersList = listOf(10, 20),
        taskComplexityCodesList = listOf(1, 2, 3, 4),
        taskNumberList = listOf(5, 10)
    )
    private val settings = PrimitiveTestSettingsModel(
        testTasksNumber = 5,
        testComplexityLevel = 7,
        taskDurationTimeSeconds = 10
    )

    @Test
    fun `загрузка вариантов настроек и начальных настроек`() = runTest {
        val categoryCode = 1
        val operationType = mapper.getPrimitiveMathOperationTypeFromCode(categoryCode)
        val expectedTaskTimePairs = listOf(
            Pair(10, true),
            Pair(20, false),
            Pair(30, false),
        )
        val expectedExtraFieldNumbersPairs = listOf(
            Pair(10, false),
            Pair(20, true)
        )

        coEvery { primitiveTestSettingsItemsUseCase.getPrimitiveTestSettingsItems(operationType) } returns settingsItems
        coEvery { primitiveTestSettingsUseCase.getTestSettings(operationType) } returns settings

        settingsViewModel.loadSettings(categoryCode)

        val state = settingsViewModel.primitiveTestSettingsPairModel.value
        assertNotNull(state)
        assertEquals(PrimitiveMathOperationType.ADDITION, state?.primitiveMathOperationType)
        assertEquals(expectedTaskTimePairs, state?.testTaskTimePairList)
        assertEquals(expectedExtraFieldNumbersPairs, state?.extraFieldNumbersList)

        coVerify(exactly = 1) { primitiveTestSettingsItemsUseCase.getPrimitiveTestSettingsItems(operationType) }
        coVerify(exactly = 1) { primitiveTestSettingsUseCase.getTestSettings(operationType) }
    }

    @Test
    fun loadSettings() {
    }

    @Test
    fun updateTaskTime() {
    }

    @Test
    fun updateExtraFieldNumber() {
    }

    @Test
    fun updateTestComplexityCode() {
    }

    @Test
    fun updateTestTasksNumber() {
    }

    @Test
    fun writeTestSettings() {
    }

}