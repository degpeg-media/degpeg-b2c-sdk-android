package com.degpeg.ui.bottomsheet

import android.app.Dialog
import android.content.DialogInterface
import android.content.DialogInterface.OnShowListener
import android.content.res.Resources
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.degpeg.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.math.abs

internal abstract class BaseBottomSheet : BottomSheetDialogFragment(), OnShowListener {
    var bottomSheet: FrameLayout? = null
    var bottomSheetBehavior: BottomSheetBehavior<*>? = null

    var expanded = false
    var enabledKeyboard = false
    var outsideTouchEnable = true

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    private fun getWindowHeight(): Int  = Resources.getSystem().displayMetrics.heightPixels

    private val bottomSheetCallback: BottomSheetCallback = object : BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) dismiss()
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            val offset = abs(slideOffset)
            if (!expanded) return
            if (offset > minDimOffset) dialog?.window?.setDimAmount(minDimOffset)
            else dialog?.window?.setDimAmount(offset)
//            if(slide >= 0.7) {
//                container.translationY = translation * statusBarHeight
//                container.setPadding(0, 0, 0, translation * statusBarHeight)
//            } else {
//                container.translationY = 0f
//                container.setPadding(0, 0, 0, 0)
//            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return expandSheet(super.onCreateDialog(savedInstanceState))
    }

    fun replaceFragment(container: Int, fragment: Fragment) {
        val ft: FragmentTransaction = childFragmentManager.beginTransaction()
        ft.replace(container, fragment)
        ft.commit()
    }

    private fun expandSheet(dialog: Dialog): Dialog {
        dialog.setOnShowListener(this)
        val window = dialog.window ?: return dialog
        window.setDimAmount(if (expanded) 0f else minDimOffset)
        if (expanded) {
            window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        if (enabledKeyboard) window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        )
        return dialog
    }

    fun hideBottomSheet() {
        try {
            if (bottomSheetBehavior == null) return
            dismiss()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onShow(dialog: DialogInterface) {
        val bottomSheetDialog = dialog as BottomSheetDialog
        bottomSheetDialog.setCanceledOnTouchOutside(outsideTouchEnable)

        bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet) ?: return
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet!!)
        bottomSheetBehavior?.addBottomSheetCallback(bottomSheetCallback)
        if (!expanded) return
        bottomSheetBehavior?.peekHeight = getWindowHeight()
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED

        val layoutParams = bottomSheet?.layoutParams ?: return
        layoutParams.height = getWindowHeight()
        bottomSheet?.layoutParams = layoutParams
    }


    fun show(activity: AppCompatActivity, tag: String) {
        if (activity.isFinishing || activity.isDestroyed) return
        if (isAdded || isStateSaved) return
        show(activity.supportFragmentManager, tag)
    }

    companion object {
        const val minDimOffset = 0.10f
    }
}