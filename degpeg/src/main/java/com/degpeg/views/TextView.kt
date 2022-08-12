package com.degpeg.views

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat

internal class TextView : AppCompatTextView {
    constructor(context: Context?) : super(context!!)

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context!!, attrs, defStyleAttr)

    val string: String
        get() {
            val charSequence = super.getText() ?: return ""
            return charSequence.toString()
        }

//    override fun setText(charSequence: CharSequence, type: BufferType) {
//        var text = charSequence
//        if (text.isNotEmpty()) {
//            text = text[0].toString().uppercase() + text.subSequence(1, text.length)
//        }
//        super.setText(text, type)
//    }

    fun setEndIcon(resInt: Int){
        setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, resInt), null)
    }
}