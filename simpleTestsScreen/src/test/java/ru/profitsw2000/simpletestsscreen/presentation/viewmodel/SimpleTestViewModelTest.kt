package ru.profitsw2000.simpletestsscreen.presentation.viewmodel

import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.currentTime
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

        simpleTestViewModel = SimpleTestViewModel(
            primitiveTestSettingsUseCase,
            primitiveTestTaskGeneratorUseCase,
            primitiveTestResultUseCase,
            primitiveTestResultDbWriteUseCase,
            testDispatcher
        )
    }

    @After
    fun tearDown() {
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
    fun `переход на следующее задание при истечении времени таймера`() = runTest {
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
    fun `переход на следующее задание при ответе пользователя`() = runTest {
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
    fun `завершение теста, расчёт результатов, запись в БД, сброс состояния`() = runTest {
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

}
/*

@Test
fun `при уничтожении ViewModel все фоновые задачи и таймер отменяются`() = runTest {
    val operation = PrimitiveMathOperationType.ADDITION
    val primitiveTestSettingsModel = PrimitiveTestSettingsModel(
        testTasksNumber = 10,
        taskDurationTimeSeconds = 60,
        testComplexityLevel = 1
    )
    val task = PrimitiveMathTaskModel(2, 2, operation)

    coEvery { primitiveTestSettingsUseCase.getTestSettings(operation) } returns primitiveTestSettingsModel
    coEvery { primitiveTestTaskGeneratorUseCase.getAdditionTask(any()) } returns task

    // 1. Запускаем тест и активируем таймер
    simpleTestViewModel.startTest(operation)
    runCurrent()

    // Проверяем, что таймер успел потикать (прошла 1 секунда)
    advanceTimeBy(1000)
    runCurrent()
    assertEquals(1, simpleTestViewModel.primitiveTestUiStateFlow.value.taskTime)

    // 2. Имитируем уничтожение ViewModel (выход из Фрагмента).
    // Так как метод onCleared() имеет статус protected, вызываем его через рефлексию:
    val onClearedMethod = simpleTestViewModel.javaClass.superclass.getDeclaredMethod("onCleared")
    onClearedMethod.isAccessible = true
    onClearedMethod.invoke(simpleTestViewModel)
    runCurrent()

    // 3. Проматываем время дальше (еще на 3 секунды вперед)
    advanceTimeBy(3000)
    runCurrent()

    // 4. Проверяем, что таймер ОСТАНОВИЛСЯ и время больше НЕ увеличивается.
    // Если бы ViewModel не уничтожилась, время стало бы равным 4 (1 + 3).
    // Но так как корутины отменились, оно жестко застыло на 1.
    assertEquals(1, simpleTestViewModel.primitiveTestUiStateFlow.value.taskTime)
}*/
/*

@Test
fun `выставление оценки 5 при 100 процентов правильных ответов`() = runTest {
    val operation = PrimitiveMathOperationType.ADDITION
    val primitiveTestSettingsModel = PrimitiveTestSettingsModel(
        testTasksNumber = 2, // 2 вопроса
        taskDurationTimeSeconds = 10,
        testComplexityLevel = 1
    )
    val task = PrimitiveMathTaskModel(2, 2, operation)

    coEvery { primitiveTestSettingsUseCase.getTestSettings(operation) } returns primitiveTestSettingsModel
    coEvery { primitiveTestTaskGeneratorUseCase.getAdditionTask(any()) } returns task

    // Симулируем, что ВСЕ ответы верны
    coEvery { primitiveTestResultUseCase.checkCalculationResult(any(), any()) } returns true

    // Создаем слот для перехвата итоговой модели, отправляемой в БД
    val resultSlot = io.mockk.slot<PrimitiveTestResultModel>()
    coJustRun { primitiveTestResultDbWriteUseCase.writeTestResultToDatabase(capture(resultSlot)) }

    // Запуск
    simpleTestViewModel.startTest(operation)
    runCurrent()

    // Даем два ответа
    simpleTestViewModel.appendAnswerResult(4)
    runCurrent()
    simpleTestViewModel.appendAnswerResult(4)
    runCurrent()

    // Проверяем, что в базу записалась отличная оценка (Замени на свою константу, например FIVE_ASSESSMENT)
    val capturedResult = resultSlot.captured
    assertEquals(2, capturedResult.correctAnswersNumber) // 2 из 2 верно
    // На месте цифры 5 должна быть твоя константа FIVE_ASSESSMENT:
    assertEquals(5, capturedResult.testAssessment)
}

@Test
fun `выставление плохой оценки при 0 процентов правильных ответов`() = runTest {
    val operation = PrimitiveMathOperationType.ADDITION
    val primitiveTestSettingsModel = PrimitiveTestSettingsModel(
        testTasksNumber = 2,
        taskDurationTimeSeconds = 10,
        testComplexityLevel = 1
    )
    val task = PrimitiveMathTaskModel(2, 2, operation)

    coEvery { primitiveTestSettingsUseCase.getTestSettings(operation) } returns primitiveTestSettingsModel
    coEvery { primitiveTestTaskGeneratorUseCase.getAdditionTask(any()) } returns task

    // Симулируем, что ВСЕ ответы НЕВЕРНЫ
    coEvery { primitiveTestResultUseCase.checkCalculationResult(any(), any()) } returns false

    val resultSlot = io.mockk.slot<PrimitiveTestResultModel>()
    coJustRun { primitiveTestResultDbWriteUseCase.writeTestResultToDatabase(capture(resultSlot)) }

    simpleTestViewModel.startTest(operation)
    runCurrent()

    simpleTestViewModel.appendAnswerResult(99) // заведомо неверный
    runCurrent()
    simpleTestViewModel.appendAnswerResult(99)
    runCurrent()

    val capturedResult = resultSlot.captured
    assertEquals(0, capturedResult.correctAnswersNumber) // 0 из 2 верно
    // На месте цифры 2 должна быть твоя константа TWO_ASSESSMENT:
    assertEquals(2, capturedResult.testAssessment)
}

@Test
fun `выставление средней оценки при части правильных ответов`() = runTest {
    val operation = PrimitiveMathOperationType.ADDITION
    val primitiveTestSettingsModel = PrimitiveTestSettingsModel(
        testTasksNumber = 4, // возьмем 4 вопроса для ровного деления
        taskDurationTimeSeconds = 10,
        testComplexityLevel = 1
    )
    val task = PrimitiveMathTaskModel(2, 2, operation)

    coEvery { primitiveTestSettingsUseCase.getTestSettings(operation) } returns primitiveTestSettingsModel
    coEvery { primitiveTestTaskGeneratorUseCase.getAdditionTask(any()) } returns task

    // Настраиваем поочередный ответ: 1-й верный, 2-й неверный, 3-й верный, 4-й неверный (итого 50%)
    coEvery { primitiveTestResultUseCase.checkCalculationResult(any(), any()) } returnsMany listOf(true, false, true, false)

    val resultSlot = io.mockk.slot<PrimitiveTestResultModel>()
    coJustRun { primitiveTestResultDbWriteUseCase.writeTestResultToDatabase(capture(resultSlot)) }

    simpleTestViewModel.startTest(operation)
    runCurrent()

    // Проходим все 4 вопроса
    repeat(4) {
        simpleTestViewModel.appendAnswerResult(4)
        runCurrent()
    }

    val capturedResult = resultSlot.captured
    assertEquals(2, capturedResult.correctAnswersNumber) // 2 из 4 верно (50%)

    // Здесь проверяем, какая оценка должна быть при 50% правильных ответов.
    // Подставь константу, под которую подпадает 0.5 (например, THREE_ASSESSMENT)
    assertEquals(3, capturedResult.testAssessment)
}
В третьем тесте используется классная фича MockK — returnsMany. При первом вызове checkCalculationResult мок вернет true, при втором — false, при третьем — снова true и так далее. Это позволяет легко симулировать реальное прохождение теста с ошибками, не усложняя код.
Попробуй подставить свои константные оценки вместо жестко прописанных 5, 2 и 3.
*/
/*

@Test
fun `тест корректно завершается при комбинации ручных ответов и пропуска по таймеру`() = runTest {
    val operation = PrimitiveMathOperationType.ADDITION
    val primitiveTestSettingsModel = PrimitiveTestSettingsModel(
        testTasksNumber = 3,          // Всего 3 вопроса в тесте
        taskDurationTimeSeconds = 5,  // 5 секунд на каждый вопрос
        testComplexityLevel = 1
    )
    val task = PrimitiveMathTaskModel(3, 3, operation)

    coEvery { primitiveTestSettingsUseCase.getTestSettings(operation) } returns primitiveTestSettingsModel
    coEvery { primitiveTestTaskGeneratorUseCase.getAdditionTask(any()) } returns task

    // Мокаем проверку: пусть ручные ответы будут верными, а пропуск (-1) — неверным
    coEvery { primitiveTestResultUseCase.checkCalculationResult(any(), 6) } returns true
    coEvery { primitiveTestResultUseCase.checkCalculationResult(any(), -1) } returns false

    val resultSlot = io.mockk.slot<PrimitiveTestResultModel>()
    coJustRun { primitiveTestResultDbWriteUseCase.writeTestResultToDatabase(capture(resultSlot)) }

    // Инициализация теста
    simpleTestViewModel.startTest(operation)
    runCurrent()

    // --- ВОПРОС 1 ---
    // Думаем 2 секунды и вручную отвечаем "6"
    advanceTimeBy(2000)
    simpleTestViewModel.appendAnswerResult(6)
    runCurrent()

    // --- ВОПРОС 2 ---
    // Ничего не нажимаем. Ждем 5 секунд, пока время полностью выйдет (таймаут)
    advanceTimeBy(5000)
    runCurrent()

    // --- ВОПРОС 3 (Последний) ---
    // Думаем 1 секунду и вручную отвечаем "6"
    advanceTimeBy(1000)
    simpleTestViewModel.appendAnswerResult(6)
    runCurrent()

    // --- ПРОВЕРКИ ---
    val finalResult = resultSlot.captured

    // 1. Проверяем, что в базу ушли правильные списки ответов и времени
    assertEquals(listOf(6, -1, 6), finalResult.testResultsList)   // [Ответ, Пропуск, Ответ]
    assertEquals(listOf(2, 5, 1), finalResult.testTasksTimeList)  // [2 сек, 5 сек, 1 сек]

    // 2. Проверяем расчет времени и правильных ответов
    assertEquals(2, finalResult.correctAnswersNumber) // 2 верных из 3
    assertEquals(8, finalResult.totalTimeSeconds)     // Суммарное время: 2 + 5 + 1 = 8 секунд
}
*/
