package ru.profitsw2000.simpletestsscreen.data.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import ru.profitsw2000.simpletestsscreen.data.domain.model.PrimitiveMathTaskModel
import ru.profitsw2000.simpletestsscreen.data.domain.repository.PrimitiveTestTaskGeneratorRepository

class PrimitiveTestTaskGeneratorUseCaseTest {

    private val repository: PrimitiveTestTaskGeneratorRepository = mockk()
    private val primitiveTestTaskGeneratorUseCase = PrimitiveTestTaskGeneratorUseCase(repository)

    @Test
    fun `получение примера на сложение`() = runTest() {
        val expectedTask = mockk<PrimitiveMathTaskModel>()
        coEvery { repository.generateTask(1) } returns expectedTask
        val result = primitiveTestTaskGeneratorUseCase.getAdditionTask(1)

        assertEquals(expectedTask, result)
        coVerify(exactly = 1) { repository.generateTask(1) }
    }

}