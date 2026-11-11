package ru.profitsw2000.mainscreen.data.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ru.profitsw2000.mainscreen.data.data.repository.TestCategoryRepositoryImpl
import ru.profitsw2000.mainscreen.data.domain.repository.TestCategoryRepository

class TestCategoryUseCaseTest {
    private val testCategoryRepository = mockk<TestCategoryRepositoryImpl>()
    private lateinit var testCategoryUseCase: TestCategoryUseCase

    @Before
    fun setUp() {
        testCategoryUseCase = TestCategoryUseCase(testCategoryRepository)
    }

    @Test
    fun `успешное получение данных из репозитория`() = runTest {
        val expectedResult = listOf(1, 2, 3, 4)

        coEvery {
            testCategoryRepository.getTestCategoriesCodesList()
        } returns expectedResult
        val actualResult = testCategoryUseCase.getTestCategoriesCodesList()

        assertEquals(expectedResult, actualResult)
        coVerify (exactly = 1) {
            testCategoryRepository.getTestCategoriesCodesList()
        }
    }

}