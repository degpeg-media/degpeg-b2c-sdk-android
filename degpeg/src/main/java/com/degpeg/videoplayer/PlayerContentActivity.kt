package com.degpeg.videoplayer

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.degpeg.b2csdk.DegpegSDKProvider
import com.degpeg.b2csdk.DegpegSDKProvider.PROVIDER_ID
import com.degpeg.b2csdk.DegpegSDKProvider.PUBLISHER_ID
import com.degpeg.b2csdk.UserRole
import com.degpeg.databinding.ActivityVideoPlayerBinding
import com.degpeg.model.ChatItem
import com.degpeg.model.CountModel
import com.degpeg.model.ProductModel
import com.degpeg.model.VideoContentItem
import com.degpeg.socket.ChatMessageListener
import com.degpeg.socket.ConnectedListener
import com.degpeg.socket.CountModelListener
import com.degpeg.socket.SocketIO
import com.degpeg.ui.adapter.ChatAdapter
import com.degpeg.ui.adapter.ProductListAdapter
import com.degpeg.utility.*
import com.degpeg.utility.DateTimeUtil.currentUTCTime
import com.degpeg.utility.DateTimeUtil.utcToLocal
import com.degpeg.utility.LocalDataHelper.appUser
import com.degpeg.viewmodel.ChatViewModel
import com.degpeg.viewmodel.UserViewModel
import com.google.android.exoplayer2.Player

