package ru.profitsw2000.mainscreen.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.navigation.koinNavGraphViewModel
import ru.profitsw2000.core.model.PrimitiveTestResultModel
import ru.profitsw2000.core.model.PrimitiveTestSettingsModel
import ru.profitsw2000.core.utils.GENERAL_TEST_HARD_COMPLEXITY_CODE
import ru.profitsw2000.core.utils.GENERAL_TEST_LIGHT_COMPLEXITY_CODE
import ru.profitsw2000.core.utils.GENERAL_TEST_MIDDLE_COMPLEXITY_CODE
import ru.profitsw2000.core.utils.GENERAL_TEST_NORMAL_COMPLEXITY_CODE
import ru.profitsw2000.mainscreen.R
import ru.profitsw2000.mainscreen.data.model.PrimitiveTestSettingsCardModel
import ru.profitsw2000.mainscreen.databinding.FragmentPrimitiveMathTestCategoryBinding
import ru.profitsw2000.mainscreen.presentation.uiState.CategoryHistoryUiState
import ru.profitsw2000.mainscreen.presentation.uiState.CategorySettingsUiState
import ru.profitsw2000.mainscreen.presentation.viewmodel.MainViewModel
import ru.profitsw2000.navigator.Navigator
import kotlin.getValue

class PrimitiveMathTestCategoryFragment : Fragment() {

    private var _binding: FragmentPrimitiveMathTestCategoryBinding? = null
    private val binding
        get() = _binding!!
    private val mainViewModel: MainViewModel by koinNavGraphViewModel(R.id.main_screen_graph)
    private val navigator: Navigator by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentPrimitiveMathTestCategoryBinding.bind(
                inflater.inflate(R.layout.fragment_primitive_math_test_category, container, false)
            )        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeFlows()
    }

    private fun initViews() = with(binding) {

    }

    private fun observeFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.historyUiState.collect { historyState ->
                    renderHistoryData(historyState)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.settingsUiState.collect { settingsState ->
                    renderSettingsData(settingsState)
                }
            }
        }
    }

    private fun renderHistoryData(categoryHistoryUiState: CategoryHistoryUiState) {
        when(categoryHistoryUiState) {
            CategoryHistoryUiState.Error -> handleHistoryError()
            CategoryHistoryUiState.Loading -> {}
            is CategoryHistoryUiState.Success -> setHistoryViews(categoryHistoryUiState.primitiveTestResultModelList)
        }
    }

    private fun renderSettingsData(settingsUiState: CategorySettingsUiState) {
        when(settingsUiState) {
            CategorySettingsUiState.Error -> handleSettingsError()
            CategorySettingsUiState.Loading -> {}
            is CategorySettingsUiState.Success -> setSettingsViews(settingsUiState.primitiveTestSettingsCardModel)
        }
    }

    private fun handleHistoryError() = with(binding) {
        testResultsGroup.visibility = View.GONE
        noLastTestsResultsErrorMessageTextView.visibility = View.VISIBLE
    }

    private fun handleSettingsError() = with(binding) {
        settingsGroup.visibility = View.GONE
        settingsErrorMessageTextView.visibility = View.VISIBLE
    }

    private fun setHistoryViews(primitiveTestResultModelList: List<PrimitiveTestResultModel>) {
        when(primitiveTestResultModelList.size) {
            0 -> handleHistoryError()
            else -> setHistoryLayouts(primitiveTestResultModelList)
        }
    }

    private fun setHistoryLayouts(
        primitiveTestResultModelList: List<PrimitiveTestResultModel>
    ) = with(binding) {
        primitiveTestResultModelList.forEachIndexed { index, model ->
            when(index) {
                1 ->
                    assessmentFirstTextView.text =
                        "${model.testAssessment} (${model.correctAnswersNumber}/${model.settingsModel.testTasksNumber})"
                2 ->
                    assessmentSecondTextView.text =
                        "${model.testAssessment} (${model.correctAnswersNumber}/${model.settingsModel.testTasksNumber})"
                3 ->
                    assessmentThirdTextView.text =
                        "${model.testAssessment} (${model.correctAnswersNumber}/${model.settingsModel.testTasksNumber})"
                else -> {}
            }
        }
    }

