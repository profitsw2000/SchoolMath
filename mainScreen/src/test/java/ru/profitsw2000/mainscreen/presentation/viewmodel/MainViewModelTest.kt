package ru.profitsw2000.mainscreen.presentation.viewmodel

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.profitsw2000.core.domain.usecase.PrimitiveTestSettingsUseCase
import ru.profitsw2000.core.model.PrimitiveTestResultModel
import ru.profitsw2000.core.model.PrimitiveTestSettingsModel
import ru.profitsw2000.core.utils.PrimitiveMathOperationType
import ru.profitsw2000.mainscreen.coroutines.MainDispatcherRule
import ru.profitsw2000.mainscreen.data.domain.usecase.TestCategoryUseCase
import ru.profitsw2000.mainscreen.data.domain.usecase.TestHistoryUseCase
import ru.profitsw2000.mainscreen.presentation.uiState.CategoryHistoryUiState
import ru.profitsw2000.mainscreen.presentation.uiState.CategorySettingsUiState
import ru.profitsw2000.mainscreen.presentation.uiState.MainUiState

class MainViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testCategoryUseCase: TestCategoryUseCase = mockk()
    private val testHistoryUseCase: TestHistoryUseCase = mockk()
    private val primitiveTestSettingsUseCase: PrimitiveTestSettingsUseCase = mockk()

    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        mainViewModel = MainViewModel(
            testCategoryUseCase,
            testHistoryUseCase,
            primitiveTestSettingsUseCase
        )
    }

    @Test
    fun `успешная загрузка списка категорий`() = runTest() {
        val expectedCategories = listOf(1, 2, 3)
        coEvery {
            testCategoryUseCase.getTestCategoriesCodesList()
        } returns expectedCategories

        mainViewModel.loadCategories()
        assertEquals(MainUiState.Loading, mainViewModel.categoryUiState.value)
        advanceUntilIdle()

        assertEquals(MainUiState.Success(expectedCategories), mainViewModel.categoryUiState.value)
    }

    @Test
    fun `успешная загрузка настройки для выбранной категории`() = runTest {
        val settings = PrimitiveTestSettingsModel(
            testTasksNumber = 10,
            testComplexityLevel = 2,
            taskDurationTimeSeconds = 10
        )
        mainViewModel.setCategory(1)

        coEvery {
            primitiveTestSettingsUseCase.getTestSettings(PrimitiveMathOperationType.ADDITION)
        } returns settings

        mainViewModel.loadCategorySettings()
        assertEquals(CategorySettingsUiState.Loading, mainViewModel.settingsUiState.value)
        advanceUntilIdle()

        assertEquals(CategorySettingsUiState.Success(settings), mainViewModel.settingsUiState.value)
    }

    @Test
    fun `ошибка загрузки настроек выбранного теста`() = runTest {
        mainViewModel.setCategory(2)
        coEvery {
            primitiveTestSettingsUseCase.getTestSettings(PrimitiveMathOperationType.SUBTRACTION)
        } throws RuntimeException("Database error")

        // When
        mainViewModel.loadCategorySettings()
        assertEquals(CategorySettingsUiState.Loading, mainViewModel.settingsUiState.value)
        advanceUntilIdle()

        // Then
        assertEquals(CategorySettingsUiState.Error, mainViewModel.settingsUiState.value)
    }

    @Test
    fun `загрузка истории по умолчанию(сложение), если категория не была задана`() = runTest {
        val mockHistory = listOf<PrimitiveTestResultModel>()
        coEvery {
            testHistoryUseCase.getLastTestsResults(
                PrimitiveMathOperationType.ADDITION,
                5
            )
        } returns mockHistory

        mainViewModel.loadCategoryHistory()
        assertEquals(CategoryHistoryUiState.Loading, mainViewModel.historyUiState.value)
        advanceUntilIdle()

        assertEquals(CategoryHistoryUiState.Success(mockHistory), mainViewModel.historyUiState.value)
    }

    @Test
    fun `загрузка истории по умолчанию(сложение) заданной категории`() = runTest {
        val mockHistory = listOf<PrimitiveTestResultModel>()
        mainViewModel.setCategory(3)

        coEvery {
            testHistoryUseCase.getLastTestsResults(
                PrimitiveMathOperationType.MULTIPLICATION,
                5
            )
        } returns mockHistory

        mainViewModel.loadCategoryHistory()
        assertEquals(CategoryHistoryUiState.Loading, mainViewModel.historyUiState.value)
        advanceUntilIdle()

        assertEquals(CategoryHistoryUiState.Success(mockHistory), mainViewModel.historyUiState.value)
    }

    @Test
    fun `ошибка загрузки истории тестов`() = runTest {
        mainViewModel.setCategory(4)
        coEvery {
            testHistoryUseCase.getLastTestsResults(
                PrimitiveMathOperationType.DIVISION,
                6
            )
        } throws Exception("Database error")

        mainViewModel.loadCategoryHistory()
        assertEquals(CategoryHistoryUiState.Loading, mainViewModel.historyUiState.value)
        advanceUntilIdle()

        assertEquals(CategoryHistoryUiState.Error, mainViewModel.historyUiState.value)
    }

}