package ru.profitsw2000.simpletestsscreen.data.data.repository

import android.content.Context
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

@OptIn(ExperimentalCoroutinesApi::class)
class PrimitiveTestSettingsRepositoryImplTest {

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher + Job())

    private val context: Context = mockk()

    @Before
    fun setUp() {
        TODO("Not yet implemented")
    }

    @After
    fun tearDown() {
        TODO("Not yet implemented")
    }

    @Test
    fun getAdditionTestSettings() {
    }

    @Test
    fun writeAdditionTestSettings() {
    }

}