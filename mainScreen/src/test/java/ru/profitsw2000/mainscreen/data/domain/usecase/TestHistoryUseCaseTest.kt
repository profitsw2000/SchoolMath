package ru.profitsw2000.mainscreen.data.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ru.profitsw2000.core.model.PrimitiveTestResultModel
import ru.profitsw2000.core.utils.PrimitiveMathOperationType
import ru.profitsw2000.mainscreen.data.domain.repository.TestHistoryRepository

class TestHistoryUseCaseTest {

    private val testHistoryRepository: TestHistoryRepository = mockk()
    private lateinit var testHistoryUseCase: TestHistoryUseCase

    @Before
    fun setUp() {
        testHistoryUseCase = TestHistoryUseCase(testHistoryRepository)
    }

    @Test
    fun `функция получения истории прохождения последних тестов возвращает данные из репо`() = runTest {
        val operationType = PrimitiveMathOperationType.ADDITION
        val limitNumber = 5

        val expectedResult = listOf(mockk<PrimitiveTestResultModel>())

        coEvery {
            testHistoryRepository.getLastTestsResults(operationType, limitNumber)
        } returns expectedResult

        val actualResult = testHistoryUseCase.getLastTestsResults(operationType, limitNumber)

        assertEquals(expectedResult, actualResult)

        coVerify(exactly = 1) {
            testHistoryRepository.getLastTestsResults(operationType, limitNumber)
        }
    }

    @Test
    fun `исключение проталкивается дальше, если запрос в БД не успешен`() = runTest {
        val operationType = PrimitiveMathOperationType.ADDITION
        val limitNumber = 5

        coEvery {
            testHistoryRepository.getLastTestsResults(operationType, limitNumber)
        } throws RuntimeException("Repository error")

        try {
            testHistoryUseCase.getLastTestsResults(operationType, limitNumber)
            fail("Expected RuntimeException was not thrown!!!")
        } catch (exc: Exception) {
            assertEquals("Repository error", exc.message)
        }

        coVerify(exactly = 1) {
            testHistoryRepository.getLastTestsResults(operationType, limitNumber)
        }
    }

}