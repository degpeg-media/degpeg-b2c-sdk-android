package com.degpeg.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.degpeg.R
import com.degpeg.databinding.ItemCategoriesHomeBinding
import com.degpeg.model.SessionCategoryItem
import com.degpeg.ui.diff.SessionCategoryItemDiff
import com.degpeg.utility.loadImage

internal class CategoryAdapter() :
    ListAdapter<SessionCategoryItem, CategoryAdapter.ViewHolder>(SessionCategoryItemDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCategoriesHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    fun set(mList: MutableList<SessionCategoryItem>?) {
        submitList(mList)
    }

    fun set(mList: MutableList<SessionCategoryItem>?, commitCallback : ()->Unit) {
        submitList(mList, commitCallback)
    }

    class ViewHolder(
        private val binding: ItemCategoriesHomeBinding,
        private val adapter: CategoryAdapter
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: SessionCategoryItem, position: Int) {
            binding.image.loadImage(data.categoryImageUrl,0)
            binding.image.setBackgroundColor(
                ContextCompat.getColor(
                    binding.image.context,
                    when {
                        (position % 2 == 0) -> R.color.red
                        else -> R.color.brown
                    }
                )
            )
            binding.txtName.text = data.name
        }
    }
}