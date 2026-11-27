package ru.profitsw2000.mainscreen.presentation.view

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.Shadows.shadowOf
import ru.profitsw2000.core.R
import ru.profitsw2000.core.model.PrimitiveTestResultModel
import ru.profitsw2000.core.utils.ADDITION_CATEGORY_CODE
import ru.profitsw2000.mainscreen.data.model.PrimitiveTestSettingsCardModel
import ru.profitsw2000.mainscreen.presentation.uiState.CategoryHistoryUiState
import ru.profitsw2000.mainscreen.presentation.uiState.CategorySettingsUiState
import ru.profitsw2000.mainscreen.presentation.viewmodel.MainViewModel
import ru.profitsw2000.navigator.Navigator
import kotlin.intArrayOf


@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class PrimitiveMathTestCategoryFragmentTest {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var navigator: Navigator
    private lateinit var historyUiState: MutableStateFlow<CategoryHistoryUiState>
    private lateinit var settingsUiState: MutableStateFlow<CategorySettingsUiState>

    @Before
    fun setUp() {
        stopKoin()

        historyUiState = MutableStateFlow(CategoryHistoryUiState.Loading)
        settingsUiState = MutableStateFlow(CategorySettingsUiState.Loading)

        mainViewModel = mockk(relaxed = true) {
            every { historyUiState } returns this@PrimitiveMathTestCategoryFragmentTest.historyUiState
            every { settingsUiState } returns this@PrimitiveMathTestCategoryFragmentTest.settingsUiState
            every { selectedPrimitiveMathOperationTypeCode } returns 1
        }
        navigator = mockk(relaxed = true)

        val testModule = module {
            viewModel { mainViewModel }
            single { navigator }
        }

        startKoin {
            modules(testModule)
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `установка иконки для категории сложения`() {
        every {
            mainViewModel.selectedPrimitiveMathOperationTypeCode
        } returns 1
        val scenario = launchPrimitiveMathTestCategoryFragment()

        scenario.onFragment { fragment ->
            val view = fragment.requireView()
            assertImageResource(view.findViewById(ru.profitsw2000.mainscreen.R.id.math_test_result_first_raw_image_view), ru.profitsw2000.mainscreen.R.drawable.add_icon)
            assertImageResource(view.findViewById(ru.profitsw2000.mainscreen.R.id.math_test_result_second_raw_image_view), ru.profitsw2000.mainscreen.R.drawable.add_icon)
            assertImageResource(view.findViewById(ru.profitsw2000.mainscreen.R.id.math_test_result_third_raw_image_view), ru.profitsw2000.mainscreen.R.drawable.add_icon)
        }
    }

    @Test
    fun `установка иконки для категории вычитания`() {
        every {
            mainViewModel.selectedPrimitiveMathOperationTypeCode
        } returns 2
        val scenario = launchPrimitiveMathTestCategoryFragment()

        scenario.onFragment { fragment ->
            val view = fragment.requireView()
            assertImageResource(view.findViewById(ru.profitsw2000.mainscreen.R.id.math_test_result_first_raw_image_view), ru.profitsw2000.mainscreen.R.drawable.subtraction_icon)
            assertImageResource(view.findViewById(ru.profitsw2000.mainscreen.R.id.math_test_result_second_raw_image_view), ru.profitsw2000.mainscreen.R.drawable.subtraction_icon)
            assertImageResource(view.findViewById(ru.profitsw2000.mainscreen.R.id.math_test_result_third_raw_image_view), ru.profitsw2000.mainscreen.R.drawable.subtraction_icon)
        }
    }

    @Test
    fun `установка иконки для категории умножения`() {
        every {
            mainViewModel.selectedPrimitiveMathOperationTypeCode
        } returns 3
        val scenario = launchPrimitiveMathTestCategoryFragment()

        scenario.onFragment { fragment ->
            val view = fragment.requireView()
            assertImageResource(view.findViewById(ru.profitsw2000.mainscreen.R.id.math_test_result_first_raw_image_view), ru.profitsw2000.mainscreen.R.drawable.multiplication_icon)
            assertImageResource(view.findViewById(ru.profitsw2000.mainscreen.R.id.math_test_result_second_raw_image_view), ru.profitsw2000.mainscreen.R.drawable.multiplication_icon)
            assertImageResource(view.findViewById(ru.profitsw2000.mainscreen.R.id.math_test_result_third_raw_image_view), ru.profitsw2000.mainscreen.R.drawable.multiplication_icon)
        }
    }

    @Test
    fun `установка иконки для категории деления`() {
        every {
            mainViewModel.selectedPrimitiveMathOperationTypeCode
        } returns 4
        val scenario = launchPrimitiveMathTestCategoryFragment()

        scenario.onFragment { fragment ->
            val view = fragment.requireView()
            assertImageResource(view.findViewById(ru.profitsw2000.mainscreen.R.id.math_test_result_first_raw_image_view), ru.profitsw2000.mainscreen.R.drawable.division_icon)
            assertImageResource(view.findViewById(ru.profitsw2000.mainscreen.R.id.math_test_result_second_raw_image_view), ru.profitsw2000.mainscreen.R.drawable.division_icon)
            assertImageResource(view.findViewById(ru.profitsw2000.mainscreen.R.id.math_test_result_third_raw_image_view), ru.profitsw2000.mainscreen.R.drawable.division_icon)
        }
    }

    @Test
    fun `установка иконки по умолчанию при неизвестном коде категории`() {
        every { mainViewModel.selectedPrimitiveMathOperationTypeCode } returns -1
        val scenario = launchPrimitiveMathTestCategoryFragment()

        scenario.onFragment { fragment ->
            val view = fragment.requireView()
            assertImageResource(view.findViewById(ru.profitsw2000.mainscreen.R.id.math_test_result_first_raw_image_view), ru.profitsw2000.mainscreen.R.drawable.add_icon)
            assertImageResource(view.findViewById(ru.profitsw2000.mainscreen.R.id.math_test_result_second_raw_image_view), ru.profitsw2000.mainscreen.R.drawable.add_icon)
            assertImageResource(view.findViewById(ru.profitsw2000.mainscreen.R.id.math_test_result_third_raw_image_view), ru.profitsw2000.mainscreen.R.drawable.add_icon)
        }
    }


    @Test
    fun `ошибка загрузки истории тестов`() {
        launchPrimitiveMathTestCategoryFragment()

        historyUiState.value = CategoryHistoryUiState.Error

        onView(withId(ru.profitsw2000.mainscreen.R.id.math_test_result_first_linear_layout))
            .check(matches(not(isDisplayed())))
        onView(withId(ru.profitsw2000.mainscreen.R.id.no_last_tests_results_error_message_text_view))
            .check(matches(isDisplayed()))
    }

    @Test
    fun `пустой список истории тестов отображается как отсутствие результатов`() {
        launchPrimitiveMathTestCategoryFragment()

        historyUiState.value = CategoryHistoryUiState.Success(emptyList())

        onView(withId(ru.profitsw2000.mainscreen.R.id.test_results_group))
            .check(matches(not(isDisplayed())))
        onView(withId(ru.profitsw2000.mainscreen.R.id.no_last_tests_results_error_message_text_view))
            .check(matches(isDisplayed()))
    }

    @Test
    fun `успешная загрузка одного результата теста отображает только первую строку`() {
        launchPrimitiveMathTestCategoryFragment()
        val mockModel = createResultModel(assessment = 5, correct = 10, total = 10)

        historyUiState.value = CategoryHistoryUiState.Success(listOf(mockModel))

        onView(withId(ru.profitsw2000.mainscreen.R.id.no_last_tests_results_error_message_text_view)).check(matches(not(isDisplayed())))
        onView(withId(ru.profitsw2000.mainscreen.R.id.math_test_result_first_linear_layout)).check(matches(isDisplayed()))
        onView(withId(ru.profitsw2000.mainscreen.R.id.assessment_first_text_view)).check(matches(withText("5 (10/10)")))
        onView(withId(ru.profitsw2000.mainscreen.R.id.math_test_result_second_linear_layout)).check(matches(not(isDisplayed())))
        onView(withId(ru.profitsw2000.mainscreen.R.id.math_test_result_third_linear_layout)).check(matches(not(isDisplayed())))
    }

    @Test
    fun `успешная загрузка двух результатов теста отображает две строки`() {
        launchPrimitiveMathTestCategoryFragment()
        val results = listOf(
            createResultModel(assessment = 5, correct = 10, total = 10),
            createResultModel(assessment = 3, correct = 6, total = 20)
        )

        historyUiState.value = CategoryHistoryUiState.Success(results)

        onView(withId(ru.profitsw2000.mainscreen.R.id.no_last_tests_results_error_message_text_view)).check(matches(not(isDisplayed())))
        onView(withId(ru.profitsw2000.mainscreen.R.id.math_test_result_first_linear_layout)).check(matches(isDisplayed()))
        onView(withId(ru.profitsw2000.mainscreen.R.id.assessment_first_text_view)).check(matches(withText("5 (10/10)")))
        onView(withId(ru.profitsw2000.mainscreen.R.id.math_test_result_second_linear_layout)).check(matches(isDisplayed()))
        onView(withId(ru.profitsw2000.mainscreen.R.id.assessment_second_text_view)).check(matches(withText("3 (6/20)")))
        onView(withId(ru.profitsw2000.mainscreen.R.id.math_test_result_third_linear_layout)).check(matches(not(isDisplayed())))
    }

    @Test
    fun `успешная загрузка трех результатов тестов корректно заполняет все три строки`() {
        launchPrimitiveMathTestCategoryFragment()
        val results = listOf(
            createResultModel(assessment = 5, correct = 10, total = 10),
            createResultModel(assessment = 4, correct = 8, total = 10),
            createResultModel(assessment = 3, correct = 6, total = 10)
        )

        historyUiState.value = CategoryHistoryUiState.Success(results)

        onView(withId(ru.profitsw2000.mainscreen.R.id.no_last_tests_results_error_message_text_view)).check(matches(not(isDisplayed())))

        onView(withId(ru.profitsw2000.mainscreen.R.id.math_test_result_first_linear_layout)).check(matches(isDisplayed()))
        onView(withId(ru.profitsw2000.mainscreen.R.id.assessment_first_text_view)).check(matches(withText("5 (10/10)")))

        onView(withId(ru.profitsw2000.mainscreen.R.id.math_test_result_second_linear_layout)).check(matches(isDisplayed()))
        onView(withId(ru.profitsw2000.mainscreen.R.id.assessment_second_text_view)).check(matches(withText("4 (8/10)")))

        onView(withId(ru.profitsw2000.mainscreen.R.id.math_test_result_third_linear_layout)).check(matches(isDisplayed()))
        onView(withId(ru.profitsw2000.mainscreen.R.id.assessment_third_text_view)).check(matches(withText("3 (6/10)")))
    }

    @Test
    fun `ошибка загрузки настроек скрывает группу настроек и показывает текст ошибки`() {
        launchPrimitiveMathTestCategoryFragment()

        settingsUiState.value = CategorySettingsUiState.Error

        onView(withId(ru.profitsw2000.mainscreen.R.id.settings_group)).check(matches(not(isDisplayed())))
        onView(withId(ru.profitsw2000.mainscreen.R.id.settings_error_message_text_view)).check(matches(isDisplayed()))
    }

    @Test
    fun `отображение настроек с легкой сложностью и дополнительным полем для сложения`() {
        every { mainViewModel.selectedPrimitiveMathOperationTypeCode } returns ADDITION_CATEGORY_CODE
        val scenario = launchPrimitiveMathTestCategoryFragment()

        val settingsModel = createSettingsModel(
            taskNumber = 15,
            taskTime = "05:00",
            complexityCode = 1,
            extraFieldNumber = 20
        )
        settingsUiState.value = CategorySettingsUiState.Success(settingsModel)
        onView(withId(ru.profitsw2000.mainscreen.R.id.settings_error_message_text_view)).check(matches(not(isDisplayed())))
        onView(withId(ru.profitsw2000.mainscreen.R.id.task_count_value_text_view)).check(matches(withText("15")))
        onView(withId(ru.profitsw2000.mainscreen.R.id.task_time_value_text_view)).check(matches(withText("05:00")))
        scenario.onFragment { fragment ->
            val expectedComplexity = fragment.getString(ru.profitsw2000.mainscreen.R.string.light_complexity_task_settings_text)
            val expectedExtra = fragment.getString(ru.profitsw2000.mainscreen.R.string.primitive_test_extra_settings_sum_under, 20)
            onView(withId(ru.profitsw2000.mainscreen.R.id.task_complexity_item_text_view)).check(matches(withText(expectedComplexity)))
            onView(withId(ru.profitsw2000.mainscreen.R.id.task_extra_field_item_text_view)).check(matches(isDisplayed()))
            onView(withId(ru.profitsw2000.mainscreen.R.id.task_extra_field_item_text_view)).check(matches(withText(expectedExtra)))
        }
    }

    @Test
    fun `скрытие дополнительного поля настроек если extraFieldNumber равен null`() {
        val scenario = launchPrimitiveMathTestCategoryFragment()
        //launchPrimitiveMathTestCategoryFragment()
        val settingsModel = createSettingsModel(
            taskNumber = 20,
            taskTime = "10:00",
            complexityCode = 4,
            extraFieldNumber = null
        )
        settingsUiState.value = CategorySettingsUiState.Success(settingsModel)
        onView(withId(ru.profitsw2000.mainscreen.R.id.settings_error_message_text_view)).check(matches(not(isDisplayed())))
        onView(withId(ru.profitsw2000.mainscreen.R.id.task_count_value_text_view)).check(matches(withText("20")))
        onView(withId(ru.profitsw2000.mainscreen.R.id.task_time_value_text_view)).check(matches(withText("10:00")))
        scenario.onFragment { fragment ->
            val expectedComplexity = fragment.getString(ru.profitsw2000.mainscreen.R.string.hard_complexity_task_settings_text)
            onView(withId(ru.profitsw2000.mainscreen.R.id.task_complexity_item_text_view)).check(matches(withText(expectedComplexity)))
        }
        onView(withId(ru.profitsw2000.mainscreen.R.id.task_extra_field_item_text_view)).check(matches(not(isDisplayed())))
    }

    @Test
    fun `корректное отображение всех уровней сложности`() {
        val scenario = launchPrimitiveMathTestCategoryFragment()
        val complexities = mapOf(
            1 to ru.profitsw2000.mainscreen.R.string.light_complexity_task_settings_text,
            2 to ru.profitsw2000.mainscreen.R.string.normal_complexity_task_settings_text,
            3 to ru.profitsw2000.mainscreen.R.string.middle_complexity_task_settings_text,
            4 to ru.profitsw2000.mainscreen.R.string.hard_complexity_task_settings_text,
            999 to ru.profitsw2000.mainscreen.R.string.light_complexity_task_settings_text // ветка else
        )
        complexities.forEach { (code, expectedStringRes) ->
            settingsUiState.value = CategorySettingsUiState.Success(
                createSettingsModel(complexityCode = code)
            )
            scenario.onFragment { fragment ->
                val expectedText = fragment.getString(expectedStringRes)
                onView(withId(ru.profitsw2000.mainscreen.R.id.task_complexity_item_text_view))
                    .check(matches(withText(expectedText)))
            }
        }
    }

    private fun assertImageResource(imageView: ImageView, @DrawableRes expectedResId: Int) {
        val actualResId = shadowOf(imageView.drawable).createdFromResId
        assertEquals(expectedResId, actualResId)
    }

    private fun launchPrimitiveMathTestCategoryFragment() = launchFragmentInContainer<PrimitiveMathTestCategoryFragment> (
            themeResId = R.style.Theme_SchoolMath,
            factory = object : androidx.fragment.app.FragmentFactory() {
                override fun instantiate(classLoader: ClassLoader, className: String): androidx.fragment.app.Fragment {
                    return PrimitiveMathTestCategoryFragment().also { fragment ->
                        fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                            if (viewLifecycleOwner != null) {
                                Navigation.setViewNavController(fragment.requireView(), createNavController())
                            }
                        }
                    }
                }
            }
        )


    private fun createNavController() = TestNavHostController(ApplicationProvider.getApplicationContext()).apply {
        setViewModelStore(androidx.lifecycle.ViewModelStore())
        setGraph(ru.profitsw2000.mainscreen.R.navigation.main_screen_graph)
    }

    private fun createResultModel(
        assessment: Int,
        correct: Int,
        total: Int
    ): PrimitiveTestResultModel = mockk(relaxed = true) {
        every { testAssessment } returns assessment
        every { correctAnswersNumber } returns correct
        every { settingsModel.testTasksNumber } returns total
    }

    private fun createSettingsModel(
        taskNumber: Int = 10,
        taskTime: String = "03:00",
        complexityCode: Int = 1,
        extraFieldNumber: Int? = null
    ): PrimitiveTestSettingsCardModel = mockk(relaxed = true) {
        every { testTaskNumber } returns taskNumber
        every { testTaskTime } returns taskTime
        every { complexityLevelCode } returns complexityCode
        every { this@mockk.extraFieldNumber } returns extraFieldNumber
    }

}