package com.degpeg.utility

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.text.*
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.*
import androidx.annotation.AnimRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.degpeg.Controller
import com.degpeg.R
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import kotlin.math.ln
import kotlin.math.pow

inline fun EditText.afterTextChanged(crossinline listener: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            listener(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }
    })
}

inline fun EditText.onTextChanged(crossinline listener: (s: CharSequence?, start: Int, before: Int, count: Int) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            listener(s, start, before, count)
        }
    })
}

val gson = GsonBuilder().disableHtmlEscaping().create()
fun Any?.toJSONObject(): JSONObject {
    return JSONObject(this.toJson())
}

fun Any?.toJson(): String = gson.toJson(this)

fun Any?.toJsonExcludeExpose(): String {
    val gson = GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .create()
    return gson.toJson(this)
}

fun <T> String?.fromJson(classOfT: Class<T>): T = Gson().fromJson(this, classOfT)

fun <T> String?.fromJsonList(): List<T> {
    return Gson().fromJson(this, object : TypeToken<List<T>>() {}.type) ?: emptyList<T>()
}

/**
 * Enable/Disable EditText to editable
 * */
fun EditText.setEditable(enable: Boolean) {
    isFocusable = enable
    isFocusableInTouchMode = enable
    isClickable = enable
    isCursorVisible = enable
}

/*
* Make EditText Scrollable inside scrollview
* */
@SuppressLint("ClickableViewAccessibility")
fun EditText.makeScrollableInScrollView() {
    setOnTouchListener(View.OnTouchListener { v, event ->
        if (hasFocus()) {
            v.parent.requestDisallowInterceptTouchEvent(true)
            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_SCROLL -> {
                    v.parent.requestDisallowInterceptTouchEvent(false)
                    return@OnTouchListener true
                }
            }
        }
        false
    })
}

/*
* Execute block if OS version is greater or equal Lolipop(21)
* */
inline fun lollipopAndAbove(block: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        block()
    }
}

/*
* Execute block if OS version is greater than or equal Naugat(24)
* */
inline fun nougatAndAbove(block: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        block()
    }
}

/**
 * Check internet connection.
 * */
inline fun Context.withNetwork(block: () -> Unit, blockError: () -> Unit) {
    val connectivityManager = this
        .getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    connectivityManager?.let { it ->
        val netInfo = it.activeNetworkInfo
        netInfo?.let {
            if (netInfo.isConnected)
                block()
            else
                blockError()
        }
    }
}

/*
* Execute block into try...catch
* */
inline fun <T> justTry(tryBlock: () -> T) = try {
    tryBlock()
} catch (e: Throwable) {
    e.printStackTrace()
}

fun Context.openPdfFromUrl(pdfUrl: String?) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl))
    startActivity(browserIntent)
}

fun Fragment.openPdfFromUrl(pdfUrl: String?) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl))
    startActivity(browserIntent)
}


inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}

fun Intent.getInt(key: String, defaultValue: Int = 0): Int {
    return extras?.getInt(key, defaultValue) ?: defaultValue
}

fun Intent.getString(key: String, defaultValue: String = ""): String {
    return extras?.getString(key, defaultValue) ?: defaultValue
}

/**
 * Hide/Show view with scale animation
 * */
fun View.setVisibilityWithScaleAnim(visibility: Int) {
    this.clearAnimation()
    this.visibility = View.VISIBLE
    val scale = if (visibility == View.GONE)
        0f
    else
        1f

    val scaleDown = ObjectAnimator.ofPropertyValuesHolder(
        this,
        PropertyValuesHolder.ofFloat("scaleX", scale),
        PropertyValuesHolder.ofFloat("scaleY", scale)
    )
    scaleDown.duration = 300
    scaleDown.interpolator = DecelerateInterpolator()
    scaleDown.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            super.onAnimationEnd(animation)
            this@setVisibilityWithScaleAnim.visibility = visibility
        }
    })
    scaleDown.start()
}

fun View.setAnimationWithVisibility(@AnimRes animationRes: Int, visibility: Int) {
    setVisibility(visibility)
    clearAnimation()
    val viewAnim = AnimationUtils.loadAnimation(context, animationRes)
    animation = viewAnim
    viewAnim.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {
        }

        override fun onAnimationEnd(animation: Animation?) {
            setVisibility(visibility)
        }

        override fun onAnimationStart(animation: Animation?) {
            setVisibility(View.VISIBLE)
        }
    })
    // viewAnim.start()
}

