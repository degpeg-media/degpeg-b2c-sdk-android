package com.degpeg.repository

import com.degpeg.model.ChatItem
import com.degpeg.model.CountModel
import com.degpeg.network.RetroClient.apiService
import org.json.JSONObject
import retrofit2.Response

internal object ChatRepository {

    private fun getLiveSessionFilter(liveSessionId: String): HashMap<String, Any> {
        val jsonObject = JSONObject().put(
            "where",
            JSONObject().put("liveSessionId", liveSessionId)
        )
        return hashMapOf(Pair("filter", jsonObject.toString()))
    }

    private fun getContentProviderFilter(contentProviderId: String): HashMap<String, Any> {
        val jsonObject = JSONObject().put(
            "where",
            JSONObject().put("contentProviderId", contentProviderId)
        )
        return hashMapOf(Pair("filter", jsonObject.toString()))
    }

    private fun getProductFilter(productId: String): HashMap<String, Any> {
        val jsonObject = JSONObject().put(
            "where",
            JSONObject().put("id", productId)
        )
        return hashMapOf(Pair("filter", jsonObject.toString()))
    }

    suspend fun getChatMessages(liveSessionId: String): Response<ArrayList<ChatItem>> {
        return apiService.getChatMessages(getLiveSessionFilter(liveSessionId))
    }

    suspend fun sendChatMessages(param: HashMap<String, Any>): Response<ChatItem> =
        apiService.sendChatMessages(param)

    suspend fun getProducts(productId: String) =
        apiService.getProducts(getProductFilter(productId))

    suspend fun getSessionViews(liveSessionId: String) =
        apiService.getSessionViews(getLiveSessionFilter(liveSessionId))

    suspend fun updateViewCount(param: HashMap<String, Any>) = apiService.updateViewCount(param)

    suspend fun getLikeCount(liveSessionId: String): Response<ArrayList<Any>> =
        apiService.getLikeCount(getLiveSessionFilter(liveSessionId))

    suspend fun updateLikeCount(param: HashMap<String, Any>) = apiService.updateLikeCount(param)

    suspend fun getPurchaseCount(liveSessionId: String): Response<ArrayList<Any>> =
        apiService.getPurchaseCount(getLiveSessionFilter(liveSessionId))

    suspend fun updatePurchaseCount(param: HashMap<String, Any>) = apiService.updatePurchaseCount(param)

    suspend fun getShareCount(liveSessionId: String): Response<ArrayList<Any>> =
        apiService.getShareCount(getLiveSessionFilter(liveSessionId))

    suspend fun updateShareCount(param: HashMap<String, Any>) = apiService.updateShareCount(param)

}