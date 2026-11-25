package ru.profitsw2000.mainscreen.presentation.view

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
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
import ru.profitsw2000.core.R
import ru.profitsw2000.mainscreen.presentation.uiState.CategoryHistoryUiState
import ru.profitsw2000.mainscreen.presentation.uiState.CategorySettingsUiState
import ru.profitsw2000.mainscreen.presentation.uiState.MainUiState
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
            every { historyUiState } returns historyUiState
            every { settingsUiState } returns settingsUiState
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
    fun `ошибка загрузки истории тестов`() {
        launchPrimitiveMathTestCategoryFragment()

        historyUiState.value = CategoryHistoryUiState.Error

        onView(withId(ru.profitsw2000.mainscreen.R.id.math_test_result_first_linear_layout))
            .check(matches(not(isDisplayed())))
    }

    private fun launchPrimitiveMathTestCategoryFragment() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext()).apply {
            setViewModelStore(androidx.lifecycle.ViewModelStore())
            setGraph(ru.profitsw2000.mainscreen.R.navigation.main_screen_graph)
        }

        launchFragmentInContainer<PrimitiveMathTestCategoryFragment> (
            themeResId = R.style.Theme_SchoolMath,
            factory = object : androidx.fragment.app.FragmentFactory() {
                override fun instantiate(classLoader: ClassLoader, className: String): androidx.fragment.app.Fragment {
                    return PrimitiveMathTestCategoryFragment().also { fragment ->
                        fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                            if (viewLifecycleOwner != null) {
                                Navigation.setViewNavController(fragment.requireView(), navController)
                            }
                        }
                    }
                }
            }
        )
    }

}