/*    private fun setHistoryView(textView: TextView, text: String) {
        t
        textView.text = text
    }*/

    private fun setSettingsViews(
        primitiveTestSettingsCardModel: PrimitiveTestSettingsCardModel
    ) = with(binding) {
        taskCountValueTextView.text = primitiveTestSettingsCardModel.testTaskNumber.toString()
        taskTimeValueTextView.text = primitiveTestSettingsCardModel.testTaskTime
        taskComplexityItemTextView.text =
            getTaskComplexityString(primitiveTestSettingsCardModel.complexityLevelCode)
        if (primitiveTestSettingsCardModel.extraFieldNumber == null)
            taskExtraFieldItemTextView.visibility = View.GONE
        else
            taskExtraFieldItemTextView.text = getExtraFieldSettingsString(mainViewModel.selectedPrimitiveMathOperationTypeCode)
    }

    private fun getTaskComplexityString(taskComplexityCode: Int): String {
        return when(taskComplexityCode) {
            GENERAL_TEST_LIGHT_COMPLEXITY_CODE -> resources.getString(R.string.light_complexity_task_settings_text)
            GENERAL_TEST_NORMAL_COMPLEXITY_CODE -> resources.getString(R.string.normal_complexity_task_settings_text)
            GENERAL_TEST_MIDDLE_COMPLEXITY_CODE -> resources.getString(R.string.middle_complexity_task_settings_text)
            GENERAL_TEST_HARD_COMPLEXITY_CODE -> resources.getString(R.string.hard_complexity_task_settings_text)
            else -> resources.getString(R.string.light_complexity_task_settings_text)
        }
    }

    private fun getExtraFieldSettingsString(categoryCode: Int): String {
        return "0"
    }
}

/**
 * package com.example.yourapp // Замените на ваш package
 *
 * import android.content.res.ColorStateList
 * import android.graphics.Color
 * import android.os.Bundle
 * import android.widget.TextView
 * import androidx.appcompat.app.AppCompatActivity
 * import com.google.android.material.button.MaterialButton
 * import com.google.android.material.slider.Slider
 *
 * class MainActivity : AppCompatActivity() {
 *
 *     private var selectedDifficulty = 1 // 1 - лёгкий, 2 - средний, 3 - сложный
 *
 *     override fun onCreate(savedInstanceState: Bundle?) {
 *         super.onCreate(savedInstanceState)
 *         setContentView(R.layout.activity_main)
 *
 *         val tvDifficulty = findViewById<TextView>(R.id.tvDifficulty)
 *         val sliderDifficulty = findViewById<Slider>(R.id.sliderDifficulty)
 *         val btnNewTest = findViewById<MaterialButton>(R.id.btnNewTest)
 *
 *         // Слушатель изменения положения ползунка
 *         sliderDifficulty.addOnChangeListener { _, value, _ ->
 *             selectedDifficulty = value.toInt()
 *             updateDifficultyUI(selectedDifficulty, tvDifficulty, sliderDifficulty)
 *         }
 *
 *         // Обработка нажатия на кнопку НОВЫЙ ТЕСТ
 *         btnNewTest.setOnClickListener {
 *             // Здесь можно запустить экран теста и передать selectedDifficulty
 *         }
 *     }
 *
 *     private fun updateDifficultyUI(level: Int, textView: TextView, slider: Slider) {
 *         when (level) {
 *             1 -> {
 *                 textView.text = "Сложность - лёгкий"
 *                 val green = Color.parseColor("#22C55E")
 *                 textView.setTextColor(green)
 *                 slider.thumbTintList = ColorStateList.valueOf(Color.parseColor("#16A34A"))
 *                 slider.trackActiveTintList = ColorStateList.valueOf(green)
 *                 slider.trackInactiveTintList = ColorStateList.valueOf(Color.parseColor("#DCFCE7"))
 *             }
 *             2 -> {
 *                 textView.text = "Сложность - средний"
 *                 val amber = Color.parseColor("#F59E0B")
 *                 textView.setTextColor(amber)
 *                 slider.thumbTintList = ColorStateList.valueOf(Color.parseColor("#D97706"))
 *                 slider.trackActiveTintList = ColorStateList.valueOf(amber)
 *                 slider.trackInactiveTintList = ColorStateList.valueOf(Color.parseColor("#FEF3C7"))
 *             }
 *             3 -> {
 *                 textView.text = "Сложность - сложный"
 *                 val red = Color.parseColor("#EF4444")
 *                 textView.setTextColor(red)
 *                 slider.thumbTintList = ColorStateList.valueOf(Color.parseColor("#DC2626"))
 *                 slider.trackActiveTintList = ColorStateList.valueOf(red)
 *                 slider.trackInactiveTintList = ColorStateList.valueOf(Color.parseColor("#FEE2E2"))
 *             }
 *         }
 *     }
 * }
 *
 */