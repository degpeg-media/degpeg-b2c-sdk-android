package com.degpeg.views

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmapOrNull
import com.makeramen.roundedimageview.RoundedImageView
import com.bumptech.glide.Glide
import com.degpeg.Controller
import com.degpeg.R
import com.degpeg.views.shapetext.ColorGenerator
import com.degpeg.views.shapetext.TextDrawable
import java.io.File

internal class CircularImageView : RoundedImageView {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    fun setUrl(url: String?) {
        Glide.with(context).load(url).centerCrop().placeholder(R.drawable.ic_placeholder).into(this)
    }

    fun setUrl(file: File?) {
        Glide.with(context).load(file).centerCrop().placeholder(R.drawable.ic_placeholder)
            .into(this)
    }

    fun set(res_id: Int) {
        Glide.with(this).load(res_id).into(this)
    }

    fun clear() {
        Glide.with(context).clear(this)
    }

    private var colors = ColorGenerator.MATERIAL
    private var textDrawable = TextDrawable.builder().beginConfig().fontSize(25)
        .textColor(ContextCompat.getColor(Controller.instance, R.color.textColorWhite))
        .useFont(ResourcesCompat.getFont(Controller.instance, R.font.nunito_semi_bold))
        .endConfig()

    fun setImageWithInitial(url: String?, initial: String?) {
        if (url.isNullOrEmpty() && !initial.isNullOrEmpty()) {
            setImageBitmap(textDrawable.buildRound(
                initial,
                colors.getColor(initial)
            ).toBitmapOrNull(50,50))
        } else setUrl(url)
    }
}