fun Context.getAppVersionName(): String {
    return packageManager.getPackageInfo(packageName, 0).versionName
}

fun Context.showToast(
    message: String?,
    duration: Int = Toast.LENGTH_SHORT,
) {
    if (!message.isNullOrEmpty())
        Toast.makeText(this, message, duration).show()
}

fun SpannableString.setClickableSpan(
    start: Int,
    end: Int, @ColorInt color: Int,
    block: (view: View?) -> Unit,
) {
    setSpan(object : ClickableSpan() {
        override fun onClick(view: View) {
            block(view)
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.isUnderlineText = false // set to false to remove underline
        }

    }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    // Set Color Span
    setSpan(ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
}

/*
* Toggle integer value, from 0->1 or 1->0
* */
fun Int.toggle() = if (this == 1) 0 else 1

/*
* Return true if view is visible otherwise return false
* */
fun View.isVisible() = visibility == View.VISIBLE
fun View.isGone(): Boolean = visibility == View.GONE
fun View.isInvisible(): Boolean = visibility == View.INVISIBLE

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun Int?.nullSafe(defaultValue: Int = 0): Int {
    return this ?: defaultValue
}

fun Float?.nullSafe(defaultValue: Float = 0f): Float {
    return this ?: defaultValue
}

fun Double?.nullSafe(defaultValue: Double = 0.0): Double {
    return this ?: defaultValue
}


@SuppressWarnings("deprecation")
fun String?.fromHtml(): Spanned {
    if (this == null)
        return SpannableString("")
    else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        // FROM_HTML_MODE_LEGACY is the behaviour that was used for versions below android N
        // we are using this flag to give a consistent behaviour
        return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        return Html.fromHtml(this)
    }
}

/**
 * Return ActionBar height
 * */
fun Activity.getActionBarHeight(): Int {
    val tv = TypedValue()
    return if (theme.resolveAttribute(android.R.attr.actionBarSize, tv, true))
        TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
    else 0
}

fun View.measureWidthHeight(onCompleteMeasure: (width: Int, height: Int) -> Unit) {
    viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            viewTreeObserver.removeOnPreDrawListener(this)
            onCompleteMeasure.invoke(measuredWidth, measuredHeight)
            return true
        }
    })
}

fun Drawable.setTintColor(@ColorInt colorTint: Int): Drawable {
    colorFilter = PorterDuffColorFilter(colorTint, PorterDuff.Mode.SRC_ATOP)
    return this
}

fun SearchView.setHintColor(@ColorInt hintColor: Int) {
    (findViewById<EditText>(androidx.appcompat.R.id.search_src_text)).setHintTextColor(hintColor)
}

fun String?.float(defaultValue: Float = 0f): Float {
    return if (isNullOrEmpty()) {
        defaultValue
    } else {
        try {
            this.toFloat().nullSafe()
        } catch (e: Exception) {
            e.printStackTrace()
            defaultValue
        }
    }
}

/**
 * Convert string to int, If string is empty or null return default value
 * */
fun String?.int(defaultValue: Int = 0): Int {
    return if (isNullOrEmpty()) {
        defaultValue
    } else {
        try {
            this.toInt().nullSafe()
        } catch (e: Exception) {
            e.printStackTrace()
            defaultValue
        }
    }
}

/**
 * Convert string to double, If string is empty or null return default value
 * */
fun String?.toDoubleOrDefaultValue(defaultValue: Double = 0.0): Double {
    return if (isNullOrEmpty()) {
        defaultValue
    } else {
        try {
            this?.toDouble().nullSafe()
        } catch (e: Exception) {
            e.printStackTrace()
            defaultValue
        }
    }
}

/**
 * Return Tag of view as string
 * */
fun View.getStringTag(): String = if (tag == null) "" else tag.toString()

fun RadioGroup.getCheckedButtonText(): String {
    return if (checkedRadioButtonId != -1)
        findViewById<RadioButton>(checkedRadioButtonId).text.toString()
    else
        ""
}


