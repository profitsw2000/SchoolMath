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
import ru.profitsw2000.core.utils.GENERAL_TEST_HARD_COMPLEXITY_CODE
import ru.profitsw2000.core.utils.GENERAL_TEST_LIGHT_COMPLEXITY_CODE
import ru.profitsw2000.core.utils.GENERAL_TEST_MIDDLE_COMPLEXITY_CODE
import ru.profitsw2000.core.utils.GENERAL_TEST_NORMAL_COMPLEXITY_CODE
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
        val expectedComplexityCodePairs = listOf(
            Pair(GENERAL_TEST_LIGHT_COMPLEXITY_CODE, false),
            Pair(GENERAL_TEST_NORMAL_COMPLEXITY_CODE, false),
            Pair(GENERAL_TEST_MIDDLE_COMPLEXITY_CODE, true),
            Pair(GENERAL_TEST_HARD_COMPLEXITY_CODE, false)
        )
        val expectedTaskNumberPairs = listOf(
            Pair(5, true),
            Pair(10, false)
        )

        coEvery { primitiveTestSettingsItemsUseCase.getPrimitiveTestSettingsItems(operationType) } returns settingsItems
        coEvery { primitiveTestSettingsUseCase.getTestSettings(operationType) } returns settings

        settingsViewModel.loadSettings(categoryCode)

        val state = settingsViewModel.primitiveTestSettingsPairModel.value
        assertNotNull(state)
        assertEquals(PrimitiveMathOperationType.ADDITION, state?.primitiveMathOperationType)
        assertEquals(expectedTaskTimePairs, state?.testTaskTimePairList)
        assertEquals(expectedExtraFieldNumbersPairs, state?.extraFieldNumbersList)
        assertEquals(expectedComplexityCodePairs, state?.testTaskComplexityCodesPairList)
        assertEquals(expectedTaskNumberPairs, state?.testTasksNumberPairList)

        coVerify(exactly = 1) { primitiveTestSettingsItemsUseCase.getPrimitiveTestSettingsItems(operationType) }
        coVerify(exactly = 1) { primitiveTestSettingsUseCase.getTestSettings(operationType) }
    }

    @Test
    fun `обновление времени теста в настройках`() = runTest {
        setupAdditionInitialState()
        val newTime = 20
        settingsViewModel.updateTaskTime(newTime)

        val currentState = settingsViewModel.primitiveTestSettingsPairModel.value
        val expectedTaskTimePairs = listOf(
            Pair(10, false),
            Pair(20, true),
            Pair(30, false),
        )
        assertNotNull(currentState)
        assertEquals(expectedTaskTimePairs, currentState?.testTaskTimePairList)
    }

    @Test
    fun `обновление поля с дополнительным цифровым значением`() = runTest {
        setupAdditionInitialState()
        val newExtraFieldNumber = 10
        settingsViewModel.updateExtraFieldNumber(newExtraFieldNumber)

        val currentState = settingsViewModel.primitiveTestSettingsPairModel.value
        val expectedExtraFieldNumbersPairs = listOf(
            Pair(10, true),
            Pair(20, false)
        )
        assertNotNull(currentState)
        assertEquals(expectedExtraFieldNumbersPairs, currentState?.extraFieldNumbersList)
    }

    @Test
    fun `обновление поля настроек сложности теста`() = runTest {
        setupAdditionInitialState()
        settingsViewModel.updateTestComplexityCode(GENERAL_TEST_LIGHT_COMPLEXITY_CODE)

        val currentState = settingsViewModel.primitiveTestSettingsPairModel.value
        val expectedComplexityCodePairs = listOf(
            Pair(GENERAL_TEST_LIGHT_COMPLEXITY_CODE, true),
            Pair(GENERAL_TEST_NORMAL_COMPLEXITY_CODE, false),
            Pair(GENERAL_TEST_MIDDLE_COMPLEXITY_CODE, false),
            Pair(GENERAL_TEST_HARD_COMPLEXITY_CODE, false)
        )
        assertNotNull(currentState)
        assertEquals(expectedComplexityCodePairs, currentState?.testTaskComplexityCodesPairList)
    }

    @Test
    fun `обновление поля настройки количества заданий в тесте`() = runTest {
        setupAdditionInitialState()
        settingsViewModel.updateTestTasksNumber(10)

        val currentState = settingsViewModel.primitiveTestSettingsPairModel.value
        val expectedTaskNumberPairs = listOf(
            Pair(5, false),
            Pair(10, true)
        )
        assertNotNull(currentState)
        assertEquals(expectedTaskNumberPairs, currentState?.testTasksNumberPairList)

    }

    @Test
    fun `запись выбранных настроек для теста сложения`() = runTest {
        setupAdditionInitialState()
        settingsViewModel.updateTaskTime(20)
        settingsViewModel.updateExtraFieldNumber(10)
        settingsViewModel.updateTestComplexityCode(GENERAL_TEST_NORMAL_COMPLEXITY_CODE)
        settingsViewModel.updateTestTasksNumber(10)

        coEvery { primitiveTestSettingsUseCase.writeTestSettings(any(), any()) } returns Unit

        settingsViewModel.writeTestSettings()
        val expectedModel = PrimitiveTestSettingsModel(
            testTasksNumber = 10,
            testComplexityLevel = 2,
            taskDurationTimeSeconds = 20
        )

        coVerify(exactly = 1) {
            primitiveTestSettingsUseCase.writeTestSettings(
                PrimitiveMathOperationType.ADDITION,
                expectedModel
            )
        }
    }

    private fun setupAdditionInitialState() {
        val categoryCode = 1
        val primitiveMathOperationType = mapper.getPrimitiveMathOperationTypeFromCode(categoryCode)

        coEvery { primitiveTestSettingsItemsUseCase.getPrimitiveTestSettingsItems(primitiveMathOperationType) } returns settingsItems
        coEvery { primitiveTestSettingsUseCase.getTestSettings(primitiveMathOperationType) } returns settings

        settingsViewModel.loadSettings(categoryCode)
    }

}