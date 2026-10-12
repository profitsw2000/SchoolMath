package ru.profitsw2000.core.room.dao

import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ru.profitsw2000.core.model.PrimitiveTestSettingsModel
import ru.profitsw2000.core.room.database.AppDatabase
import ru.profitsw2000.core.room.entity.PrimitiveTestResultEntity
import ru.profitsw2000.core.utils.PrimitiveMathOperationType
import java.io.IOException
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class PrimitiveTestResultDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: PrimitiveTestResultDao

    private fun createEntity(
        id: Long,
        operationType: PrimitiveMathOperationType,
        tasksNumber: Int,
        complexity: Int,
        duration: Int
    ) = PrimitiveTestResultEntity(
        id = id,
        settings = PrimitiveTestSettingsModel(
            testTasksNumber = tasksNumber,
            testComplexityLevel = complexity,
            taskDurationTimeSeconds = duration
        ),
        correctAnswersNumber = 4,
        testAssessment = 4,
        totalTimeSeconds = 87,
        primitiveMathOperationType = operationType,
        firstOperandsList = listOf(1,2,3,5,7),
        secondOperandList = listOf(3,5,2,4,7),
        testResultList = listOf(4,8,5,9,14),
        testTasksTimeList = listOf(2,6,8,9,12)
    )

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        dao = db.primitiveTestResultDao
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    @Test
    fun filter_by_all_parameters_together() = runTest {
        val target = createEntity(1,PrimitiveMathOperationType.ADDITION,10,1,20)
        val wrong1 = createEntity(2,PrimitiveMathOperationType.SUBTRACTION,10,1,20)
        val wrong2 = createEntity(3,PrimitiveMathOperationType.ADDITION,20,1,20)

        dao.insert(target)
        dao.insert(wrong1)
        dao.insert(wrong2)

        val pagingSource = dao.getPagedTestResults(
            primitiveMathOperationType = PrimitiveMathOperationType.ADDITION,
            testTasksNumber = 10,
            testComplexityLevel = 1,
            testDurationTime = 20
        )
        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )
        assertTrue(loadResult is PagingSource.LoadResult.Page)
        val page = loadResult as PagingSource.LoadResult.Page

        assertEquals(1, page.data.size)
        assertEquals(target, page.data.first())
    }

    @Test
    fun test_filter_by_single_param() = runTest {
        val target1 = createEntity(1,PrimitiveMathOperationType.ADDITION,10,2,20)
        val target2 = createEntity(2,PrimitiveMathOperationType.SUBTRACTION,10,3,20)
        val wrong2 = createEntity(3,PrimitiveMathOperationType.ADDITION,20,2,20)

        dao.insert(target1)
        dao.insert(target2)
        dao.insert(wrong2)

        val pagingSource = dao.getPagedTestResults(testTasksNumber = 10)
        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )
        assertTrue(loadResult is PagingSource.LoadResult.Page)
        val page = loadResult as PagingSource.LoadResult.Page

        assertEquals(2, page.data.size)
        assertEquals(target2, page.data[0])
        assertEquals(target1, page.data[1])
    }

    @Test
    fun all_filters_are_null() = runTest {
        val target1 = createEntity(1,PrimitiveMathOperationType.ADDITION,10,2,20)
        val target2 = createEntity(2,PrimitiveMathOperationType.SUBTRACTION,10,3,20)
        val target3 = createEntity(3,PrimitiveMathOperationType.ADDITION,20,2,20)

        dao.insert(target1)
        dao.insert(target2)
        dao.insert(target3)

        val pagingSource = dao.getPagedTestResults()
        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )
        assertTrue(loadResult is PagingSource.LoadResult.Page)
        val page = loadResult as PagingSource.LoadResult.Page

        assertEquals(3, page.data.size)
        assertEquals(target3, page.data[0])
        assertEquals(target2, page.data[1])
        assertEquals(target1, page.data[2])
    }

}