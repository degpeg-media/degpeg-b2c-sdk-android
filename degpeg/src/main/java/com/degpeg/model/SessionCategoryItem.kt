package com.degpeg.model

import com.google.gson.annotations.SerializedName

internal data class SessionCategoryItem(
    @SerializedName("categoryImage_url")
    val categoryImageUrl: String,
    val description: String,
    val id: String,
    val name: String
)