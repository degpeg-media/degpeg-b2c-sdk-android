package com.degpeg.ui.diff

import androidx.recyclerview.widget.DiffUtil
import com.degpeg.model.SessionCategoryItem
import com.degpeg.model.VideoContentItem

internal class SessionCategoryItemDiff : DiffUtil.ItemCallback<SessionCategoryItem>() {

    override fun areItemsTheSame(old: SessionCategoryItem, new: SessionCategoryItem): Boolean {
        return old.id == new.id
    }

    override fun areContentsTheSame(o: SessionCategoryItem, n: SessionCategoryItem): Boolean {
        if (o.id != n.id) return false
        return true
    }
}