package com.degpeg.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.degpeg.databinding.ItemTextChatBinding
import com.degpeg.model.ChatItem
import com.degpeg.ui.diff.ChatItemDiff

internal class ChatAdapter :
    ListAdapter<ChatItem, ChatAdapter.ViewHolder>(ChatItemDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemTextChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun set(mList: MutableList<ChatItem>?, runnable: Runnable?) {
        if (mList.isNullOrEmpty()) return
        submitList(mList, runnable)
    }

    class ViewHolder(
        private val binding: ItemTextChatBinding,
        private val adapter: ChatAdapter
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ChatItem) {
            binding.imgChatProfile.setImageWithInitial("", data.getInitials())
            binding.txtMessage.text = data.message
        }
    }

}