package com.degpeg.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.degpeg.model.CountModel
import com.degpeg.model.ProductModel
import com.degpeg.repository.ChatRepository
import com.degpeg.socket.SocketHelper
import com.degpeg.socket.SocketIO
import com.degpeg.utility.Constants.MOBILE_SOURCE
import com.degpeg.utility.DateTimeUtil
import com.degpeg.utility.LocalDataHelper
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.collections.set

internal open class CountViewModel : ViewModel() {

    private fun getViewCountUpdateParam(liveSessionId: String): HashMap<String, Any> {
        val param = HashMap<String, Any>()
        param["start"] = DateTimeUtil.currentUTCTime()
        param["end"] = DateTimeUtil.currentUTCTime()
        param["source"] = MOBILE_SOURCE
        param["liveSessionId"] = liveSessionId
        return param
    }

    private fun getCountUpdateParam(liveSessionId: String): HashMap<String, Any> {
        val param = HashMap<String, Any>()
        param["userId"] = LocalDataHelper.appUser?.userId.orEmpty()
        param["time_stamp"] = DateTimeUtil.currentUTCTime()
        param["source"] = MOBILE_SOURCE
        param["liveSessionId"] = liveSessionId
        return param
    }

    /**
     * view count manage
     * */
    var liveUsersLiveData = MutableLiveData<CountModel>()
    fun getViewCount(liveSessionId: String): MutableLiveData<CountModel> {
        if (liveUsersLiveData.value == null) {
            viewModelScope.launch {
                try {
                    val response =
                        ChatRepository.getSessionViews(liveSessionId)
                    val data = response.body()
                    if (response.isSuccessful) {
                        liveUsersLiveData.postValue(
                            CountModel(data?.size?.toLong() ?: 0, liveSessionId)
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return liveUsersLiveData
    }

    private var isCountUpdated = false
    fun updateViewCount(liveSessionId: String) {
        if (isCountUpdated) return
        val countModel = liveUsersLiveData.value ?: return
        viewModelScope.launch {
            try {
                val response =
                    ChatRepository.updateViewCount(getViewCountUpdateParam(liveSessionId))
                if (response.isSuccessful) {
                    isCountUpdated = true
                    countModel.count = countModel.count.plus(1)
                    liveUsersLiveData.postValue(countModel)

                    SocketIO.emit(SocketHelper.UPDATE_VIEW,
                        JSONObject().apply {
                            put("count", countModel.count)
                            put("session_id", liveSessionId)
                        })
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    /**
     * Like count manage
     * */
    var likeCountLiveData = MutableLiveData<CountModel>()
    fun getLikeCount(liveSessionId: String): MutableLiveData<CountModel> {
        if (likeCountLiveData.value == null) {
            viewModelScope.launch {
                try {
                    val response =
                        ChatRepository.getLikeCount(liveSessionId)
                    val data = response.body()
                    if (response.isSuccessful) {
                        likeCountLiveData.postValue(
                            CountModel(
                                data?.size?.toLong() ?: 0,
                                liveSessionId
                            )
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return likeCountLiveData
    }

    fun updateLikeCount(liveSessionId: String) {
        val countModel = likeCountLiveData.value ?: return

        viewModelScope.launch {
            try {
                val response = ChatRepository.updateLikeCount(getCountUpdateParam(liveSessionId))
                if (response.isSuccessful) {
                    countModel.count = countModel.count.plus(1)
                    likeCountLiveData.postValue(countModel)

                    SocketIO.emit(SocketHelper.UPDATE_LIKE,
                        JSONObject().apply {
                            put("count", countModel.count)
                            put("session_id", liveSessionId)
                        })
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Share count manage
     * */
    var purchaseCountLiveData = MutableLiveData<CountModel>()
    fun getPurchaseCount(liveSessionId: String): MutableLiveData<CountModel> {
        if (purchaseCountLiveData.value == null) {
            viewModelScope.launch {
                try {
                    val response =
                        ChatRepository.getPurchaseCount(liveSessionId)
                    val data = response.body()
                    if (response.isSuccessful) {
                        purchaseCountLiveData.postValue(
                            CountModel(
                                data?.size?.toLong() ?: 0,
                                liveSessionId
                            )
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return purchaseCountLiveData
    }

    fun updatePurchaseCount(liveSessionId: String, productModel: ProductModel) {
        val countModel = purchaseCountLiveData.value ?: return
        val param = HashMap<String, Any>()
        param["time_stamp"] = DateTimeUtil.currentUTCTime()
        param["liveSessionId"] = liveSessionId
        param["amount"] = productModel.price

        viewModelScope.launch {
            try {
                val response =
                    ChatRepository.updatePurchaseCount(param)
                if (response.isSuccessful) {
                    countModel.count = countModel.count.plus(1)
                    purchaseCountLiveData.postValue(countModel)

                    SocketIO.emit(SocketHelper.UPDATE_PURCHASE,
                        JSONObject().apply {
                            put("count", countModel.count)
                            put("session_id", liveSessionId)
                        })
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Share count manage
     * */
    var shareCountLiveData = MutableLiveData<CountModel>()
    fun getShareCount(liveSessionId: String): MutableLiveData<CountModel> {
        if (shareCountLiveData.value == null) {
            viewModelScope.launch {
                try {
                    val response =
                        ChatRepository.getShareCount(liveSessionId)
                    val data = response.body()
                    if (response.isSuccessful) {
                        shareCountLiveData.postValue(CountModel(data?.size?.toLong() ?: 0, liveSessionId))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return shareCountLiveData
    }

    fun updateShareCount(liveSessionId: String) {
        val countModel = shareCountLiveData.value ?: return

        viewModelScope.launch {
            try {
                val response = ChatRepository.updateShareCount(getCountUpdateParam(liveSessionId))
                if (response.isSuccessful) {
                    countModel.count = countModel.count.plus(1)
                    shareCountLiveData.postValue(countModel)

                    SocketIO.emit(SocketHelper.UPDATE_SHARE,
                        JSONObject().apply {
                            put("count", countModel.count)
                            put("session_id", liveSessionId)
                        })
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}