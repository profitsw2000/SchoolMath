package ru.profitsw2000.mainscreen.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.profitsw2000.mainscreen.R
import ru.profitsw2000.mainscreen.databinding.FragmentPrimitiveMathTestCategoryBinding

class PrimitiveMathTestCategoryFragment : Fragment() {

    private var _binding: FragmentPrimitiveMathTestCategoryBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_primitive_math_test_category, container, false)
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