package ru.profitsw2000.core.room.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.profitsw2000.core.model.PrimitiveTestSettingsModel
import ru.profitsw2000.core.room.database.AppDatabase
import ru.profitsw2000.core.room.entity.PrimitiveTestResultEntity
import ru.profitsw2000.core.utils.PrimitiveMathOperationType
import java.io.IOException
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class PrimitiveTestResultDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: PrimitiveTestResultDao

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
    fun insert_validEntity_savesToDatabaseSuccessfully() = runTest {
        val mockSettings = PrimitiveTestSettingsModel(
            testTasksNumber = 5,
            testComplexityLevel = 1,
            taskDurationTimeSeconds = 20
        )
        val primitiveTestResultEntity = PrimitiveTestResultEntity(
            id = 10L,
            settings = mockSettings,
            correctAnswersNumber = 4,
            testAssessment = 4,
            totalTimeSeconds = 87,
            primitiveMathOperationType = PrimitiveMathOperationType.ADDITION,
            firstOperandsList = listOf(1,2,3,5,7),
            secondOperandList = listOf(3,5,2,4,7),
            testResultList = listOf(4,8,5,9,14),
            testTasksTimeList = listOf(2,6,8,9,12)
        )

        dao.insert(primitiveTestResultEntity)
    }

}
/*

@RunWith(AndroidJUnit4::class)
class PrimitiveTestResultDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: PrimitiveTestResultDao

    @Before
    fun createDb() {
        // Создаем базу в оперативной памяти (In-Memory)
        // Обязательно укажите здесь ваши TypeConverter, если они не подключены на уровне самого AppDatabase
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()

        dao = db.primitiveTestResultDao() // Замените на реальное имя метода в вашем AppDatabase
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insert_validEntity_savesToDatabaseSuccessfully() = runTest {
        // Arrange: Создаем объект настроек (замените на ваш конструктор)
        val mockSettings = PrimitiveTestSettingsModel(
            // добавьте сюда поля вашей модели PrimitiveTestSettingsModel
        )

        // Arrange: Создаем тестируемую сущность
        val entity = PrimitiveTestResultEntity(
            id = 1,
            settings = mockSettings,
            correctAnswersNumber = 8,
            testAssessment = 5,
            totalTimeSeconds = 120,
            primitiveMathOperationType = PrimitiveMathOperationType.ADDITION,
            firstOperandsList = listOf(5, 10, 15),
            secondOperandList = listOf(2, 4, 6),
            testResultList = listOf(7, 14, 21),
            testTasksTimeList = listOf(10, 12, 8)
        )

        // Act: Сохраняем в базу данных
        dao.insert(entity)

        // Assert:
        // Так как метода get/read в DAO пока нет, мы можем временно проверить
        // успешность вставки через прямой SQL-запрос к БД, либо пока просто убедиться,
        // что метод выполнился без исключений (Room сам проверит работу конвертеров при вставке)

        // Как только добавите метод получения (например, getById(1)), раскомментируйте это:
        */
/*
        val result = dao.getById(1)
        assertNotNull(result)
        assertEquals(8, result.correctAnswersNumber)
        assertEquals(PrimitiveMathOperationType.ADDITION, result.primitiveMathOperationType)
        assertEquals(listOf(5, 10, 15), result.firstOperandsList)
        *//*

    }
}
*/
