package ru.profitsw2000.mainscreen.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.navigation.koinNavGraphViewModel
import ru.profitsw2000.core.model.PrimitiveTestResultModel
import ru.profitsw2000.core.model.PrimitiveTestSettingsModel
import ru.profitsw2000.core.utils.ADDITION_CATEGORY_CODE
import ru.profitsw2000.core.utils.DIVISION_CATEGORY_CODE
import ru.profitsw2000.core.utils.GENERAL_TEST_HARD_COMPLEXITY_CODE
import ru.profitsw2000.core.utils.GENERAL_TEST_LIGHT_COMPLEXITY_CODE
import ru.profitsw2000.core.utils.GENERAL_TEST_MIDDLE_COMPLEXITY_CODE
import ru.profitsw2000.core.utils.GENERAL_TEST_NORMAL_COMPLEXITY_CODE
import ru.profitsw2000.core.utils.MULTIPLICATION_CATEGORY_CODE
import ru.profitsw2000.core.utils.SUBTRACTION_CATEGORY_CODE
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
        newTestButton.setOnClickListener {

        }
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
                0 -> setHistoryView(
                    mathTestResultFirstLinearLayout,
                    assessmentFirstTextView,
                    model
                )
                1 -> setHistoryView(
                    mathTestResultSecondLinearLayout,
                    assessmentSecondTextView,
                    model
                )
                2 -> setHistoryView(
                    mathTestResultThirdLinearLayout,
                    assessmentThirdTextView,
                    model
                )
                else -> {}
            }
        }
    }

    private fun setHistoryView(
        linearLayout: LinearLayout,
        textView: TextView,
        primitiveTestResultModel: PrimitiveTestResultModel
    ) {
        linearLayout.visibility = View.VISIBLE
        textView.text =
            "${primitiveTestResultModel.testAssessment} (${primitiveTestResultModel.correctAnswersNumber}/${primitiveTestResultModel.settingsModel.testTasksNumber})"
    }

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
            taskExtraFieldItemTextView.text =
                getExtraFieldSettingsString(
                    mainViewModel.selectedPrimitiveMathOperationTypeCode,
                    primitiveTestSettingsCardModel.extraFieldNumber
                )
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

    private fun getExtraFieldSettingsString(
        categoryCode: Int,
        extraFieldNumber: Int
    ): String {
        return when(categoryCode) {
            ADDITION_CATEGORY_CODE -> resources.getString(R.string.primitive_test_extra_settings_sum_under, extraFieldNumber)
            SUBTRACTION_CATEGORY_CODE -> ""
            MULTIPLICATION_CATEGORY_CODE -> ""
            DIVISION_CATEGORY_CODE -> ""
            else -> ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
