package ru.profitsw2000.mainscreen.data.data.repository

import ru.profitsw2000.mainscreen.data.domain.repository.TestCategoryRepository
import ru.profitsw2000.core.utils.ADDITION_CATEGORY_CODE
import ru.profitsw2000.core.utils.DIVISION_CATEGORY_CODE
import ru.profitsw2000.core.utils.MULTIPLICATION_CATEGORY_CODE
import ru.profitsw2000.core.utils.SUBTRACTION_CATEGORY_CODE

class TestCategoryRepositoryImpl(): TestCategoryRepository {
    override fun getTestCategoriesCodesList(): List<Int> {
        return listOf(
            ADDITION_CATEGORY_CODE,
            SUBTRACTION_CATEGORY_CODE,
            MULTIPLICATION_CATEGORY_CODE,
            DIVISION_CATEGORY_CODE
        )
    }
}