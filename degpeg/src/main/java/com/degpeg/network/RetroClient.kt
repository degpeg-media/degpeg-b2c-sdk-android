package com.degpeg.network

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.degpeg.BuildConfig
import com.degpeg.Controller
import com.degpeg.b2csdk.DegpegSDKProvider
import com.degpeg.network.NetworkURL.BEARER
import com.degpeg.network.NetworkURL.HEADER_AUTHORIZATION
import com.degpeg.network.NetworkURL.RES_BLOCKED
import com.degpeg.network.NetworkURL.RES_UNAUTHORISED
import com.degpeg.utility.Constants
import com.degpeg.utility.LocalDataHelper.authToken
import com.degpeg.utility.Log
import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

internal object RetroClient {
    private var retrofit: Retrofit? = null
    private val interceptor: Interceptor
        get() {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY.takeIf { BuildConfig.DEBUG }
                ?: HttpLoggingInterceptor.Level.NONE)
            return logging
        }

    private val builder = OkHttpClient.Builder()
        .connectionSpecs(listOf(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS))
        .connectTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)

    private val headerInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
        if (!authToken.isNullOrEmpty()) {
            Log.d("$HEADER_AUTHORIZATION: $BEARER $authToken")
            request.addHeader(HEADER_AUTHORIZATION, "$BEARER $authToken")
        }
        chain.proceed(request.build())
    }

//    private val apiTokenInterceptor = Interceptor { chain ->
//        var request = chain.request()
//        if (AppHelper.isLogin()) {
//            val url: HttpUrl = request.url.newBuilder()
//                .addQueryParameter(QUERY_AUTHORIZATION, AppHelper.getApiToken()).build()
//            request = request.newBuilder().url(url).build()
//        }
//        chain.proceed(request)
//    }

    private val forbiddenInterceptor = Interceptor { chain ->
        val request = chain.request()
        val response = chain.proceed(request)
        when (response.code) {
            RES_UNAUTHORISED -> manageForBiddenRequest()
//            RES_BLOCKED -> manageBlockRequest()
        }
        response
    }

    private fun manageForBiddenRequest() {
        val intent = Intent(Constants.ACTION_FOR_BIDDEN_RESPONSE)
        LocalBroadcastManager.getInstance(Controller.instance).sendBroadcast(intent)
    }

    private val client =
        builder.addInterceptor(interceptor)
            .addInterceptor(headerInterceptor)
//            .addInterceptor(apiTokenInterceptor)
//            .addInterceptor(forbiddenInterceptor)
            .build()

    private val retrofitInstance: Retrofit
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(NetworkURL.ROOT_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }

    val apiService: ApiService = retrofitInstance.create(ApiService::class.java)
}