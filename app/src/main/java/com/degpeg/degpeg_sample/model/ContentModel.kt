package com.degpeg.degpeg_sample.model

import androidx.annotation.DrawableRes

data class ContentModel(
    val name: String,
    val price: String,
    @DrawableRes val image: Int,
)