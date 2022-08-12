package com.degpeg.ui.diff

import androidx.recyclerview.widget.DiffUtil
import com.degpeg.model.VideoContentItem

internal class VideoContentItemDiff : DiffUtil.ItemCallback<VideoContentItem>() {

    override fun areItemsTheSame(old: VideoContentItem, new: VideoContentItem): Boolean {
        return old.id == new.id
    }

    override fun areContentsTheSame(o: VideoContentItem, n: VideoContentItem): Boolean {
        if (o.id != n.id) return false
        if (o.webVideoUrl != n.webVideoUrl) return false
        if (o.status != n.status) return false
        if (o.userDetail != n.userDetail) return false
        return true
    }
}