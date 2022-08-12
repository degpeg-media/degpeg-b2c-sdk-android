package com.degpeg.model

import android.os.Parcel
import android.os.Parcelable
import com.degpeg.utility.Parcelable.parcelableCreator
import com.google.gson.annotations.SerializedName

internal data class VideoContentItem(
    @SerializedName("banner_url")
    val bannerUrl: String,
    val channelIds: List<String>?,
    val contentProviderId: String,
    val createdAt: String,
    val ctaIds: List<String>?,
    @SerializedName("date_time")
    val dateTime: String,
    val description: String,
    val duration: String,
    val id: String,
    val liveSessionCategory: LiveSessionCategory?,
    val liveSessionCategoryId: String,
    val name: String,
    val products: List<String>?,
    val sessionDataId: String,
    @SerializedName("session_pass_code")
    val sessionPassCode: String,
    val sessionType: String,
    val status: String,
    @SerializedName("stream_key")
    val streamKey: String,
    val url: String,
    @SerializedName("web_banner_url")
    val webBannerUrl: String,
    @SerializedName("web_video_url")
    val webVideoUrl: String,
    var userDetail: UserDetail? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().orEmpty(),
        parcel.createStringArrayList(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.createStringArrayList(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readParcelable(LiveSessionCategory::class.java.classLoader),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.createStringArrayList(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readParcelable(UserDetail::class.java.classLoader),
    )

    fun isLive(): Boolean {
        return status.equals("live", true)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(bannerUrl)
        parcel.writeStringList(channelIds)
        parcel.writeString(contentProviderId)
        parcel.writeString(createdAt)
        parcel.writeStringList(ctaIds)
        parcel.writeString(dateTime)
        parcel.writeString(description)
        parcel.writeString(duration)
        parcel.writeString(id)
        parcel.writeParcelable(liveSessionCategory, flags)
        parcel.writeString(liveSessionCategoryId)
        parcel.writeString(name)
        parcel.writeStringList(products)
        parcel.writeString(sessionDataId)
        parcel.writeString(sessionPassCode)
        parcel.writeString(sessionType)
        parcel.writeString(status)
        parcel.writeString(streamKey)
        parcel.writeString(url)
        parcel.writeString(webBannerUrl)
        parcel.writeString(webVideoUrl)
        parcel.writeParcelable(userDetail, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::VideoContentItem)
    }
}