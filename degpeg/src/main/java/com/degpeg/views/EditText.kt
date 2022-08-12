package com.degpeg.views

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.ViewParent
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.textfield.TextInputLayout
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

internal class EditText : AppCompatEditText {
    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
    }

    override fun onTextChanged(
        text: CharSequence,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        try {
            val inputLayout = findTextInputLayoutParent(this.parent, 3)
            if (inputLayout != null) {
                inputLayout.error = ""
                inputLayout.isErrorEnabled = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun clear() {
        setText("")
    }

    val string: String
        get() {
            val editable = super.getText() ?: return ""
            return editable.toString()
        }

    fun trim(): String {
        val editable = super.getText() ?: return ""
        return editable.toString().trim { it <= ' ' }
    }

    override fun length(): Int {
        return string.length
    }

    fun setError(res: Int) {
        super.setError(context.getString(res))
    }

    val requestBody: RequestBody
        get() = string.toRequestBody(MEDIA_TYPE_TEXT)

    fun isChanged(name: String?): Boolean {
        return if (name == null) false else trim() != name
    }

    val isEmpty: Boolean
        get() = TextUtils.isEmpty(trim())

    companion object {
        val MEDIA_TYPE_TEXT: MediaType = "text/plain".toMediaType()
        val MEDIA_TYPE_IMAGE: MediaType = ("image/*").toMediaType()
        val MEDIA_TYPE_VIDEO: MediaType = ("video/*").toMediaType()
        val MEDIA_TYPE_PDF: MediaType = ("application/pdf").toMediaType()
        private fun findTextInputLayoutParent(view: ViewParent?, maxDepth: Int): TextInputLayout? {
            if (view == null) return null
            val parent = view.parent ?: return null
            if (parent is TextInputLayout) {
                return parent
            } else if (maxDepth > 0) {
                return findTextInputLayoutParent(parent, maxDepth - 1)
            }
            return null
        }
    }
}