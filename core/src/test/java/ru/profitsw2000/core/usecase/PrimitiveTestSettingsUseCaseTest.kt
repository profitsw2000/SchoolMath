package ru.profitsw2000.core.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import ru.profitsw2000.core.domain.repository.PrimitiveTestSettingsRepository
import ru.profitsw2000.core.domain.usecase.PrimitiveTestSettingsUseCase
import ru.profitsw2000.core.model.PrimitiveTestSettingsModel
import ru.profitsw2000.core.utils.PrimitiveMathOperationType

@OptIn(ExperimentalCoroutinesApi::class)
class PrimitiveTestSettingsUseCaseTest {

    private val primitiveTestSettingsRepository: PrimitiveTestSettingsRepository = mockk(relaxed = true)
    private val primitiveTestSettingsUseCase =
        PrimitiveTestSettingsUseCase(primitiveTestSettingsRepository)

    private val mockAdditionSettingsModel: PrimitiveTestSettingsModel = mockk()
    private val mockSubtractionSettingsModel: PrimitiveTestSettingsModel = mockk()
    private val mockMultiplicationSettingsModel: PrimitiveTestSettingsModel = mockk()
    private val mockDivisionSettingsModel: PrimitiveTestSettingsModel = mockk()

    @Test
    fun getTestSettings() = runTest {
        coEvery { primitiveTestSettingsRepository.getAdditionTestSettings() } returns mockAdditionSettingsModel
        coEvery { primitiveTestSettingsRepository.getSubtractionTestSettings() } returns mockSubtractionSettingsModel
        coEvery { primitiveTestSettingsRepository.getMultiplicationTestSettings() } returns mockMultiplicationSettingsModel
        coEvery { primitiveTestSettingsRepository.getDivisionTestSettings() } returns mockDivisionSettingsModel

        val additionResult = primitiveTestSettingsUseCase.getTestSettings(PrimitiveMathOperationType.ADDITION)
        val subtractionResult = primitiveTestSettingsUseCase.getTestSettings(PrimitiveMathOperationType.SUBTRACTION)
        val multiplicationResult = primitiveTestSettingsUseCase.getTestSettings(PrimitiveMathOperationType.MULTIPLICATION)
        val divisionResult = primitiveTestSettingsUseCase.getTestSettings(PrimitiveMathOperationType.DIVISION)

        assertEquals(mockAdditionSettingsModel, additionResult)
        assertNotEquals(subtractionResult, additionResult)
        assertEquals(mockSubtractionSettingsModel, subtractionResult)
        assertNotEquals(multiplicationResult, subtractionResult)
        assertEquals(mockMultiplicationSettingsModel, multiplicationResult)
        assertNotEquals(additionResult, subtractionResult)
        assertEquals(mockDivisionSettingsModel, divisionResult)
        assertNotEquals(additionResult, subtractionResult)

        coVerify(exactly = 1) { primitiveTestSettingsRepository.getAdditionTestSettings() }
        coVerify(exactly = 1) { primitiveTestSettingsRepository.getSubtractionTestSettings() }
        coVerify(exactly = 1) { primitiveTestSettingsRepository.getMultiplicationTestSettings() }
        coVerify(exactly = 1) { primitiveTestSettingsRepository.getDivisionTestSettings() }
    }

    @Test
    fun writeTestSettings() = runTest {
        primitiveTestSettingsUseCase.writeTestSettings(PrimitiveMathOperationType.ADDITION, mockAdditionSettingsModel)
        primitiveTestSettingsUseCase.writeTestSettings(PrimitiveMathOperationType.SUBTRACTION, mockSubtractionSettingsModel)
        primitiveTestSettingsUseCase.writeTestSettings(PrimitiveMathOperationType.MULTIPLICATION, mockMultiplicationSettingsModel)
        primitiveTestSettingsUseCase.writeTestSettings(PrimitiveMathOperationType.DIVISION, mockDivisionSettingsModel)

        coVerify(exactly = 1) { primitiveTestSettingsRepository.writeAdditionTestSettings(mockAdditionSettingsModel) }
        coVerify(exactly = 1) { primitiveTestSettingsRepository.writeSubtractionTestSettings(mockSubtractionSettingsModel) }
        coVerify(exactly = 1) { primitiveTestSettingsRepository.writeMultiplicationTestSettings(mockMultiplicationSettingsModel) }
        coVerify(exactly = 1) { primitiveTestSettingsRepository.writeDivisionTestSettings(mockDivisionSettingsModel) }
    }

}