fun ImageView.loadImage(
    imageUrl: String?,
    @DrawableRes placeholder: Int = R.drawable.ic_placeholder
) {
    justTry {
        Glide.with(Controller.instance).load(imageUrl).placeholder(placeholder).into(this)
    }
}

fun ImageView.loadImageWithError(
    imageUrl: String?,
    @DrawableRes placeholder: Int = 0,
) {
    justTry {
        Glide.with(Controller.instance).load(imageUrl).placeholder(placeholder)
            .into(this)
    }
}

fun ImageView.loadImage(
    images: List<String>?,
    @DrawableRes placeholder: Int = 0,
) {
    justTry {
        Glide.with(Controller.instance).clear(this)
        if (images == null || images.isEmpty()) return
        Glide.with(Controller.instance).load(images[0]).placeholder(placeholder).into(this)
    }
}

fun ImageView.loadImageSmall(
    images: List<String>?,
    @DrawableRes placeholder: Int = 0,
) {
    justTry {
        Glide.with(Controller.instance).clear(this)
        if (images == null || images.isEmpty()) return
        Glide.with(Controller.instance).load(images[0]).override(250, 250).placeholder(placeholder)
            .into(this)
    }
}

fun ImageView.loadResource(res: Int) {
    justTry { Glide.with(Controller.instance).load(res).into(this) }
}


/** multipart*/
fun EditText.getTrimText(): String = text.toString().trim()

fun EditText.toRequestBody(): RequestBody {
    return getTrimText().requestBody()
}

fun String.requestBody(): RequestBody {
    return toRequestBody(("text/plain").toMediaTypeOrNull())
}

fun File.toMultipartBody(parameterName: String): MultipartBody.Part {
    return MultipartBody.Part.createFormData(
        parameterName,
        name,
        this.asRequestBody("image/*".toMediaType())
    )
}

fun File.toMultipartBody(type: MediaType, parameterName: String): MultipartBody.Part {
    return MultipartBody.Part.createFormData(
        parameterName,
        name,
        this.asRequestBody(type)
    )
}

fun TextView.setColorSpan(mainText: String, subtext: String, color: Int) {
    setText(mainText, TextView.BufferType.SPANNABLE)
    val str = text as Spannable
    val i = mainText.indexOf(subtext)
    str.setSpan(
        ForegroundColorSpan(color),
        i,
        i + subtext.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
}

fun TextView.setDrawableColor(@ColorRes color: Int) {
    compoundDrawables.filterNotNull().forEach {
        it.colorFilter =
            PorterDuffColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN)
    }
}

fun TextView.textColor(color: Int) {
    setTextColor(ContextCompat.getColor(context, color))
}

fun View.margin(
    left: Float? = null,
    top: Float? = null,
    right: Float? = null,
    bottom: Float? = null
) {
    layoutParams<ViewGroup.MarginLayoutParams> {
        left?.run { leftMargin = dpToPx(this) }
        top?.run { topMargin = dpToPx(this) }
        right?.run { rightMargin = dpToPx(this) }
        bottom?.run { bottomMargin = dpToPx(this) }
    }
}

inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) {
    if (layoutParams is T) block(layoutParams as T)
}

fun View.dpToPx(dp: Float): Int = context.dpToPx(dp)
fun Context.dpToPx(dp: Float): Int =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()

fun Activity.getStatusBarHeight(): Int {
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resourceId > 0) resources.getDimensionPixelSize(resourceId)
    else Rect().apply { window.decorView.getWindowVisibleDisplayFrame(this) }.top
}

internal fun Long.formatAsViewCount(): String {
    if (this < 1000) return "" + this
    val exp = (ln(this.toDouble()) / ln(1000.0)).toInt()
    return String.format("%.1f %c", this / 1000.0.pow(exp.toDouble()), "kMGTPE"[exp - 1])
}

internal fun Context.shareVideoLink(shareText:String, link:String) {
    val sendIntent = Intent()
    sendIntent.action = Intent.ACTION_SEND
    sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(applicationInfo.labelRes))
    sendIntent.putExtra(Intent.EXTRA_TEXT, "$shareText\n\n$link")
    sendIntent.type = "text/plain"
    startActivity(Intent.createChooser(sendIntent, "Share link:"))
}
