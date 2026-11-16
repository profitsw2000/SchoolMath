package ru.profitsw2000.mainscreen.data.data.repository

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertThrows
import ru.profitsw2000.core.mappers.PrimitiveTestResultMapper
import ru.profitsw2000.core.model.PrimitiveTestResultModel
import ru.profitsw2000.core.room.dao.PrimitiveTestResultDao
import ru.profitsw2000.core.room.entity.PrimitiveTestResultEntity
import ru.profitsw2000.core.utils.PrimitiveMathOperationType
import ru.profitsw2000.mainscreen.coroutines.MainDispatcherRule
import ru.profitsw2000.mainscreen.data.domain.repository.TestHistoryRepository

@OptIn(ExperimentalCoroutinesApi::class)
class TestHistoryRepositoryImplTest {

    private val primitiveTestResultDao: PrimitiveTestResultDao = mockk()
    private val mapper: PrimitiveTestResultMapper = mockk()
    private lateinit var testHistoryRepository: TestHistoryRepository

    @Before
    fun setUp() {
        testHistoryRepository = TestHistoryRepositoryImpl(
            primitiveTestResultDao,
            mapper
        )
    }

    @Test
    fun `возврат истории результатов теста при удачном чтении из БД в нужном формате`() = runTest {
        val operationType = PrimitiveMathOperationType.ADDITION
        val limitNumber = 5

        val resultEntities = listOf(mockk<PrimitiveTestResultEntity>())
        val expectedModels = listOf(mockk<PrimitiveTestResultModel>())

        coEvery {
            primitiveTestResultDao.getLastDatabaseEntriesWithGivenOperationType(
                operationType,
                limitNumber
            )
        } returns resultEntities
        coEvery {
            mapper.map(resultEntities)
        } returns expectedModels

        val result = testHistoryRepository.getLastTestsResults(
            operationType,
            limitNumber
        )

        assertEquals(expectedModels, result)

        coVerify(exactly = 1) {
            primitiveTestResultDao.getLastDatabaseEntriesWithGivenOperationType(operationType, limitNumber)
        }
        coVerify(exactly = 1) {
            mapper.map(resultEntities)
        }
    }

    @Test
    fun `выброс исключения если произошла ошибка чтения из БД`() = runTest {
        val operationType = PrimitiveMathOperationType.ADDITION
        val limitNumber = 5

        coEvery {
            primitiveTestResultDao.getLastDatabaseEntriesWithGivenOperationType(
                operationType,
                limitNumber
            )
        } throws RuntimeException("Database read error")

        try {
            testHistoryRepository.getLastTestsResults(
                operationType,
                limitNumber
            )
            org.junit.Assert.fail("Expected RuntimeException has not been thrown")
        } catch (exc: Exception) {
            org.junit.Assert.assertEquals("Database read error", exc.message)
        }
        coVerify(exactly = 0) { mapper.map(any<List<PrimitiveTestResultEntity>>()) }
    }

}