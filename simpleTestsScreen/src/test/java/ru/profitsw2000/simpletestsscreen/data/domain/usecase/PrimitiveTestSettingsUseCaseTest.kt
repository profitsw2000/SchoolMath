package ru.profitsw2000.simpletestsscreen.data.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import ru.profitsw2000.core.model.PrimitiveTestSettingsModel
import ru.profitsw2000.core.utils.PrimitiveMathOperationType
import ru.profitsw2000.simpletestsscreen.data.domain.repository.PrimitiveTestSettingsRepository

class PrimitiveTestSettingsUseCaseTest {

    private val repository: PrimitiveTestSettingsRepository = mockk()
    private val primitiveTestSettingsUseCase = PrimitiveTestSettingsUseCase(repository)

    @Test
    fun `получение настроек для примеров сложения`() = runTest {
        val expectedSettings = mockk<PrimitiveTestSettingsModel>()
        coEvery { repository.getAdditionTestSettings() } returns expectedSettings

        val result = primitiveTestSettingsUseCase.getTestSettings(PrimitiveMathOperationType.ADDITION)
        assertEquals(expectedSettings, result)
        coVerify(exactly = 1) {repository.getAdditionTestSettings()}
    }

    @Test
    fun `получение настроек для примеров вычитания`() = runTest {
        val expectedSettings = mockk<PrimitiveTestSettingsModel>()
        coEvery { repository.getSubtractionTestSettings() } returns expectedSettings

        val result = primitiveTestSettingsUseCase.getTestSettings(PrimitiveMathOperationType.SUBTRACTION)
        assertEquals(expectedSettings, result)
        coVerify(exactly = 1) {repository.getSubtractionTestSettings()}
    }

    @Test
    fun `получение настроек для примеров умножения`() = runTest {
        val expectedSettings = mockk<PrimitiveTestSettingsModel>()
        coEvery { repository.getMultiplicationTestSettings() } returns expectedSettings

        val result = primitiveTestSettingsUseCase.getTestSettings(PrimitiveMathOperationType.MULTIPLICATION)
        assertEquals(expectedSettings, result)
        coVerify(exactly = 1) {repository.getMultiplicationTestSettings()}
    }

    @Test
    fun `получение настроек для примеров деления`() = runTest {
        val expectedSettings = mockk<PrimitiveTestSettingsModel>()
        coEvery { repository.getDivisionTestSettings() } returns expectedSettings

        val result = primitiveTestSettingsUseCase.getTestSettings(PrimitiveMathOperationType.DIVISION)
        assertEquals(expectedSettings, result)
        coVerify(exactly = 1) {repository.getDivisionTestSettings()}
    }

}