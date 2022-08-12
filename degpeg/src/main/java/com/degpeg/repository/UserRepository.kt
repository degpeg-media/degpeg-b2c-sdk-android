package com.degpeg.repository

import com.degpeg.model.UserDetail
import com.degpeg.network.RetroClient.apiService
import org.json.JSONObject
import retrofit2.Response

internal object UserRepository {

    suspend fun getUserDetail(contentProviderId: String): Response<ArrayList<UserDetail>> {
        val jsonObject = JSONObject().put(
            "where",
            JSONObject().put("contentProviderId", contentProviderId)
        )
        val filterMap =
            hashMapOf<String, Any>(Pair("filter", jsonObject.toString()))
        return apiService.getUserDetail(filterMap)
    }
}