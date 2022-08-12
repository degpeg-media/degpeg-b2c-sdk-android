package com.degpeg.ui.diff

import androidx.recyclerview.widget.DiffUtil
import com.degpeg.model.ChatItem
import com.degpeg.model.ProductModel

internal class ProductItemDiff : DiffUtil.ItemCallback<ProductModel>() {

    override fun areItemsTheSame(old: ProductModel, new: ProductModel): Boolean {
        return old.id == new.id
    }

    override fun areContentsTheSame(o: ProductModel, n: ProductModel): Boolean {
        if (o.id != n.id) return false
        return true
    }
}