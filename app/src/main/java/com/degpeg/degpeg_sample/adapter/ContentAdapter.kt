package com.degpeg.degpeg_sample.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.degpeg.degpeg_sample.databinding.ItemContentBinding
import com.degpeg.degpeg_sample.model.ContentModel
import com.degpeg.utility.loadImage

internal class ContentAdapter constructor(
    val context: Context,
    val onVideoItemClick: (videoData: ContentModel) -> Unit
) :
    ListAdapter<ContentModel, ContentAdapter.ViewHolder>(ContentItemDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getCurrent(): MutableList<ContentModel> {
        return this.currentList.toMutableList()
    }

    fun set(mList: MutableList<ContentModel>?) {
        if (mList.isNullOrEmpty()) return
        submitList(mList)
    }

    class ViewHolder(
        private val binding: ItemContentBinding,
        private val adapter: ContentAdapter
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ContentModel) {
            binding.image.setImageResource(data.image)
            binding.txtName.text = data.name
            binding.txtPrice.text = data.price
            binding.rootView.setOnClickListener { adapter.onVideoItemClick.invoke(data) }
        }
    }

}