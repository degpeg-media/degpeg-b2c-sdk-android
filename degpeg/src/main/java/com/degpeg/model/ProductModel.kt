package com.degpeg.model

import android.net.Uri
import com.google.gson.annotations.SerializedName

internal data class ProductModel(
    val contentProviderId: String,
    val currency: String,
    val discount: String,
    val frequentlyUsed: Boolean,
    val id: String,
    @SerializedName("image_url")
    val imageUrl: String,
    val name: String,
    val price: String,
    @SerializedName("purchase_link")
    val purchaseLink: String,
    val quantity: String
){
    fun getPurchaseLinkUri(): Uri? {
        if(purchaseLink.isEmpty())return null
        var uri = Uri.parse(purchaseLink)
        if(uri.scheme.isNullOrEmpty())
            uri = Uri.parse("http://$purchaseLink")
        return uri
    }
}