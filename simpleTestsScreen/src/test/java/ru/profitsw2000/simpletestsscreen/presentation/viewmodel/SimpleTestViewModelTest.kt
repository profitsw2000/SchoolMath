package ru.profitsw2000.simpletestsscreen.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.profitsw2000.core.model.PrimitiveTestSettingsModel
import ru.profitsw2000.core.utils.PrimitiveMathOperationType
import ru.profitsw2000.simpletestsscreen.coroutines.MainDispatcherRule
import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveMathTaskModel
import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveTestResultModel
import ru.profitsw2000.simpletestsscreen.data.domain.usecase.PrimitiveTestResultDbWriteUseCase
import ru.profitsw2000.simpletestsscreen.data.domain.usecase.PrimitiveTestResultUseCase
import ru.profitsw2000.simpletestsscreen.data.domain.usecase.PrimitiveTestSettingsUseCase
import ru.profitsw2000.simpletestsscreen.data.domain.usecase.PrimitiveTestTaskGeneratorUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class SimpleTestViewModelTest {
    //private val testDispatcher = StandardTestDispatcher()
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val primitiveTestSettingsUseCase: PrimitiveTestSettingsUseCase = mockk()
    private val primitiveTestTaskGeneratorUseCase: PrimitiveTestTaskGeneratorUseCase = mockk()
    private val primitiveTestResultUseCase: PrimitiveTestResultUseCase = mockk()
    private val primitiveTestResultDbWriteUseCase: PrimitiveTestResultDbWriteUseCase = mockk()

    private lateinit var viewModelStore: ViewModelStore
    private lateinit var simpleTestViewModel: SimpleTestViewModel

    @Before
    fun setUp() {
        //Dispatchers.setMain(testDispatcher)

        viewModelStore = ViewModelStore()
        val factory = object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SimpleTestViewModel(
                    primitiveTestSettingsUseCase,
                    primitiveTestTaskGeneratorUseCase,
                    primitiveTestResultUseCase,
                    primitiveTestResultDbWriteUseCase,
                    mainDispatcherRule.testDispatcher,
                    TestScope(mainDispatcherRule.testDispatcher)
                ) as T
            }
        }
        val provider = ViewModelProvider(viewModelStore, factory)

        simpleTestViewModel = provider.get(SimpleTestViewModel::class.java)
    }

    @After
    fun tearDown() {
        //Dispatchers.resetMain()
        viewModelStore.clear()
    }

    @Test
    fun `загрузка настроек теста и первого примера`() = runTest(mainDispatcherRule.testDispatcher) {
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
    fun `таймер тикает каждую секунду`() = runTest(mainDispatcherRule.testDispatcher) {
        val operation = PrimitiveMathOperationType.ADDITION
        val primitiveTestSettingsModel = PrimitiveTestSettingsModel(
            testTasksNumber = 10,
            taskDurationTimeSeconds = 15,
            testComplexityLevel = 3
        )
        val task = PrimitiveMathTaskModel(4, 3, operation)

        coEvery { primitiveTestSettingsUseCase.getTestSettings(operation) } returns primitiveTestSettingsModel
        coEvery { primitiveTestTaskGeneratorUseCase.getAdditionTask(3) } returns task

        simpleTestViewModel.startTest(operation)
        runCurrent()

        advanceTimeBy(1000)
        runCurrent()
        assertEquals(1, simpleTestViewModel.primitiveTestUiStateFlow.value.taskTime)

        advanceTimeBy(2000)
        runCurrent()
        assertEquals(3, simpleTestViewModel.primitiveTestUiStateFlow.value.taskTime)
    }

    @Test
    fun `переход на следующее задание при истечении времени таймера`() = runTest(mainDispatcherRule.testDispatcher) {
        val operation = PrimitiveMathOperationType.ADDITION
        val primitiveTestSettingsModel = PrimitiveTestSettingsModel(
            testTasksNumber = 10,
            taskDurationTimeSeconds = 15,
            testComplexityLevel = 3
        )
        val task = PrimitiveMathTaskModel(6, 6, operation)

        coEvery { primitiveTestSettingsUseCase.getTestSettings(operation) } returns primitiveTestSettingsModel
        coEvery { primitiveTestTaskGeneratorUseCase.getAdditionTask(any()) } returns task

        simpleTestViewModel.startTest(operation)
        runCurrent()

        advanceTimeBy(15000)
        runCurrent()

        val state = simpleTestViewModel.primitiveTestUiStateFlow.value
        assertEquals(2, state.taskNumber)
        assertEquals(0, state.taskTime)

        assertEquals(-1, simpleTestViewModel.testResultsList.first())
    }

    @Test
    fun `переход на следующее задание при ответе пользователя`() = runTest(mainDispatcherRule.testDispatcher) {
        val operation = PrimitiveMathOperationType.ADDITION
        val primitiveTestSettingsModel = PrimitiveTestSettingsModel(
            testTasksNumber = 10,
            taskDurationTimeSeconds = 15,
            testComplexityLevel = 3
        )
        val task = PrimitiveMathTaskModel(6, 6, operation)

        coEvery { primitiveTestSettingsUseCase.getTestSettings(operation) } returns primitiveTestSettingsModel
        coEvery { primitiveTestTaskGeneratorUseCase.getAdditionTask(any()) } returns task

        simpleTestViewModel.startTest(operation)
        runCurrent()

        advanceTimeBy(6000)
        runCurrent()

        simpleTestViewModel.appendAnswerResult(12)
        runCurrent()

        assertEquals(12, simpleTestViewModel.testResultsList.first())
        assertEquals(6, simpleTestViewModel.taskTimeList.first())

        assertEquals(2, simpleTestViewModel.primitiveTestUiStateFlow.value.taskNumber)
        assertEquals(0, simpleTestViewModel.primitiveTestUiStateFlow.value.taskTime)

    }

    @Test
    fun `завершение теста, расчёт результатов, запись в БД, сброс состояния`() = runTest(mainDispatcherRule.testDispatcher) {
        val operation = PrimitiveMathOperationType.ADDITION
        val primitiveTestSettingsModel = PrimitiveTestSettingsModel(
            testTasksNumber = 5,
            taskDurationTimeSeconds = 20,
            testComplexityLevel = 3
        )
        val task = PrimitiveMathTaskModel(2, 6, operation)

        coEvery { primitiveTestSettingsUseCase.getTestSettings(operation) } returns primitiveTestSettingsModel
        coEvery { primitiveTestTaskGeneratorUseCase.getAdditionTask(any()) } returns task
        coEvery { primitiveTestResultUseCase.checkCalculationResult(any(), any()) } returns true
        coJustRun { primitiveTestResultDbWriteUseCase.writeTestResultToDatabase(any()) }

        simpleTestViewModel.startTest(operation)
        runCurrent()

        advanceTimeBy(3000)
        simpleTestViewModel.appendAnswerResult(8)
        runCurrent()

        advanceTimeBy(4000)
        simpleTestViewModel.appendAnswerResult(8)
        runCurrent()

        advanceTimeBy(5000)
        simpleTestViewModel.appendAnswerResult(8)
        runCurrent()

        advanceTimeBy(3000)
        simpleTestViewModel.appendAnswerResult(8)
        runCurrent()

        advanceTimeBy(3000)
        simpleTestViewModel.appendAnswerResult(8)
        runCurrent()

        coVerify(exactly = 1) {
            primitiveTestResultDbWriteUseCase.writeTestResultToDatabase(any())
        }

        val finalState = simpleTestViewModel.primitiveTestUiStateFlow.value
        assertFalse(finalState.testIsRunning)
        assertEquals(1, finalState.taskNumber)
    }

    @Test
    fun `при уничтожении вью модели все задачи отменяются`() = runTest(mainDispatcherRule.testDispatcher) {
        val operation = PrimitiveMathOperationType.ADDITION
        val primitiveTestSettingsModel = PrimitiveTestSettingsModel(
            testTasksNumber = 10,
            taskDurationTimeSeconds = 20,
            testComplexityLevel = 1
        )
        val task = PrimitiveMathTaskModel(2, 2, operation)

        coEvery { primitiveTestSettingsUseCase.getTestSettings(operation) } returns primitiveTestSettingsModel
        coEvery { primitiveTestTaskGeneratorUseCase.getAdditionTask(any()) } returns task

        simpleTestViewModel.startTest(operation)
        runCurrent()

        advanceTimeBy(6000)
        runCurrent()
        assertEquals(6, simpleTestViewModel.primitiveTestUiStateFlow.value.taskTime)

        viewModelStore.clear()
        runCurrent()

        advanceTimeBy(2000)
        runCurrent()

        assertEquals(6, simpleTestViewModel.primitiveTestUiStateFlow.value.taskTime)
    }

    @Test
    fun `при уничтожении вью модели результат сохраняется в БД`() = runTest(mainDispatcherRule.testDispatcher) {
        val operation = PrimitiveMathOperationType.ADDITION
        val primitiveTestSettingsModel = PrimitiveTestSettingsModel(
            testTasksNumber = 10,
            taskDurationTimeSeconds = 10,
            testComplexityLevel = 1
        )
        val task = PrimitiveMathTaskModel(2, 2, operation)
        val expectedResult = PrimitiveTestResultModel(
            settingsModel = primitiveTestSettingsModel,
            correctAnswersNumber = 4,
            testAssessment = 2,
            totalTimeSeconds = 22,
            primitiveMathOperationType = PrimitiveMathOperationType.ADDITION,
            primitiveMathTaskModelList = listOf(task, task, task, task, task),
            testResultsList = listOf(4, 4, 4, 4, -1),
            testTasksTimeList = listOf(3,3,3,3,10)
        )

        coEvery { primitiveTestSettingsUseCase.getTestSettings(operation) } returns primitiveTestSettingsModel
        coEvery { primitiveTestTaskGeneratorUseCase.getAdditionTask(any()) } returns task
        coEvery { primitiveTestResultUseCase.checkCalculationResult(any(), 4) } returns true
        coEvery { primitiveTestResultUseCase.checkCalculationResult(any(), -1) } returns false

        val resultSlot = slot<PrimitiveTestResultModel>()

        coEvery { primitiveTestResultDbWriteUseCase.writeTestResultToDatabase(capture(resultSlot)) } returns Unit

        simpleTestViewModel.startTest(operation)
        runCurrent()

        advanceTimeBy(3100)
        simpleTestViewModel.appendAnswerResult(4)
        runCurrent()

        advanceTimeBy(3100)
        simpleTestViewModel.appendAnswerResult(4)
        runCurrent()

        advanceTimeBy(3100)
        simpleTestViewModel.appendAnswerResult(4)
        runCurrent()

        advanceTimeBy(3100)
        simpleTestViewModel.appendAnswerResult(4)
        runCurrent()

        advanceTimeBy(10100)
        runCurrent()
        assertEquals(0, simpleTestViewModel.primitiveTestUiStateFlow.value.taskTime)

        advanceTimeBy(1000)
        runCurrent()

        viewModelStore.clear()
        advanceUntilIdle()

        coVerify(exactly = 1) {
            primitiveTestResultDbWriteUseCase.writeTestResultToDatabase(any())
        }

        val capturedResult = resultSlot.captured

        assertEquals(expectedResult, capturedResult)
    }

    @Test
    fun `запись в БД результата проверки на тройку`() = runTest(mainDispatcherRule.testDispatcher) {
        val operation = PrimitiveMathOperationType.ADDITION
        val primitiveTestSettingsModel = PrimitiveTestSettingsModel(
            testTasksNumber = 10,
            taskDurationTimeSeconds = 10,
            testComplexityLevel = 1
        )
        val task = PrimitiveMathTaskModel(2, 2, operation)
        val expectedResult = PrimitiveTestResultModel(
            settingsModel = primitiveTestSettingsModel,
            correctAnswersNumber = 8,
            testAssessment = 3,
            totalTimeSeconds = 37,
            primitiveMathOperationType = PrimitiveMathOperationType.ADDITION,
            primitiveMathTaskModelList = listOf(task, task, task, task, task, task, task, task, task, task),
            testResultsList = listOf(4, 4, 4, 4, -1, 4, 4, 4, 4, 5),
            testTasksTimeList = listOf(3,3,3,3,10,3,3,3,3,3)
        )

        coEvery { primitiveTestSettingsUseCase.getTestSettings(operation) } returns primitiveTestSettingsModel
        coEvery { primitiveTestTaskGeneratorUseCase.getAdditionTask(any()) } returns task
        coEvery { primitiveTestResultUseCase.checkCalculationResult(any(), 4) } returns true
        coEvery { primitiveTestResultUseCase.checkCalculationResult(any(), 5) } returns false
        coEvery { primitiveTestResultUseCase.checkCalculationResult(any(), -1) } returns false

        val resultSlot = slot<PrimitiveTestResultModel>()

        coEvery { primitiveTestResultDbWriteUseCase.writeTestResultToDatabase(capture(resultSlot)) } returns Unit

        simpleTestViewModel.startTest(operation)
        runCurrent()

        advanceTimeBy(3100)
        simpleTestViewModel.appendAnswerResult(4)
        runCurrent()

        advanceTimeBy(3100)
        simpleTestViewModel.appendAnswerResult(4)
        runCurrent()

        advanceTimeBy(3100)
        simpleTestViewModel.appendAnswerResult(4)
        runCurrent()

        advanceTimeBy(3100)
        simpleTestViewModel.appendAnswerResult(4)
        runCurrent()

        advanceTimeBy(10100)
        runCurrent()
        //assertEquals(0, simpleTestViewModel.primitiveTestUiStateFlow.value.taskTime)

        advanceTimeBy(3100)
        simpleTestViewModel.appendAnswerResult(4)
        runCurrent()

        advanceTimeBy(3100)
        simpleTestViewModel.appendAnswerResult(4)
        runCurrent()

        advanceTimeBy(3100)
        simpleTestViewModel.appendAnswerResult(4)
        runCurrent()

        advanceTimeBy(3100)
        simpleTestViewModel.appendAnswerResult(4)
        runCurrent()

        advanceTimeBy(3100)
        simpleTestViewModel.appendAnswerResult(5)
        advanceUntilIdle()

        coVerify(exactly = 1) {
            primitiveTestResultDbWriteUseCase.writeTestResultToDatabase(any())
        }

        val capturedResult = resultSlot.captured

        assertEquals(expectedResult, capturedResult)
    }

    @Test
    fun `запись в БД результата проверки на четвёрку`() = runTest(mainDispatcherRule.testDispatcher) {
        val operation = PrimitiveMathOperationType.ADDITION
        val primitiveTestSettingsModel = PrimitiveTestSettingsModel(
            testTasksNumber = 10,
            taskDurationTimeSeconds = 10,
            testComplexityLevel = 1
        )
        val task = PrimitiveMathTaskModel(2, 2, operation)
        val expectedResult = PrimitiveTestResultModel(
            settingsModel = primitiveTestSettingsModel,
            correctAnswersNumber = 9,
            testAssessment = 4,
            totalTimeSeconds = 30,
            primitiveMathOperationType = PrimitiveMathOperationType.ADDITION,
            primitiveMathTaskModelList = listOf(task, task, task, task, task, task, task, task, task, task),
            testResultsList = listOf(4, 4, 4, 4, 4, 4, 4, 4, 4, 5),
            testTasksTimeList = listOf(3,3,3,3,3,3,3,3,3,3)
        )

        coEvery { primitiveTestSettingsUseCase.getTestSettings(operation) } returns primitiveTestSettingsModel
        coEvery { primitiveTestTaskGeneratorUseCase.getAdditionTask(any()) } returns task
        coEvery { primitiveTestResultUseCase.checkCalculationResult(any(), 4) } returns true
        coEvery { primitiveTestResultUseCase.checkCalculationResult(any(), 5) } returns false
        coEvery { primitiveTestResultUseCase.checkCalculationResult(any(), -1) } returns false

        val resultSlot = slot<PrimitiveTestResultModel>()

        coEvery { primitiveTestResultDbWriteUseCase.writeTestResultToDatabase(capture(resultSlot)) } returns Unit

        simpleTestViewModel.startTest(operation)
        runCurrent()

        advanceTimeBy(3100)
        simpleTestViewModel.appendAnswerResult(4)
        runCurrent()

        advanceTimeBy(3100)
        simpleTestViewModel.appendAnswerResult(4)
        runCurrent()

        advanceTimeBy(3100)
        simpleTestViewModel.appendAnswerResult(4)
        runCurrent()

        advanceTimeBy(3100)
        simpleTestViewModel.appendAnswerResult(4)
        runCurrent()

        advanceTimeBy(3100)
        simpleTestViewModel.appendAnswerResult(4)
        runCurrent()

        advanceTimeBy(3100)
        simpleTestViewModel.appendAnswerResult(4)
        runCurrent()

        advanceTimeBy(3100)
        simpleTestViewModel.appendAnswerResult(4)
        runCurrent()

        advanceTimeBy(3100)
        simpleTestViewModel.appendAnswerResult(4)
        runCurrent()

        advanceTimeBy(3100)
        simpleTestViewModel.appendAnswerResult(4)
        runCurrent()

        advanceTimeBy(3100)
        simpleTestViewModel.appendAnswerResult(5)
        advanceUntilIdle()

        coVerify(exactly = 1) {
            primitiveTestResultDbWriteUseCase.writeTestResultToDatabase(any())
        }

        val capturedResult = resultSlot.captured

        assertEquals(expectedResult, capturedResult)
    }

    @Test
    fun `запись в БД результата проверки на пятёрку`() = runTest(mainDispatcherRule.testDispatcher) {
        val operation = PrimitiveMathOperationType.ADDITION
        val primitiveTestSettingsModel = PrimitiveTestSettingsModel(
            testTasksNumber = 10,
            taskDurationTimeSeconds = 10,
            testComplexityLevel = 1
        )
        val task = PrimitiveMathTaskModel(2, 2, operation)
        val expectedResult = PrimitiveTestResultModel(
            settingsModel = primitiveTestSettingsModel,
            correctAnswersNumber = 10,
            testAssessment = 5,
            totalTimeSeconds = 34,
            primitiveMathOperationType = PrimitiveMathOperationType.ADDITION,
            primitiveMathTaskModelList = listOf(task, task, task, task, task, task, task, task, task, task),
            testResultsList = listOf(4, 4, 4, 4, 4, 4, 4, 4, 4, 4),
            testTasksTimeList = listOf(3,3,5,5,3,3,3,3,3,3)
        )

        coEvery { primitiveTestSettingsUseCase.getTestSettings(operation) } returns primitiveTestSettingsModel
        coEvery { primitiveTestTaskGeneratorUseCase.getAdditionTask(any()) } returns task
        coEvery { primitiveTestResultUseCase.checkCalculationResult(any(), 4) } returns true
        coEvery { primitiveTestResultUseCase.checkCalculationResult(any(), 5) } returns false
        coEvery { primitiveTestResultUseCase.checkCalculationResult(any(), -1) } returns false

        val resultSlot = slot<PrimitiveTestResultModel>()

        coEvery { primitiveTestResultDbWriteUseCase.writeTestResultToDatabase(capture(resultSlot)) } returns Unit

        simpleTestViewModel.startTest(operation)
        runCurrent()

        advanceTimeBy(3100)
        simpleTestViewModel.appendAnswerResult(4)
        runCurrent()

        advanceTimeBy(3100)
        simpleTestViewModel.appendAnswerResult(4)
        runCurrent()

        advanceTimeBy(5100)
        simpleTestViewModel.appendAnswerResult(4)
        runCurrent()

        advanceTimeBy(5100)
        simpleTestViewModel.appendAnswerResult(4)
        runCurrent()

        advanceTimeBy(3100)
        simpleTestViewModel.appendAnswerResult(4)
        runCurrent()

        advanceTimeBy(3100)
        simpleTestViewModel.appendAnswerResult(4)
        runCurrent()

        advanceTimeBy(3100)
        simpleTestViewModel.appendAnswerResult(4)
        runCurrent()

        advanceTimeBy(3100)
        simpleTestViewModel.appendAnswerResult(4)
        runCurrent()

        advanceTimeBy(3100)
        simpleTestViewModel.appendAnswerResult(4)
        runCurrent()

        advanceTimeBy(3100)
        simpleTestViewModel.appendAnswerResult(4)
        advanceUntilIdle()

        coVerify(exactly = 1) {
            primitiveTestResultDbWriteUseCase.writeTestResultToDatabase(any())
        }

        val capturedResult = resultSlot.captured

        assertEquals(expectedResult, capturedResult)
    }

}