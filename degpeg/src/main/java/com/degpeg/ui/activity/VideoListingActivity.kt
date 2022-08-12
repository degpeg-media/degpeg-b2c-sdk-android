package com.degpeg.ui.activity

import android.os.Bundle
import androidx.core.content.ContextCompat
import com.degpeg.R
import com.degpeg.common.ActionBarActivity
import com.degpeg.common.Navigation
import com.degpeg.databinding.ActivityVideoListingBinding
import com.degpeg.model.VideoContentItem
import com.degpeg.ui.adapter.VideoListingAdapter
import com.degpeg.utility.*

internal class VideoListingActivity : ActionBarActivity() {

    private lateinit var binding: ActivityVideoListingBinding
    private val mList = ArrayList<VideoContentItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoListingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initUi() {
        val title = intent.getStringExtra("title") ?: getString(R.string.sdk_name)
        val subText = intent.getStringExtra("subText") ?: getString(R.string.sdk_name)

        val list = intent.getParcelableArrayListExtra<VideoContentItem>("mList")
        if (list != null) mList.addAll(list)

        binding.txtHeader.setColorSpan(
            title,
            subText,
            ContextCompat.getColor(this, R.color.black)
        )

        setUpTrendingData(list)
    }


    /**
     * Trending video section
     * */
    private var adapter: VideoListingAdapter? = null
    private fun setTrendingList() {
        adapter = VideoListingAdapter(this) {
            Navigation.startPlayer(this, it)
        }
        binding.recyclerView.adapter = adapter
    }

    private fun setUpTrendingData(mList: ArrayList<VideoContentItem>?) {
        if (adapter == null) setTrendingList()
        adapter?.set(mList)
        if (adapter?.getCurrent().isNullOrEmpty()) binding.txtError.visible()
        else binding.txtError.gone()
    }
}