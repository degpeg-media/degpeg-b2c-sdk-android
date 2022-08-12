package com.degpeg.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.degpeg.databinding.*
import com.degpeg.model.VideoContentItem
import com.degpeg.ui.diff.VideoContentItemDiff
import com.degpeg.utility.gone
import com.degpeg.utility.loadImage
import com.degpeg.utility.visible

internal class VideoListingAdapter constructor(
    val context: Context,
    val onVideoItemClick: (videoData: VideoContentItem) -> Unit
) :
    ListAdapter<VideoContentItem, VideoListingAdapter.ViewHolder>(VideoContentItemDiff()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemVideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getCurrent(): MutableList<VideoContentItem> {
        return this.currentList.toMutableList()
    }

    fun set(mList: MutableList<VideoContentItem>?) {
        if (mList.isNullOrEmpty()) return
        submitList(mList)
    }

    class ViewHolder(
        private val binding: ItemVideoItemBinding,
        private val adapter: VideoListingAdapter
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: VideoContentItem) {
            binding.image.loadImage(data.bannerUrl)
            if (data.isLive()) {
                binding.txtLive.visible()
                binding.txtLiveCount.visible()
            } else {
                binding.txtLive.gone()
                binding.txtLiveCount.gone()
            }

            binding.rootView.setOnClickListener { adapter.onVideoItemClick.invoke(data) }
        }
    }

}