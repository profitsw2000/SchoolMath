package ru.profitsw2000.mainscreen.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.profitsw2000.core.utils.ADDITION_CATEGORY_CODE
import ru.profitsw2000.core.utils.DIVISION_CATEGORY_CODE
import ru.profitsw2000.core.utils.MULTIPLICATION_CATEGORY_CODE
import ru.profitsw2000.core.utils.SUBTRACTION_CATEGORY_CODE
import ru.profitsw2000.mainscreen.R
import ru.profitsw2000.mainscreen.databinding.MathTestCategoryItemBinding

class CategoriesAdapter(
    private val onCategoryClick: (Int) -> Unit
) : ListAdapter<Int, CategoriesAdapter.CategoryViewHolder>(
    CategoryDiffCallback()
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryViewHolder {
        val binding = MathTestCategoryItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(binding, onCategoryClick)
    }

    override fun onBindViewHolder(
        holder: CategoryViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    class CategoryViewHolder(
        private val binding: MathTestCategoryItemBinding,
        private val onCategoryClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var currentCategoryId: Int? = null

        init {
            binding.mainCardView.setOnClickListener {
                currentCategoryId?.let { id ->
                    onCategoryClick(id)
                }
            }
        }

        val image = binding.mathTestCategoryItemImageView
        val title = binding.mathTestCategoryTextView
        val card = binding.mainCardView
        fun bind(categoryId: Int) {
            currentCategoryId = categoryId

            when(categoryId) {
                ADDITION_CATEGORY_CODE ->
                    setItemUi(
                        card,
                        image,
                        title,
                        ru.profitsw2000.core.R.color.oslo_gray,
                        R.drawable.elephant,
                        R.string.addition_category_title)
                SUBTRACTION_CATEGORY_CODE ->
                    setItemUi(
                        card,
                        image,
                        title,
                        ru.profitsw2000.core.R.color.red,
                        R.drawable.rabbit,
                        R.string.subtraction_category_title)
                MULTIPLICATION_CATEGORY_CODE ->
                    setItemUi(
                        card,
                        image,
                        title,
                        ru.profitsw2000.core.R.color.red,
                        R.drawable.bear,
                        R.string.multiplication_category_title)
                DIVISION_CATEGORY_CODE ->
                    setItemUi(
                        card,
                        image,
                        title,
                        ru.profitsw2000.core.R.color.red,
                        R.drawable.raccoon,
                        R.string.division_category_title)
                else ->
                    setItemUi(
                        card,
                        image,
                        title,
                        ru.profitsw2000.core.R.color.oslo_gray,
                        R.drawable.elephant,
                        R.string.addition_category_title)
            }
        }

        private fun setItemUi(
            cardView: CardView,
            imageView: ImageView,
            textView: TextView,
            colorResourceId: Int,
            imageResourceId: Int,
            textResourceId: Int
        ) {
            cardView.setCardBackgroundColor(colorResourceId)
            imageView.setImageResource(imageResourceId)
            textView.setText(textResourceId)
        }
    }

    private class CategoryDiffCallback : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

    }
}