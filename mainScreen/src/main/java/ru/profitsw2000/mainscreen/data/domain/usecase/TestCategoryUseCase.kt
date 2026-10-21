package ru.profitsw2000.mainscreen.data.domain.usecase

import ru.profitsw2000.mainscreen.data.domain.repository.TestCategoryRepository

class TestCategoryUseCase(
    private val testCategoryRepository: TestCategoryRepository
) {

    suspend fun getTestCategoriesCodesList(): List<Int> {
        return testCategoryRepository.getTestCategoriesCodesList()
    }

}