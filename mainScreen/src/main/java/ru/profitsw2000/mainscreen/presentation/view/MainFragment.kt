package ru.profitsw2000.mainscreen.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.navigation.koinNavGraphViewModel
import ru.profitsw2000.mainscreen.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.profitsw2000.mainscreen.databinding.FragmentMainBinding
import ru.profitsw2000.mainscreen.presentation.uiState.MainUiState
import ru.profitsw2000.mainscreen.presentation.view.adapter.CategoriesAdapter
import ru.profitsw2000.mainscreen.presentation.viewmodel.MainViewModel
import ru.profitsw2000.navigator.Navigator
import kotlin.getValue

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding
        get() = _binding!!
    private val mainViewModel: MainViewModel by koinNavGraphViewModel(R.id.main_screen_graph)
    private val navigator: Navigator by inject()
    private val adapter = CategoriesAdapter { categoryId ->
        mainViewModel.setCategory(categoryId)
        mainViewModel.loadCategoryHistory()
        mainViewModel.loadCategorySettings()
        navigator.navigateToCategoryDetails()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.bind(
            inflater.inflate(R.layout.fragment_main, container, false)
        )
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) mainViewModel.loadCategories()
        initViews()
        observeFlows()
    }

    private fun initViews() = with(binding) {
        mathTestCategoriesRecyclerview.adapter = adapter
    }

    private fun observeFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.categoryUiState.collect { state ->
                    renderData(state)
                }
            }
        }
    }

    private fun renderData(mainUiState: MainUiState) {
        when(mainUiState) {
            MainUiState.Error -> handleError()
            MainUiState.Loading -> setProgressBar(true)
            is MainUiState.Success -> setCategoryList(mainUiState.categoriesCodesList)
        }
    }

    private fun handleError() = with(binding) {
        setProgressBar(false)
        errorMessageTextView.visibility = View.VISIBLE
    }

    private fun setProgressBar(isVisible: Boolean) = with(binding) {
        errorMessageTextView.visibility = View.GONE
        if (isVisible) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun setCategoryList(categoryIdList: List<Int>) = with(binding) {
        setProgressBar(false)
        errorMessageTextView.visibility = View.GONE
        adapter.submitList(categoryIdList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}