package ru.profitsw2000.simpletestsscreen.presentation.viewmodel

import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ru.profitsw2000.core.model.PrimitiveTestSettingsModel
import ru.profitsw2000.core.utils.PrimitiveMathOperationType
import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveMathTaskModel
import ru.profitsw2000.simpletestsscreen.data.domain.usecase.PrimitiveTestResultDbWriteUseCase
import ru.profitsw2000.simpletestsscreen.data.domain.usecase.PrimitiveTestResultUseCase
import ru.profitsw2000.simpletestsscreen.data.domain.usecase.PrimitiveTestSettingsUseCase
import ru.profitsw2000.simpletestsscreen.data.domain.usecase.PrimitiveTestTaskGeneratorUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class SimpleTestViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private val primitiveTestSettingsUseCase: PrimitiveTestSettingsUseCase = mockk()
    private val primitiveTestTaskGeneratorUseCase: PrimitiveTestTaskGeneratorUseCase = mockk()
    private val primitiveTestResultUseCase: PrimitiveTestResultUseCase = mockk()
    private val primitiveTestResultDbWriteUseCase: PrimitiveTestResultDbWriteUseCase = mockk()

    private lateinit var simpleTestViewModel: SimpleTestViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        mockkStatic(Dispatchers::class)
        io.mockk.every { Dispatchers.Default } returns testDispatcher
        io.mockk.every { Dispatchers.IO } returns testDispatcher

        simpleTestViewModel = SimpleTestViewModel(
            primitiveTestSettingsUseCase,
            primitiveTestTaskGeneratorUseCase,
            primitiveTestResultUseCase,
            primitiveTestResultDbWriteUseCase
        )
    }

    @After
    fun tearDown() {
        unmockkStatic(Dispatchers::class)
        Dispatchers.resetMain()
    }

    @Test
    fun `загрузка настроек теста и первого примера`() = runTest {
        val operation = PrimitiveMathOperationType.ADDITION
        val primitiveTestSettingsModel = PrimitiveTestSettingsModel(
            testTasksNumber = 11,
            taskDurationTimeSeconds = 20,
            testComplexityLevel = 3
        )
        val task = PrimitiveMathTaskModel(4, 3, operation)

        coEvery { primitiveTestSettingsUseCase.getTestSettings(operation) } returns primitiveTestSettingsModel
        coEvery { primitiveTestTaskGeneratorUseCase.getAdditionTask(3) } returns task

        simpleTestViewModel.startTest(operation)
        //testScheduler.advanceUntilIdle()
        runCurrent()

        val state = simpleTestViewModel.primitiveTestUiStateFlow.value
        assertTrue(state.testIsRunning)
        assertEquals(4, state.firstOperand)
        assertEquals(3, state.secondOperand)
        assertEquals(20, state.totalTaskTime)
        assertEquals(11, state.totalTaskNumber)
        assertEquals(1, state.taskNumber)
        assertEquals(0, state.taskTime)

    }

    @Test
    fun `таймер тикает каждую секунду`() = runTest {
        val operation = PrimitiveMathOperationType.ADDITION
        val primitiveTestSettingsModel = PrimitiveTestSettingsModel(
            testTasksNumber = 10,
            taskDurationTimeSeconds = 15,
            testComplexityLevel = 2
        )
        val task = PrimitiveMathTaskModel(4, 3, operation)

        coEvery { primitiveTestSettingsUseCase.getTestSettings(operation) } returns primitiveTestSettingsModel
        coEvery { primitiveTestTaskGeneratorUseCase.getAdditionTask(3) } returns task

        simpleTestViewModel.startTest(operation)
        //testScheduler.advanceUntilIdle()
        advanceTimeBy(1)

        advanceTimeBy(1000)
        runCurrent()
        assertEquals(1, simpleTestViewModel.primitiveTestUiStateFlow.value.taskTime)

        advanceTimeBy(2000)
        runCurrent()
        assertEquals(3, simpleTestViewModel.primitiveTestUiStateFlow.value.taskTime)

    }

}