package com.degpeg.b2csdk

import android.app.Activity
import android.content.Intent
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.degpeg.model.User
import com.degpeg.network.ResponseHandler
import com.degpeg.repository.ContentRepository
import com.degpeg.ui.activity.DegpegHomeActivity
import com.degpeg.ui.activity.VideoPlayerActivity
import com.degpeg.ui.fragment.HomeFragment
import com.degpeg.utility.Constants.TEST_LIVE_STREAM_URL
import com.degpeg.utility.LocalDataHelper
import com.google.android.exoplayer2.MediaItem
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers

object DegpegSDKProvider {

    internal var APP_ID: String = ""
    internal var SECRET_KEY: String = ""
    internal var PUBLISHER_ID: String = ""

    fun init(
        appId: String,
        secretKey: String,
        onSuccess: (() -> Unit)? = null,
        onError: ((errorMessage: String) -> Unit)? = null
    ) {
        this.APP_ID = appId
        this.SECRET_KEY = secretKey
        updateAuthToken(appId, secretKey, onSuccess, onError)
    }

    fun startAsActivity(activity: Activity, publisherId: String) {
        PUBLISHER_ID = publisherId
        activity.startActivity(Intent(activity, DegpegHomeActivity::class.java))
    }

    fun useAsFragment(
        supportFragmentManager: FragmentManager,
        @IdRes containerId: Int,
        publisherId: String
    ) {
        PUBLISHER_ID = publisherId
        val fragment = HomeFragment.newInstance()
        supportFragmentManager.commit {
            replace(containerId, fragment, fragment::class.java.simpleName)
        }
    }

    internal fun startPlayer(activity: Activity, streamUrl: String = TEST_LIVE_STREAM_URL) {
        activity.startActivity(Intent(activity, VideoPlayerActivity::class.java).apply {
            putExtra("streamUrl", streamUrl)
        })
    }

    internal fun String.getMediaItem(): MediaItem {
        val configuration = MediaItem.LiveConfiguration.Builder().setMaxPlaybackSpeed(1.02f).build()
        return MediaItem.Builder()
            .setUri(this)
            .setLiveConfiguration(configuration)
            .build()
    }

    internal fun updateAuthToken(
        appId: String,
        secretKey: String,
        onSuccess: (() -> Unit)?,
        onError: ((errorMessage: String) -> Unit)?
    ) {
        if (!LocalDataHelper.authToken.isNullOrEmpty()) {
            onSuccess?.invoke()
            return
        }

        val param = HashMap<String, Any>()
        param["appId"] = appId
        param["secretKey"] = secretKey

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            onError?.invoke(ResponseHandler.handleErrorResponse(throwable))
        }
        ContentRepository.getAuthToken(
            param = param,
            dispatcher = Dispatchers.IO + exceptionHandler,
            responseCallback = {
                val data = it.body()
                if (it.isSuccessful && data != null) {
                    LocalDataHelper.authToken = data.token
                    onSuccess?.invoke()
                } else {
                    onError?.invoke(data?.error?.message ?: it.message())
                }
            }
        )
    }

    fun updateUser(user: User) {
        LocalDataHelper.appUser = user
    }
}