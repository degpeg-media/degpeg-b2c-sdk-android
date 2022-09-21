package com.degpeg.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.degpeg.databinding.ItemTrendingVideoBinding
import com.degpeg.model.VideoContentItem
import com.degpeg.ui.diff.VideoContentItemDiff
import com.degpeg.utility.gone
import com.degpeg.utility.loadImage
import com.degpeg.utility.visible
import java.util.*
import kotlin.collections.ArrayList

internal class TrendingVideosAdapter(
    val context: Context,
    val onTrendingVideoItemClick: (data: VideoContentItem) -> Unit
) :
    ListAdapter<VideoContentItem, TrendingVideosAdapter.ViewHolder>(VideoContentItemDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemTrendingVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getCurrent(): MutableList<VideoContentItem> {
        return this.currentList.toMutableList()
    }

    fun set(mList: MutableList<VideoContentItem>?) {
        submitList(mList)
    }

    fun set(mList: MutableList<VideoContentItem>?, commitCallback : ()->Unit) {
        submitList(mList, commitCallback)
    }

    class ViewHolder(
        private val binding: ItemTrendingVideoBinding,
        private val adapter: TrendingVideosAdapter
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: VideoContentItem) {
            binding.image.loadImage(data.bannerUrl)
            if (data.isLive()) {
                binding.txtLive.visible()
                binding.txtLiveCount.gone()
            } else {
                binding.txtLive.gone()
                binding.txtLiveCount.gone()
            }

            binding.rootView.setOnClickListener { adapter.onTrendingVideoItemClick.invoke(data) }
        }
    }

}