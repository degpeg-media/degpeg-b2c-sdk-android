package com.degpeg.degpeg_sample.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.degpeg.databinding.ItemSliderImageBinding
import com.degpeg.degpeg_sample.databinding.ItemSliderBinding

internal class SliderAdapter(
    var list: ArrayList<Int>,
    val callback: (data: String, position: Int) -> Unit
) :
    RecyclerView.Adapter<SliderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, this)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list.get(position))
    }

    class ViewHolder(
        val binding: ItemSliderBinding,
        private val adapter: SliderAdapter
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