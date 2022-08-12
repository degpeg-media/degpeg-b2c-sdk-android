package com.degpeg.common

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_SECURE
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

internal abstract class BaseActivity : AppCompatActivity() {
    protected val tag: String = this::class.java.simpleName
    protected lateinit var activityLauncher: BetterActivityResult<Intent, ActivityResult>
    var portraitOrientation: Int = Configuration.ORIENTATION_PORTRAIT
    var landscapeOrientation: Int = Configuration.ORIENTATION_LANDSCAPE
    var orientation: Int = Configuration.ORIENTATION_PORTRAIT
    abstract fun initUi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orientation = resources.configuration.orientation
        activityLauncher = BetterActivityResult.registerActivityForResult(this)
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        initUi()
    }


    fun hideKeyBoard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }

    fun setSecureActivity() = window.setFlags(FLAG_SECURE, FLAG_SECURE)
    fun keepScreenOn() = window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        orientation = newConfig.orientation
    }

    protected val isPortraitMode: Boolean
        get() {
            return orientation == Configuration.ORIENTATION_PORTRAIT
        }

//    private var alertDialog: AlertDialog? = null
//
//    open fun showAlertMessage(str: String) {
//        showAlertMessage(str, null)
//    }
//
//    open fun showAlertMessage(str: String, onClickListener: DialogInterface.OnClickListener?) {
//        showAlertMessage(null, str, true, "", onClickListener)
//    }
//
//    open fun showAlertMessage(
//        title: String?, str: String, isCancelable: Boolean, positiveText: String,
//        onClickListener: DialogInterface.OnClickListener?,
//    ): AlertDialog? {
//        try {
//            if (alertDialog != null && alertDialog!!.isShowing) {
//                alertDialog!!.dismiss()
//            }
//            val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogCustom))
//                .setMessage(str).setCancelable(isCancelable)
//                .setPositiveButton(positiveText.takeIf { positiveText.isNotEmpty() }
//                    ?: getString(R.string.ok), onClickListener)
//
//            if (!title.isNullOrBlank()) builder.setTitle(getString(R.string.app_name))
//
//            alertDialog = builder.show()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return alertDialog
//    }
//
//    open fun showAlertMessage(
//        str: String,
//        isCancelable: Boolean,
//        positiveText: String,
//        negativeText: String,
//        callback: (isPositive: Boolean) -> Unit,
//    ): AlertDialog? {
//        try {
//            if (alertDialog != null && alertDialog!!.isShowing) {
//                alertDialog!!.dismiss()
//            }
//            val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogCustom))
//                .setMessage(str)
//                .setCancelable(isCancelable)
//                .setPositiveButton(positiveText.takeIf { positiveText.isNotBlank() }
//                    ?: getString(R.string.ok)) { _, _ -> callback.invoke(true) }
//                .setNegativeButton(negativeText.takeIf { negativeText.isNotBlank() }
//                    ?: getString(R.string.cancel)) { _, _ -> callback.invoke(false) }
//
////            if (!title.isNullOrBlank()) builder.setTitle(title)
//
//            alertDialog = builder.show()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return alertDialog
//    }

    private var snackbar: Snackbar? = null
    fun showSnackBar(view: View?, str: String?) {
        if (view == null) return
        if (str.isNullOrEmpty()) return
        if (snackbar != null && snackbar!!.isShownOrQueued) {
            snackbar?.dismiss()
        }
        snackbar = Snackbar.make(view, str, Snackbar.LENGTH_SHORT)
        snackbar?.show()
    }

    fun showSnackBar(str: String?) {
        val view: View = this.window.decorView
        if (str.isNullOrEmpty()) return
        if (snackbar != null) snackbar?.dismiss()
        snackbar = Snackbar.make(view, str, Snackbar.LENGTH_SHORT)
        snackbar?.show()
    }

    fun toast(str: String?) {
        if (str.isNullOrEmpty()) return
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }

//    fun openDatePickerDialog(
//        strTitle: String,
//        selectedDate: Calendar,
//        onDateSet: DatePickerDialog.OnDateSetListener,
//        isFuture: Boolean,
//        isPast: Boolean,
//    ) {
//        val calendar: Calendar = selectedDate
//        val day = calendar.get(Calendar.DAY_OF_MONTH)
//        val month = calendar.get(Calendar.MONTH)
//        val year = calendar.get(Calendar.YEAR)
//        val datePickerDialog =
//            DatePickerDialog(this, R.style.DatePickerTheme, onDateSet, year, month, day)
//        if (isFuture) datePickerDialog.datePicker.minDate = System.currentTimeMillis()
//        if (isPast) datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
//        datePickerDialog.setTitle(strTitle)
//        datePickerDialog.datePicker.firstDayOfWeek = Calendar.MONDAY
//        datePickerDialog.show()
//    }
//
//    fun openDatePickerDialog(
//        strTitle: String,
//        onDateSet: DatePickerDialog.OnDateSetListener,
//        isFuture: Boolean,
//        isPast: Boolean,
//    ) {
//        val calendar: Calendar = Calendar.getInstance()
//        openDatePickerDialog(strTitle, calendar, onDateSet, isFuture, isPast)
//    }

//    override fun onBackPressed() {
//        if (KeyboardVisibilityEvent.isKeyboardVisible(this)) {
//            KeyboardUtil.hideKeyboard(this)
//            return
//        }
//        super.onBackPressed()
//    }
//
//    private var dialog: KProgressHUD? = null
//    fun showProgressDialog() {
//        showProgressDialog(getString(R.string.please_wait))
//    }
//
//    fun showProgressDialog(msg: String?) {
//        if (dialog == null) {
//            dialog = KProgressHUD.create(this)
//                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                .setLabel(getString(R.string.please_wait).takeIf { msg.isNullOrEmpty() } ?: msg)
//                .setCancellable(false)
//                .setAnimationSpeed(2)
//                .setDimAmount(0.5f)
//        }
//        dialog!!.show()
//    }
//
//    fun hideProgressDialog() {
//        justTry { if (dialog != null) dialog!!.dismiss() }
//    }
}