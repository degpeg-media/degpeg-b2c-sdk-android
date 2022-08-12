package com.degpeg.videoplayer

import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import com.degpeg.R
import com.degpeg.b2csdk.DegpegSDKProvider.getMediaItem
import com.degpeg.common.BaseActivity
import com.degpeg.databinding.LayoutControllerBinding
import com.degpeg.utility.Log
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.ExoTrackSelection
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource


internal abstract class BasePlayerActivity : BaseActivity(), View.OnClickListener {

    protected lateinit var controller: LayoutControllerBinding
    protected lateinit var playerView: StyledPlayerView
    protected var player: ExoPlayer? = null

    private var playWhenReady = true
    private var playbackPosition = 0L

    private var currentVolume = 0f
    private var isMuted = false
    protected var streamUrl = ""

    protected fun initUI() {
        keepScreenOn()
        controllerClickListener()
    }

    private fun playerInitialization() {
        if (player == null) {
            val userAgent = "LiveStreamPlayer"

            val trackSelectionFactory: ExoTrackSelection.Factory = AdaptiveTrackSelection.Factory()
            val defaultRenderersFactory =
                DefaultRenderersFactory(this).setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER)

            val httpDataSourceFactory = DefaultHttpDataSource.Factory()
                .setUserAgent(userAgent).setAllowCrossProtocolRedirects(true)

            val mediaSourceFactory =
                DefaultMediaSourceFactory(httpDataSourceFactory).setLiveTargetOffsetMs(5000)

            val trackSelector = DefaultTrackSelector(this, trackSelectionFactory)
            player = ExoPlayer.Builder(this, defaultRenderersFactory)
                .setTrackSelector(trackSelector)
                .setSeekBackIncrementMs(10000)
                .setSeekForwardIncrementMs(10000)
                .setMediaSourceFactory(mediaSourceFactory)
                .build()

            playerView.player = player
        }
    }

    protected fun startPlay() {
        Log.e("streamUrl : $streamUrl")
        if (streamUrl.isEmpty()) return
        if (player == null) playerInitialization()

        player?.setMediaItem(streamUrl.getMediaItem())
        player?.playWhenReady = playWhenReady
        player?.seekToDefaultPosition()
        player?.prepare()
        player?.addListener(playerListener)
    }

    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            this@BasePlayerActivity.onPlaybackStateChanged(playbackState)
        }

        override fun onPlayerError(error: PlaybackException) {
            if (error.errorCode == PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW) {
                player?.seekToDefaultPosition()
                player?.prepare()
            }
        }
    }

    protected fun releasePlayer() {
        if (player != null) {
            playWhenReady = player!!.playWhenReady
            playbackPosition = player!!.currentPosition
            player!!.release()
            player = null
        }
    }

    fun hideSystemUi() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && window.insetsController != null) {
                window.setDecorFitsSystemWindows(false)
                window.insetsController?.let {
                    it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                    it.systemBarsBehavior =
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            } else {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /**
     * Handle controller button click events
     * */
    private fun controllerClickListener() {
        controller.btnAspectRatio.setOnClickListener(this)
        controller.btnSpeed.setOnClickListener(this)
        controller.btnMute.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            controller.btnAspectRatio -> {
                if (player == null) return
                val dialog = AspectRatioBottomSheet(
                    PlayerAspectRatio[playerView.resizeMode],
                    ratioChangeCallback = {
                        playerView.resizeMode = it.id
                    },
                    dismissCallback = {})
                dialog.show(supportFragmentManager, dialog::class.java.simpleName)
            }
            controller.btnSpeed -> {
                if (player == null) return
                val dialog = PlaybackSpeedBottomSheet(player!!, dismissCallback = {})
                dialog.show(this, dialog::class.java.simpleName)
            }
            controller.btnMute -> {
                muteVideo()
            }
        }
    }

    /**
     * mute video
     * */
    private fun muteVideo() {
        if (isMuted) {
            player?.volume = currentVolume
            isMuted = false
        } else {
            currentVolume = player?.volume ?: 1f
            player?.volume = 0f
            isMuted = true
        }
        if (isMuted) controller.btnMute.setImageResource(R.drawable.ic_volume_off)
        else controller.btnMute.setImageResource(R.drawable.ic_volume)
    }

    /**
     * abstract methods
     * */
    abstract fun onPlaybackStateChanged(playbackState: Int)
}