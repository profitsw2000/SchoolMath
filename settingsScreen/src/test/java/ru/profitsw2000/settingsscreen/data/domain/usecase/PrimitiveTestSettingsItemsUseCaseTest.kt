package ru.profitsw2000.settingsscreen.data.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import ru.profitsw2000.core.utils.PrimitiveMathOperationType
import ru.profitsw2000.settingsscreen.data.domain.model.PrimitiveTestSettingsItemsModel
import ru.profitsw2000.settingsscreen.data.domain.repository.PrimitiveTestSettingsItemsRepository

@OptIn(ExperimentalCoroutinesApi::class)
class PrimitiveTestSettingsItemsUseCaseTest {

    private val primitiveTestSettingsItemsRepository: PrimitiveTestSettingsItemsRepository = mockk()
    private val primitiveTestSettingsItemsUseCase = PrimitiveTestSettingsItemsUseCase(primitiveTestSettingsItemsRepository)

    private val additionMockModel: PrimitiveTestSettingsItemsModel = mockk()
    private val subtractionMockModel: PrimitiveTestSettingsItemsModel = mockk()
    private val multiplicationMockModel: PrimitiveTestSettingsItemsModel = mockk()
    private val divisionMockModel: PrimitiveTestSettingsItemsModel = mockk()

    @Test
    fun getPrimitiveTestSettingsItems() = runTest {
        coEvery {
            primitiveTestSettingsItemsRepository.getPrimitiveTestSettingsItems(
                PrimitiveMathOperationType.ADDITION
            )
        } returns additionMockModel
        coEvery {
            primitiveTestSettingsItemsRepository.getPrimitiveTestSettingsItems(
                PrimitiveMathOperationType.SUBTRACTION
            )
        } returns subtractionMockModel
        coEvery {
            primitiveTestSettingsItemsRepository.getPrimitiveTestSettingsItems(
                PrimitiveMathOperationType.MULTIPLICATION
            )
        } returns multiplicationMockModel
        coEvery {
            primitiveTestSettingsItemsRepository.getPrimitiveTestSettingsItems(
                PrimitiveMathOperationType.DIVISION
            )
        } returns divisionMockModel

        val additionResult = primitiveTestSettingsItemsUseCase.getPrimitiveTestSettingsItems(PrimitiveMathOperationType.ADDITION)
        val subtractionResult = primitiveTestSettingsItemsUseCase.getPrimitiveTestSettingsItems(PrimitiveMathOperationType.SUBTRACTION)
        val multiplicationResult = primitiveTestSettingsItemsUseCase.getPrimitiveTestSettingsItems(PrimitiveMathOperationType.MULTIPLICATION)
        val divisionResult = primitiveTestSettingsItemsUseCase.getPrimitiveTestSettingsItems(PrimitiveMathOperationType.DIVISION)

        assertEquals(additionMockModel, additionResult)
        assertNotEquals(subtractionMockModel, additionResult)
        assertEquals(subtractionMockModel, subtractionResult)
        assertNotEquals(multiplicationMockModel, additionResult)
        assertEquals(multiplicationMockModel, multiplicationResult)
        assertNotEquals(divisionMockModel, multiplicationResult)
        assertEquals(divisionMockModel, divisionResult)
        assertNotEquals(additionMockModel, divisionResult)

        coVerify(exactly = 1) { primitiveTestSettingsItemsRepository.getPrimitiveTestSettingsItems(PrimitiveMathOperationType.ADDITION) }
        coVerify(exactly = 1) { primitiveTestSettingsItemsRepository.getPrimitiveTestSettingsItems(PrimitiveMathOperationType.SUBTRACTION) }
        coVerify(exactly = 1) { primitiveTestSettingsItemsRepository.getPrimitiveTestSettingsItems(PrimitiveMathOperationType.MULTIPLICATION) }
        coVerify(exactly = 1) { primitiveTestSettingsItemsRepository.getPrimitiveTestSettingsItems(PrimitiveMathOperationType.DIVISION) }
    }

}