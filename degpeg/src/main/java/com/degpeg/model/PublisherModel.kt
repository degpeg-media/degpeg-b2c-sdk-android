package com.degpeg.model

import com.google.gson.annotations.SerializedName

internal data class PublisherModel(
    @SerializedName("addr_city")
    val addrCity: String,
    @SerializedName("addr_country")
    val addrCountry: String,
    @SerializedName("addr_pincode")
    val addrPincode: String,
    @SerializedName("addr_state")
    val addrState: String,
    val address: String,
    @SerializedName("contact_number")
    val contactNumber: String,
    val contentProviders: ArrayList<String>,
    val email: String,
    val id: String,
    val name: String,
    val status: String

) : BaseModel()