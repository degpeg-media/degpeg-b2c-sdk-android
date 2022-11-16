package com.degpeg.common

import android.app.Activity
import android.content.Intent
import com.degpeg.model.VideoContentItem
import com.degpeg.ui.activity.VideoListingActivity
import com.degpeg.ui.activity.VideoPlayerActivity

internal object Navigation {
    fun openVideoListingActivity(
        activity: Activity,
        title: String,
        subText: String,
        mList: ArrayList<VideoContentItem>?
    ) {
        val intent = Intent(activity, VideoListingActivity::class.java)
        intent.putExtra("title", title)
        intent.putExtra("subText", subText)
        intent.putParcelableArrayListExtra("mList", mList)
        activity.startActivity(intent)
    }


    fun startPlayer(activity: Activity, videoContentItem: VideoContentItem) {
        activity.startActivity(Intent(activity, VideoPlayerActivity::class.java).apply {
            putExtra("videoContentItem", videoContentItem)
        })
    }

    fun startPlayer(activity: Activity, sessionId: String) {
        activity.startActivity(Intent(activity, VideoPlayerActivity::class.java).apply {
            putExtra("sessionId", sessionId)
        })
    }
}