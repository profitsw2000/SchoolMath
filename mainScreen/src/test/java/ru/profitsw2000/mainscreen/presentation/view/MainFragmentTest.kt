package ru.profitsw2000.mainscreen.presentation.view

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
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
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ru.profitsw2000.core.R
import ru.profitsw2000.mainscreen.presentation.uiState.MainUiState
import ru.profitsw2000.mainscreen.presentation.viewmodel.MainViewModel
import ru.profitsw2000.navigator.Navigator

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class MainFragmentTest {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var navigator: Navigator
    private lateinit var uiStateFlow: MutableStateFlow<MainUiState>

    @Before
    fun setUp() {
        stopKoin()

        uiStateFlow = MutableStateFlow(MainUiState.Loading)
        mainViewModel = mockk(relaxed = true) {
            every { categoryUiState } returns uiStateFlow
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
    fun `в состоянии загрузки видим прогресс бар`() {
        uiStateFlow.value = MainUiState.Loading

        launchMainFragment()

        onView(withId(ru.profitsw2000.mainscreen.R.id.progress_bar))
            .check(matches(isDisplayed()))
        onView(withId(ru.profitsw2000.mainscreen.R.id.error_message_text_view))
            .check(matches(not(isDisplayed())))
    }

    private fun launchMainFragment() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext()).apply {
            setViewModelStore(androidx.lifecycle.ViewModelStore()) // Обязательно до setGraph!
            setGraph(ru.profitsw2000.mainscreen.R.navigation.main_screen_graph)
        }

        launchFragmentInContainer<MainFragment> (
            themeResId = R.style.Theme_SchoolMath,
            factory = object : androidx.fragment.app.FragmentFactory() {
                override fun instantiate(classLoader: ClassLoader, className: String): androidx.fragment.app.Fragment {
                    return MainFragment().also { fragment ->
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