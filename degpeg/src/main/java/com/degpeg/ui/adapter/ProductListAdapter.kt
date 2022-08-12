package com.degpeg.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.degpeg.databinding.ItemProductBinding
import com.degpeg.model.ProductModel
import com.degpeg.ui.diff.ProductItemDiff
import com.degpeg.utility.loadImage

internal class ProductListAdapter(val onItemBuyAction: (product: ProductModel) -> Unit) :
    ListAdapter<ProductModel, ProductListAdapter.ViewHolder>(ProductItemDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun set(mList: MutableList<ProductModel>?) {
        if (mList.isNullOrEmpty()) return
        submitList(mList)
    }

    class ViewHolder(
        private val binding: ItemProductBinding,
        private val adapter: ProductListAdapter
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ProductModel) {
            binding.txtTitle.text = data.name
            binding.txtAmount.text = String.format(data.currency + " " + data.price)
            binding.image.loadImage(data.imageUrl)
            binding.btnBuyNow.setOnClickListener { adapter.onItemBuyAction.invoke(data) }
        }
    }

}