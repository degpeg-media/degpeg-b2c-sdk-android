package com.degpeg.repository

import com.degpeg.model.AuthModel
import com.degpeg.network.ResponseHandler
import com.degpeg.network.RetroClient.apiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

internal object ContentRepository {

    fun getAuthToken(
        param: HashMap<String, Any>,
        dispatcher: CoroutineContext,
        responseCallback: (response: Response<AuthModel>) -> Unit
    ): Job {
        return CoroutineScope(dispatcher).launch {
            val response = apiService.getAuthToken(param)
            responseCallback.invoke(response)
        }
    }

    suspend fun getContentProviders(publisherId: String) =
        apiService.getContentProviders(publisherId)

    suspend fun getContentProvidersWiseVideos(providerId: String, filterMap: HashMap<String, Any>) =
        apiService.getContentProvidersWiseVideos(providerId, filterMap)


    suspend fun getChannelDetails(channelId: String) = apiService.getChannelDetails(channelId)

    suspend fun getCategories() = apiService.getCategories()

    suspend fun getSessionDetail(sessionId:String) = apiService.getSessionDetail(sessionId)
}