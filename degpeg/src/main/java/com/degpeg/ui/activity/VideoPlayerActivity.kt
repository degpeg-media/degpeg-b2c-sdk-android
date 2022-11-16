package com.degpeg.ui.activity

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.degpeg.Controller
import com.degpeg.R
import com.degpeg.databinding.ActivityVideoPlayerBinding
import com.degpeg.databinding.LayoutControllerBinding
import com.degpeg.utility.afterTextChanged
import com.degpeg.utility.getString
import com.degpeg.utility.gone
import com.degpeg.utility.visible
import com.degpeg.videoplayer.PlayerContentActivity
import com.degpeg.viewmodel.ChatViewModel
import com.degpeg.viewmodel.UserViewModel
import com.google.android.exoplayer2.util.Util

internal class VideoPlayerActivity : PlayerContentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initUi() {
        playerView = binding.exoPlayerView
        controller =
            LayoutControllerBinding.bind(playerView.findViewById(R.id.controller_layout_id))
        super.initUI()

        chatViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(Controller.instance)
        )[ChatViewModel::class.java]

        userViewModel = UserViewModel.getInstance()
        setUpScreenContent()

        if (intent.hasExtra("videoContentItem")) {
            videoContentItem = intent.getParcelableExtra("videoContentItem")
            if (videoContentItem == null) return
            streamUrl = videoContentItem?.webVideoUrl.orEmpty()
            fetchContentData()
            startPlay()
        } else if (intent.hasExtra("sessionId")) {
            binding.lyBottom.root.gone()
            liveUiManage()
            binding.contentLoader.visible()
            fetchSessionDetail(
                sessionId = intent.getString("sessionId", ""),
                onSuccess = {
                    binding.contentLoader.gone()
                    binding.lyBottom.root.visible()
                    videoContentItem = it
                    streamUrl = videoContentItem?.webVideoUrl.orEmpty()
                    fetchContentData()
                    startPlay()
                }, onError = {
                    binding.contentLoader.gone()
                })
        } else {
            streamUrl = intent.getStringExtra("streamUrl").orEmpty()
            startPlay()
        }
    }

    private fun setUpScreenContent() {
        binding.lyBottom.btnProductToggle.setOnClickListener(this)
        binding.lyBottom.btnShare.setOnClickListener(this)
        binding.lyBottom.btnLike.setOnClickListener(this)
        binding.layoutTopBar.txtUserCount.setOnClickListener(this)
        binding.layoutTopBar.btnClose.setOnClickListener(this)
        binding.lyBottom.btnSend.setOnClickListener(this)
        binding.lyBottom.btnMute.setOnClickListener(this)
        binding.lyBottom.edtMessage.setOnClickListener(this)

        binding.lyBottom.edtMessage.afterTextChanged {
            if (it.isEmpty()) {
                binding.lyBottom.btnSend.gone()
                binding.lyBottom.lyItem.visible()
            } else {
                binding.lyBottom.btnSend.visible()
                binding.lyBottom.lyItem.gone()
            }
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            binding.lyBottom.btnProductToggle -> toggleProductList()
            binding.layoutTopBar.btnClose -> finish()
            binding.layoutTopBar.txtUserCount -> toggleChatList()
            binding.lyBottom.btnSend -> sendChatMessage()
        }
    }

    private fun toggleProductList() {
        if (!hasProducts()) return toast("No Products Found...")
        if (binding.rvProduct.isVisible) {
            binding.rvProduct.gone()
            binding.lyBottom.btnProductToggle.alpha = 1f
        } else {
            binding.rvProduct.visible()
            binding.lyBottom.btnProductToggle.alpha = 0.5f
        }
    }

    private fun toggleChatList() {
        if (binding.rvChat.isVisible) {
            binding.rvChat.gone()
        } else {
            binding.rvChat.visible()
        }
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            startPlay()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Util.SDK_INT < 24 || player == null) {
            startPlay()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) releasePlayer()
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) releasePlayer()
    }
}