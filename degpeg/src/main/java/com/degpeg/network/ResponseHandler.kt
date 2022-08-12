package com.degpeg.network

import android.accounts.NetworkErrorException
import android.util.MalformedJsonException
import androidx.lifecycle.LiveDataScope
import com.degpeg.model.BaseModel
import com.degpeg.model.ErrorModel
import com.degpeg.utility.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.CoroutineExceptionHandler
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import javax.net.ssl.HttpsURLConnection

internal object ResponseHandler {

    fun handleErrorResponse(error: Throwable): String {
        return when (error) {
            is SocketTimeoutException -> "Timeout, Might be Server not responding"
            is HttpException -> {
                when (error.code()) {
                    HttpsURLConnection.HTTP_UNAUTHORIZED -> "Unauthorised User"
                    HttpsURLConnection.HTTP_FORBIDDEN -> "Forbidden"
                    HttpsURLConnection.HTTP_INTERNAL_ERROR -> "Internal Server Error"
                    HttpsURLConnection.HTTP_BAD_REQUEST -> "Bad Request"
                    else -> error.getLocalizedMessage()
                }
            }
            is JsonSyntaxException, is MalformedJsonException -> "Something Went Wrong. API is not responding properly!"
            is NetworkErrorException, is IOException -> "No Internet Connection"
            else -> error.message.toString()
        }
    }

    fun getErrorMessage(code: Int): String {
        return when (code) {
            HttpsURLConnection.HTTP_UNAUTHORIZED -> "Unauthorised User"
            HttpsURLConnection.HTTP_FORBIDDEN -> "Forbidden"
            HttpsURLConnection.HTTP_INTERNAL_ERROR -> "Internal Server Error"
            HttpsURLConnection.HTTP_BAD_REQUEST -> "Bad Request"
            else -> "Something Went Wrong"
        }
    }

    fun baseError(error: ResponseBody?): ErrorModel {
        return try {
            Gson().fromJson(error!!.charStream(), ErrorModel::class.java)
        } catch (e: Exception) {
            ErrorModel(handleErrorResponse(e), "", 500)
        }
    }

    fun <T> baseError(response: Response<T>): ErrorModel {
        return try {
            return if (response.code() == NetworkURL.RESPONSE_OK || response.code() == NetworkURL.RESPONSE_CREATED) {
                val baseModel = Gson().fromJson(response.body().toString(), BaseModel::class.java)
                baseModel?.error ?: ErrorModel("Something Went Wrong", "", response.code())
            } else {
                val errorBase =
                    Gson().fromJson(response.errorBody()?.string(), BaseModel::class.java)
                errorBase?.error ?: ErrorModel("Something Went Wrong", "", response.code())
            }
        } catch (e: Exception) {
            if (response.body() is ErrorModel) response.body() as ErrorModel
            else ErrorModel(handleErrorResponse(e), "", response.code())
        }
    }

    fun exceptionHandler(callback: ((errorMessage: String) -> Unit)? = null): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, throwable ->
            callback?.invoke(handleErrorResponse(throwable))
        }
    }

    suspend fun <T> responseParser(
        response: Response<T>,
        liveDataScope: LiveDataScope<Resource<T>>,
    ) {
        if (response.isSuccessful) {
            val result = response.body()
            if (result != null) liveDataScope.emit(Resource.success(result))
            else liveDataScope.emit(
                Resource.error(baseError(response).message, response.code())
            )
        } else {
            liveDataScope.emit(
                Resource.error(baseError(response).message, response.code())
            )
        }
    }
}