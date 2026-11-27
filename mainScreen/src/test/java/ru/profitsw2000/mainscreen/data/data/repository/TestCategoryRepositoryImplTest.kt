package ru.profitsw2000.mainscreen.data.data.repository

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ru.profitsw2000.mainscreen.data.domain.repository.TestCategoryRepository

class TestCategoryRepositoryImplTest {

    private lateinit var testCategoryRepository: TestCategoryRepository

    @Before
    fun setUp() {
        testCategoryRepository = TestCategoryRepositoryImpl()
    }

    @Test
    fun `репо возвращает список с кодами типов тестов`() {
        val expectedList = listOf(1, 2, 3, 4)
        val categoriesCodeList = testCategoryRepository.getTestCategoriesCodesList()

        assertEquals(expectedList, categoriesCodeList)

    }

}