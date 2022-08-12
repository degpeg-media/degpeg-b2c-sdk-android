package com.degpeg.model

import android.os.Parcel
import android.os.Parcelable
import com.degpeg.utility.Parcelable.parcelableCreator
import com.google.gson.annotations.SerializedName

internal data class LiveSessionCategory(
    @SerializedName("categoryImage_url")
    val categoryImageUrl: String,
    val description: String,
    val id: String,
    val name: String
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(categoryImageUrl)
        parcel.writeString(description)
        parcel.writeString(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::LiveSessionCategory)
    }

}