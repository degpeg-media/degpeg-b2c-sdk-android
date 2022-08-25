package com.degpeg.degpeg_sample.adapter

import androidx.recyclerview.widget.DiffUtil
import com.degpeg.degpeg_sample.model.ContentModel

internal class ContentItemDiff : DiffUtil.ItemCallback<ContentModel>() {

    override fun areItemsTheSame(old: ContentModel, new: ContentModel): Boolean {
        return old.name == new.name
    }

    override fun areContentsTheSame(o: ContentModel, n: ContentModel): Boolean {
        if (o.name != n.name) return false
        if (o.image != n.image) return false
        return true
    }
}