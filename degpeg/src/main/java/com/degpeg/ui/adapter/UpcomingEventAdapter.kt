package com.degpeg.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.degpeg.databinding.ItemHomeUpcomingBinding
import com.degpeg.model.VideoContentItem
import com.degpeg.ui.diff.VideoContentItemDiff
import com.degpeg.utility.DateTimeUtil.utcToLocal
import com.degpeg.utility.loadImage

internal class UpcomingEventAdapter(
    val context: Context,
    val onVideoItemClick: (data: VideoContentItem) -> Unit
) :
    ListAdapter<VideoContentItem,UpcomingEventAdapter.ViewHolder>(VideoContentItemDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemHomeUpcomingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun set(mList: MutableList<VideoContentItem>?) {
        submitList(mList)
    }

    fun set(mList: MutableList<VideoContentItem>?, commitCallback : ()->Unit) {
        submitList(mList, commitCallback)
    }

    class ViewHolder(
        private val binding: ItemHomeUpcomingBinding,
        private val adapter: UpcomingEventAdapter
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: VideoContentItem) {
            binding.txtName.text = data.name
            binding.txtDate.text = data.dateTime.utcToLocal()
            binding.image.loadImage(data.bannerUrl)
//            binding.image.setOnClickListener { adapter.onVideoItemClick.invoke(data) }
        }
    }

}