package com.degpeg.viewmodel

import androidx.lifecycle.*
import com.degpeg.model.ChannelDetail
import com.degpeg.model.SessionCategoryItem
import com.degpeg.model.VideoContentItem
import com.degpeg.network.Resource
import com.degpeg.network.ResponseHandler
import com.degpeg.network.ResponseHandler.responseParser
import com.degpeg.repository.ContentRepository
import com.degpeg.utility.DateTimeUtil.UTC_FORMAT
import com.degpeg.utility.DateTimeUtil.toDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

internal class ContentViewModel : ViewModel() {

    var mVideoList = MutableLiveData<ArrayList<VideoContentItem>?>()
    var mTopVideoList = MediatorLiveData<ArrayList<VideoContentItem>>()
    var mUpcomingVideoList = MediatorLiveData<ArrayList<VideoContentItem>>()
    var mTrendingVideoList = MediatorLiveData<ArrayList<VideoContentItem>>()

    init {
        mTopVideoList.addSource(mVideoList) { setTop5Data(it) }
        mUpcomingVideoList.addSource(mVideoList) { setUpcomingData(it) }
        mTrendingVideoList.addSource(mVideoList) { setTrendingData(it) }
    }

    fun updateList(videoList: ArrayList<VideoContentItem>) {
        mVideoList.postValue(null)
        mVideoList.postValue(videoList)
    }

    /**
     * For Trending section : for now display all video except planned and scheduled
     * */
    private fun setTrendingData(arrayList: ArrayList<VideoContentItem>?) {
        if (arrayList == null) return
        val list = ArrayList<VideoContentItem>()
        list.addAll(
            arrayList.filter {
                !it.status.equals("planned", true) &&
                        !it.status.equals("scheduled", true)
            }.sortedByDescending { it.createdAt.toDate(UTC_FORMAT) }
                .sortedByDescending { it.isLive() }
        )
        mTrendingVideoList.postValue(list)
    }

    /**
     * Upcoming Section: filter videos with status “planned” or “scheduled”
     * */
    private fun setUpcomingData(arrayList: ArrayList<VideoContentItem>?) {
        if (arrayList == null) return
        val list = ArrayList<VideoContentItem>()
        list.addAll(
            arrayList.filter {
                it.status.equals("planned", true) ||
                        it.status.equals("scheduled", true)
            }
        )
        mUpcomingVideoList.postValue(list)
    }

    /**
     * For Top Section: First 5 video with status “live” and if live not available check for “completed”
     * */
    private fun setTop5Data(arrayList: ArrayList<VideoContentItem>?) {
        if (arrayList == null) return
        val list = ArrayList<VideoContentItem>()
        list.addAll(arrayList.filter { it.status.equals("Live", true) }
            .sortedByDescending { it.createdAt.toDate(UTC_FORMAT) })
        if (list.size < 5) {
            val remainingCount = 5 - list.size
            list.addAll(
                arrayList.filter { it.status.equals("completed", true) }
                    .sortedByDescending { it.createdAt.toDate(UTC_FORMAT) }.take(remainingCount)
            )
        }

        list.forEachIndexed { index, videoContentItem ->
            run {
                viewModelScope.launch {
                    try {
                        UserViewModel.getInstance().fetchUser(videoContentItem.contentProviderId) {
                            videoContentItem.userDetail = it
                            mTopVideoList.value?.set(index, videoContentItem)
                        }
//                        val response =
//                            UserRepository.getUserDetail(videoContentItem.contentProviderId)
//                        val data = response.body()
//                        if (response.isSuccessful && !data.isNullOrEmpty()) {
//                            videoContentItem.userDetail = data.first()
//                            mTopVideoList.value?.set(index, videoContentItem)
//                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            mTopVideoList.postValue(list)
        }
    }

    /**
     * Get Details of given Content Publisher
     * */
    fun getContentProviders(publisherId: String) = liveData(Dispatchers.IO) {
        try {
            responseParser(ContentRepository.getContentProviders(publisherId), this)
        } catch (e: Exception) {
            emit(Resource.error(ResponseHandler.handleErrorResponse(e)))
        }
    }

    /**
     *  Get Video List for each Content Provider
     * */
    fun getContentProvidersWiseVideos(
        providerIds: ArrayList<String>,
        filterMap: HashMap<String, Any>
    ) = liveData(Dispatchers.IO) {
        try {
            val flowData = flow {
                providerIds.forEach() {
                    val response =
                        ContentRepository.getContentProvidersWiseVideos(it, filterMap)
                    val data = response.body()
                    if (response.isSuccessful && data != null) {
                        emit(data)
                    }
                }
            }

            val list = ArrayList<VideoContentItem>()
            flowData.collect { value ->
                list.addAll(value)
            }
            if (list.isNotEmpty()) {
                this@liveData.emit(Resource.success(data = list))
            } else {
                emit(Resource.error("No Data Found"))
            }
        } catch (e: Exception) {
            emit(Resource.error(ResponseHandler.handleErrorResponse(e)))
        }
    }

    /**
     * Filter all The videos who has channel "Stream To Website"
     * Added channelMap for reduce api calling, Use the existing data if already available in hashmap else call api
     * */
    fun getStreamToWebData(data: ArrayList<VideoContentItem>?) = liveData(Dispatchers.IO) {
        try {
            val channelMap = HashMap<String, ChannelDetail?>()
            val flowData = flow {
                data?.forEach { videoData ->
                    videoData.channelIds?.forEach { channelId ->
                        if (channelMap.contains(channelId)) {
                            if (channelMap[channelId]?.name == "Stream To Website") {
                                emit(videoData)
                            }
                        } else {
                            val response = ContentRepository.getChannelDetails(channelId)
                            val channelDetail = response.body()
                            channelMap[channelId] = channelDetail
                            if (response.isSuccessful && channelDetail != null) {
                                if (channelDetail.name == "Stream To Website") {
                                    emit(videoData)
                                }
                            }
                        }
                    }
                }
            }

            val list = ArrayList<VideoContentItem>()
            flowData.collect { value ->
                list.add(value)
            }
            if (list.isNotEmpty()) {
                this@liveData.emit(Resource.success(data = list))
            } else {
                emit(Resource.error("No Data Found"))
            }
        } catch (e: Exception) {
            emit(Resource.error(ResponseHandler.handleErrorResponse(e)))
        }
    }

    /**
     * Get All Categories
     * */
    private var mSessionCategoryList = MutableLiveData<ArrayList<SessionCategoryItem>>()
    fun getSessionCategories(): MutableLiveData<ArrayList<SessionCategoryItem>> {
        if (mSessionCategoryList.value.isNullOrEmpty()) {
            viewModelScope.launch {
                try {
                    val response = ContentRepository.getCategories()
                    val data = response.body()
                    if (response.isSuccessful && !data.isNullOrEmpty()) {
                        mSessionCategoryList.postValue(response.body())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return mSessionCategoryList
    }

    companion object {
        private var instance: ContentViewModel? = null
        fun getInstance() =
            instance ?: synchronized(ContentViewModel::class.java) {
                instance ?: ContentViewModel().also { instance = it }
            }
    }

    override fun onCleared() {
        super.onCleared()
        UserViewModel.getInstance().clear()
    }
}