internal abstract class PlayerContentActivity : BasePlayerActivity(), View.OnClickListener,
    ConnectedListener.ConnectInterface, ChatMessageListener.ChatMessageInterface,
    CountModelListener.CountModelInterface {

    protected lateinit var binding: ActivityVideoPlayerBinding
    protected var videoContentItem: VideoContentItem? = null

    protected lateinit var userViewModel: UserViewModel
    protected lateinit var chatViewModel: ChatViewModel

    private var sessionId = ""
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var productAdapter: ProductListAdapter

    protected fun fetchContentData() {
        if (videoContentItem == null) return
        sessionId = videoContentItem?.id.orEmpty()
        Log.e("session id => $sessionId")
        Log.e("content provider id => ${videoContentItem?.contentProviderId}")

        userViewModel.fetchUser(videoContentItem?.contentProviderId.orEmpty()) {
            val user = it
            binding.layoutTopBar.imgUserProfile.setImageWithInitial(
                user?.avatar,
                user?.getInitials()
            )
            binding.layoutTopBar.txtUserName.text = user?.getNonNullName() ?: ""
        }

        liveUiManage()
        chatAdapter = ChatAdapter()
        binding.rvChat.adapter = chatAdapter

        productAdapter = ProductListAdapter(this::onItemBuyAction)
        binding.rvProduct.adapter = productAdapter

        chatViewModel.getChatItem(sessionId).observe(this, this::setChatData)
        chatViewModel.getProducts(videoContentItem?.products)
            .observe(this) { productAdapter.set(it) }

        chatViewModel.getLikeCount(sessionId).observe(this) {
            binding.txtLikeCount.text = it.getFormatted()
        }

        chatViewModel.getShareCount(sessionId).observe(this) {
            binding.txtShareCount.text = it.getFormatted()
        }

        chatViewModel.getPurchaseCount(sessionId).observe(this) {
            binding.txtPurchaseCount.text = it.getFormatted()
        }

        // connect to socket
        SocketIO.init(sessionId)
        SocketIO.connect()
    }

    private fun getViewCount() {
        chatViewModel.getViewCount(sessionId).observe(this) {
            binding.layoutTopBar.txtUserCount.text = it.getFormatted()
            chatViewModel.updateViewCount(sessionId)
        }
    }

    /**
     * product data manage
     * */
    protected fun hasProducts(): Boolean {
        return productAdapter.currentList.isNotEmpty()
    }

    private fun onItemBuyAction(product: ProductModel) {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply { data = product.getPurchaseLinkUri() }
            startActivity(intent)
            chatViewModel.updatePurchaseCount(sessionId, product)
        } catch (exception: Exception) {
            toast(exception.localizedMessage.orEmpty())
        }
    }

    /**
     * Chat item adapter
     * */
    private fun setChatData(list: ArrayList<ChatItem>?) {
        chatAdapter.set(list) {
            if (chatAdapter.itemCount < 0) binding.rvChat.gone()
            else binding.rvChat.visible()
            scrollToBottom()
        }
    }

    private fun scrollToBottom() {
        scrollToPosition(chatAdapter.itemCount - 1, false)
    }

    @Suppress("SameParameterValue")
    private fun scrollToPosition(position: Int, smoothScroll: Boolean = false) {
        if (position < 0) return
        if (position >= chatAdapter.itemCount) return
        if (smoothScroll) binding.rvChat.smoothScrollToPosition(position)
        else binding.rvChat.scrollToPosition(position)
    }

    @Suppress("unused")
    protected fun liveUiManage() {
//        if (player?.isCurrentMediaItemLive == true) {

        controller.relSeekbar.gone()
        controller.lyVideoControls.gone()
        binding.layoutLiveChat.visible()

        if (videoContentItem?.isLive() == true) {
//            controller.relSeekbar.gone()
//            controller.lyVideoControls.gone()
            binding.layoutTopBar.liveIndicator.visible()
            binding.layoutTopBar.txtUserCount.visible()
//            binding.layoutLiveChat.visible()
        } else {
//            controller.relSeekbar.visible()
//            controller.lyVideoControls.gone()
//            binding.layoutLiveChat.gone()
            binding.layoutTopBar.liveIndicator.gone()
            binding.layoutTopBar.txtUserCount.gone()
        }
    }

    /**
     * Send chat message
     * */
    protected fun sendChatMessage() {
        val param = HashMap<String, Any>()
        param["time_stamp"] = currentUTCTime()
        param["userId"] = appUser?.getNonNullUserId().orEmpty()
        param["userName"] = appUser?.userName.orEmpty()
        param["liveSessionId"] = sessionId
        param["message"] = binding.lyBottom.edtMessage.trim()

        binding.lyBottom.edtMessage.clear()
        chatViewModel.sendChatMessage(param) {
            scrollToBottom()
        }
    }

    /**
     * override methods
     * */
    private var viewUpdate = false
    override fun onPlaybackStateChanged(playbackState: Int) {
        if (playbackState == Player.STATE_READY) {
            if (!viewUpdate) {
                viewUpdate = true
            }
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            binding.lyBottom.btnShare -> {
                chatViewModel.updateShareCount(sessionId)
                this.shareVideoLink(
                    "Hey join this event:${videoContentItem?.name}\n" +
                            "On ${videoContentItem?.dateTime.utcToLocal()}",
                    videoContentItem?.webVideoUrl.orEmpty()
                )
            }
            binding.lyBottom.btnLike -> {
                chatViewModel.updateLikeCount(sessionId)
            }
            binding.lyBottom.edtMessage -> {
                if (!binding.rvChat.isVisible()) binding.rvChat.visible()
            }
        }
    }

    /**
     * Socket management
     * */
    override fun onStart() {
        super.onStart()
        SocketIO.connectedListener.setCallback(tag, this)
        SocketIO.chatMessageListener.setCallback(tag, this)
        SocketIO.viewCountListener.setCallback(tag, this)
        SocketIO.likeCountListener.setCallback(tag, this)
        SocketIO.purchaseCountListener.setCallback(tag, this)
        SocketIO.shareCountListener.setCallback(tag, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        SocketIO.disconnect()
        SocketIO.connectedListener.removeCallback(tag)
        SocketIO.chatMessageListener.removeCallback(tag)
        SocketIO.viewCountListener.removeCallback(tag)
        SocketIO.likeCountListener.removeCallback(tag)
        SocketIO.purchaseCountListener.removeCallback(tag)
        SocketIO.shareCountListener.removeCallback(tag)
    }

    override fun onConnectAndJoined() {
        getViewCount()
    }

    override fun onNewMessage(chatItem: ChatItem) {
        chatViewModel.addChatItem(chatItem)
    }

    override fun onCountUpdate(countModel: CountModel, type: String) {
        Log.e("type $type : ${countModel.toJson()}")
        when (type) {
            CountModelListener.VIEW_COUNT -> {
                chatViewModel.liveUsersLiveData.postValue(countModel)
            }
            CountModelListener.LIKE_COUNT -> {
                chatViewModel.likeCountLiveData.postValue(countModel)
            }
            CountModelListener.SHARE_COUNT -> {
                chatViewModel.shareCountLiveData.postValue(countModel)
            }
            CountModelListener.PURCHASE_COUNT -> {
                chatViewModel.purchaseCountLiveData.postValue(countModel)
            }
        }
    }
}