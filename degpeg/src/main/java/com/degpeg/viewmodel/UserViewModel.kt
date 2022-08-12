package com.degpeg.viewmodel

import androidx.lifecycle.*
import com.degpeg.model.ChannelDetail
import com.degpeg.model.SessionCategoryItem
import com.degpeg.model.UserDetail
import com.degpeg.model.VideoContentItem
import com.degpeg.network.Resource
import com.degpeg.network.ResponseHandler
import com.degpeg.network.ResponseHandler.responseParser
import com.degpeg.repository.ContentRepository
import com.degpeg.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.HashMap

internal class UserViewModel : ViewModel() {

    private val hashMap = HashMap<String, UserDetail>()

    fun fetchUser(providerId: String, callback: (user: UserDetail?) -> Unit) {
        if (hashMap.contains(providerId)) {
            callback.invoke(hashMap[providerId])
        } else {
            viewModelScope.launch {
                try {
                    val response =
                        UserRepository.getUserDetail(providerId)
                    val data = response.body()
                    if (response.isSuccessful && !data.isNullOrEmpty()) {
                        hashMap[providerId] = data.first()
                        callback.invoke(hashMap[providerId])
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun getUserDetails(providerId: String) = liveData(Dispatchers.IO) {
        try {
            responseParser(UserRepository.getUserDetail(providerId), this)
        } catch (e: Exception) {
            emit(Resource.error(ResponseHandler.handleErrorResponse(e)))
        }
    }

    fun clear() {
        instance = null
    }

    companion object {
        private var instance: UserViewModel? = null
        fun getInstance() =
            instance ?: synchronized(UserViewModel::class.java) {
                instance ?: UserViewModel().also { instance = it }
            }
    }
}