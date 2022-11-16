package com.degpeg.network

import com.degpeg.model.*
import retrofit2.Response
import retrofit2.http.*

// role => publisher
// role => provider 

internal interface ApiService {

    @POST("users/auth/token")
    suspend fun getAuthToken(@Body params: HashMap<String, Any>?): Response<AuthModel>

    @GET("content-publishers/{publisherId}")
    suspend fun getContentProviders(@Path("publisherId") publisherId: String): Response<PublisherModel>

    @GET("content-providers/{providerId}/live-sessions")
    suspend fun getContentProvidersWiseVideos(
        @Path("providerId") providerId: String,
        @QueryMap filterMap: HashMap<String, Any>
    ): Response<ProviderContentModel>

    @GET("channels/{channelId}")
    suspend fun getChannelDetails(@Path("channelId") channelId: String): Response<ChannelDetail>

    @GET("live-session-categories/")
    suspend fun getCategories(): Response<ArrayList<SessionCategoryItem>>

    @GET("live-sessions/{sessionId}")
    suspend fun getSessionDetail(@Path("sessionId") sessionId: String): Response<VideoContentItem>

    @GET("users")
    suspend fun getUserDetail(@QueryMap filterMap: HashMap<String, Any>): Response<ArrayList<UserDetail>>

    @GET("chat-messages")
    suspend fun getChatMessages(@QueryMap filterMap: HashMap<String, Any>): Response<ArrayList<ChatItem>>

    @POST("chat-messages")
    suspend fun sendChatMessages(@Body param: HashMap<String, Any>): Response<ChatItem>

    @GET("views")
    suspend fun getSessionViews(@QueryMap filterMap: HashMap<String, Any>): Response<ArrayList<SessionUserModel>>

    @POST("views")
    suspend fun updateViewCount(@Body param: HashMap<String, Any>): Response<SessionUserModel>

    @GET("products")
    suspend fun getProducts(@QueryMap filterMap: HashMap<String, Any>): Response<ArrayList<ProductModel>>

    @GET("likes")
    suspend fun getLikeCount(@QueryMap filterMap: HashMap<String, Any>): Response<ArrayList<Any>>

    @POST("likes")
    suspend fun updateLikeCount(@Body filterMap: HashMap<String, Any>): Response<CountUpdateModel>

    @GET("purchases")
    suspend fun getPurchaseCount(@QueryMap filterMap: HashMap<String, Any>): Response<ArrayList<Any>>

    @POST("purchases")
    suspend fun updatePurchaseCount(@Body filterMap: HashMap<String, Any>): Response<CountUpdateModel>

    @GET("shares")
    suspend fun getShareCount(@QueryMap filterMap: HashMap<String, Any>): Response<ArrayList<Any>>

    @POST("shares")
    suspend fun updateShareCount(@Body filterMap: HashMap<String, Any>): Response<CountUpdateModel>

}