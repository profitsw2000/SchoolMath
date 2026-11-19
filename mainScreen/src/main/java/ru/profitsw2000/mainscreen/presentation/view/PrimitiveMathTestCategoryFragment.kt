package ru.profitsw2000.mainscreen.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.profitsw2000.mainscreen.R

class PrimitiveMathTestCategoryFragment : Fragment() {

    //private var _binding: FragmentP

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_primitive_math_test_category, container, false)
    }
}