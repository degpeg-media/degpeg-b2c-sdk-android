package com.degpeg.model

import android.os.Parcel
import android.os.Parcelable
import com.degpeg.utility.Parcelable.parcelableCreator
import com.google.gson.annotations.SerializedName

internal data class UserDetail(
    @SerializedName("addr_city")
    val addrCity: String?,
    @SerializedName("addr_country")
    val addrCountry: String?,
    @SerializedName("addr_pincode")
    val addrPincode: String?,
    @SerializedName("addr_state")
    val addrState: String?,
    val address: String?,
    val appId: String?,
    val avatar: String?,
    val consumerUserId: String?,
    val contentProviderId: String?,
    val contentPublisherId: String?,
    val displayPicture: String?,
    val email: String?,
    val firstName: String?,
    val gender: String?,
    val id: String?,
    val imgUrl: String?,
    val influencerId: String?,
    val isEmailVerified: Boolean,
    val lastName: String?,
    val message: String?,
    val mobile: String?,
    val name: String?,
    @SerializedName("organization_name")
    val organizationName: String?,
    val otp: String?,
    val password: String?,
    val phone: String?,
    @SerializedName("pref_categories")
    val prefCategories: List<String>?,
    val roleAssignedBy: String?,
    val roles: List<String>?,
    val secretKey: String?,
    @SerializedName("sign_in_through")
    val signInThrough: String?,
    val status: String?,
    @SerializedName("website_link")
    val websiteLink: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        addrCity = parcel.readString(),
        addrCountry = parcel.readString(),
        addrPincode = parcel.readString(),
        addrState = parcel.readString(),
        address = parcel.readString(),
        appId = parcel.readString(),
        avatar = parcel.readString(),
        consumerUserId = parcel.readString(),
        contentProviderId = parcel.readString(),
        contentPublisherId = parcel.readString(),
        displayPicture = parcel.readString(),
        email = parcel.readString(),
        firstName = parcel.readString(),
        gender = parcel.readString(),
        id = parcel.readString(),
        imgUrl = parcel.readString(),
        influencerId = parcel.readString(),
        isEmailVerified = parcel.readByte() != 0.toByte(),
        lastName = parcel.readString(),
        message = parcel.readString(),
        mobile = parcel.readString(),
        name = parcel.readString(),
        organizationName = parcel.readString(),
        otp = parcel.readString(),
        password = parcel.readString(),
        phone = parcel.readString(),
        prefCategories = parcel.createStringArrayList(),
        roleAssignedBy = parcel.readString(),
        roles = parcel.createStringArrayList(),
        secretKey = parcel.readString(),
        signInThrough = parcel.readString(),
        status = parcel.readString(),
        websiteLink = parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(addrCity)
        parcel.writeString(addrCountry)
        parcel.writeString(addrPincode)
        parcel.writeString(addrState)
        parcel.writeString(address)
        parcel.writeString(appId)
        parcel.writeString(avatar)
        parcel.writeString(consumerUserId)
        parcel.writeString(contentProviderId)
        parcel.writeString(contentPublisherId)
        parcel.writeString(displayPicture)
        parcel.writeString(email)
        parcel.writeString(firstName)
        parcel.writeString(gender)
        parcel.writeString(id)
        parcel.writeString(imgUrl)
        parcel.writeString(influencerId)
        parcel.writeByte(if (isEmailVerified) 1 else 0)
        parcel.writeString(lastName)
        parcel.writeString(message)
        parcel.writeString(mobile)
        parcel.writeString(name)
        parcel.writeString(organizationName)
        parcel.writeString(otp)
        parcel.writeString(password)
        parcel.writeString(phone)
        parcel.writeStringList(prefCategories)
        parcel.writeString(roleAssignedBy)
        parcel.writeStringList(roles)
        parcel.writeString(secretKey)
        parcel.writeString(signInThrough)
        parcel.writeString(status)
        parcel.writeString(websiteLink)
    }

    override fun describeContents(): Int {
        return 0
    }


    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::UserDetail)
    }

    fun getNonNullName(): String {
        if (!name.isNullOrEmpty()) return name
        if (!firstName.isNullOrEmpty() && !lastName.isNullOrEmpty()) return "$firstName $lastName"
        if (!firstName.isNullOrEmpty()) return firstName
        return getNameFromEmail()
    }

    fun getNameFromEmail(): String {
        return email?.split("@")?.get(0) ?: ""
    }

    fun getInitials(): String {
        val str = getNonNullName()
        if (str.isNullOrEmpty()) return "D"
       return str.split(' ')
            .mapNotNull { it.firstOrNull()?.toString() }
            .reduce { acc, s -> acc + s }.uppercase()
    }

}