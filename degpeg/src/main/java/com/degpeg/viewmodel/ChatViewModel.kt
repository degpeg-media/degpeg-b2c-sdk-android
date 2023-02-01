package com.degpeg.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.degpeg.model.ChatItem
import com.degpeg.model.ProductModel
import com.degpeg.network.Resource
import com.degpeg.network.ResponseHandler
import com.degpeg.repository.ChatRepository
import com.degpeg.repository.ContentRepository
import com.degpeg.socket.SocketHelper.CHAT_MESSAGE
import com.degpeg.socket.SocketIO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.collections.set

internal class ChatViewModel : CountViewModel() {

    private var chatItemLiveData = MutableLiveData<ArrayList<ChatItem>>()
    private var productLiveData = MutableLiveData<ArrayList<ProductModel>>()

    /**
     * Chat message manage
     * */
    fun getChatItem(liveSessionId: String): MutableLiveData<ArrayList<ChatItem>> {
        if (chatItemLiveData.value.isNullOrEmpty()) {
            viewModelScope.launch {
                try {
                    val response =
                        ChatRepository.getChatMessages(liveSessionId)
                    val data = response.body()
                    if (response.isSuccessful && !data.isNullOrEmpty()) {
                        chatItemLiveData.postValue(response.body())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return chatItemLiveData
    }

    fun addChatItem(chatItem: ChatItem) {
        chatItemLiveData.addItem(chatItem)
    }

    fun sendChatMessage(param: HashMap<String, Any>, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val response =
                    ChatRepository.sendChatMessages(param)
                val data = response.body()
                if (response.isSuccessful && data != null) {
                    chatItemLiveData.addItem(data)

                    //send message to socket
                    val socketParam: HashMap<String, Any> = HashMap(param)
                    socketParam["userId"] = socketParam["userName"] as Any
                    socketParam["session_id"] = socketParam["liveSessionId"] as Any
                    socketParam.remove("liveSessionId")
                    socketParam.remove("userName")
//                    SocketIO.emit(CHAT_MESSAGE, socketParam.toJSONObject())
                    SocketIO.emit(CHAT_MESSAGE, socketParam)

                    onSuccess.invoke()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * get product list
     * */
    fun getProducts(contentProviderId: List<String>?): MutableLiveData<ArrayList<ProductModel>> {
        if (productLiveData.value.isNullOrEmpty()) {
            viewModelScope.launch {
                val flowData = flow {
                    contentProviderId?.forEach() {
                        val response =
                            ChatRepository.getProducts(it)
                        val data = response.body()
                        if (response.isSuccessful && !data.isNullOrEmpty()) {
                            emit(data)
                        }
                    }
                }

                val list = ArrayList<ProductModel>()
                flowData.collect { value ->
                    list.addAll(value)
                }
                if (list.isNotEmpty()) {
                    productLiveData.postValue(list)
                }
            }
        }
        return productLiveData
    }

    /**
     * Get Details of session
     * */
    fun getSessionDetail(sessionId: String) = liveData(Dispatchers.IO) {
        try {
            ResponseHandler.responseParser(ContentRepository.getSessionDetail(sessionId), this)
        } catch (e: Exception) {
            emit(Resource.error(ResponseHandler.handleErrorResponse(e)))
        }
    }

}