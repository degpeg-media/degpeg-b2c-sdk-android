package com.degpeg.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.degpeg.databinding.ItemSliderImageBinding

internal class SliderImageAdapter(
    var list: ArrayList<Int>,
    val callback: (data: String, position: Int) -> Unit
) :
    RecyclerView.Adapter<SliderImageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemSliderImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, this)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list.get(position))
    }

    class ViewHolder(
        val binding: ItemSliderImageBinding,
        private val adapter: SliderImageAdapter
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(@DrawableRes data: Int) {
            binding.image.setImageResource(data)
            binding.image.setOnClickListener {
                adapter.callback.invoke("", absoluteAdapterPosition)
            }

        }
    }
}