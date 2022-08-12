package com.degpeg.ui.diff

import androidx.recyclerview.widget.DiffUtil
import com.degpeg.model.ChatItem

internal class ChatItemDiff : DiffUtil.ItemCallback<ChatItem>() {

    override fun areItemsTheSame(old: ChatItem, new: ChatItem): Boolean {
        return old.id == new.id
    }

    override fun areContentsTheSame(o: ChatItem, n: ChatItem): Boolean {
        if (o.id != n.id) return false
        if (o.timeStamp != n.timeStamp) return false
        if (o.message != n.message) return false
        return true
    }
}