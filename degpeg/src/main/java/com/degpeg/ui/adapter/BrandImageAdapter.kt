package com.degpeg.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.degpeg.databinding.ItemBrandBinding
import com.degpeg.databinding.ItemSliderImageBinding

internal class BrandImageAdapter(
    var list: ArrayList<Int>,
) :
    RecyclerView.Adapter<BrandImageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemBrandBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, this)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list.get(position))
    }

    class ViewHolder(
        val binding: ItemBrandBinding,
        private val adapter: BrandImageAdapter
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(@DrawableRes data: Int) {
            binding.image.setImageResource(data)
        }
    }
}