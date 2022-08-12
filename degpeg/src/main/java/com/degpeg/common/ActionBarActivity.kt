package com.degpeg.common

import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.degpeg.R
import com.degpeg.databinding.LayoutHomeToolbarBinding
import com.degpeg.databinding.LayoutToolbarBinding

internal abstract class ActionBarActivity : BaseActivity(), View.OnClickListener {
    lateinit var actionView: LayoutToolbarBinding

    override fun setContentView(view: View?) {
        if (view != null) {
            actionView = LayoutToolbarBinding.bind(view)
            setSupportActionBar(actionView.toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            clickListeners()
        }
        super.setContentView(view)
    }

    private fun clickListeners() {
        actionView.imgBack.setOnClickListener(this)
        actionView.imgNotification.setOnClickListener(this)
    }

    fun homeUpEnable(enable: Boolean) {
        actionView.imgBack.visibility = View.VISIBLE.takeIf { enable } ?: View.GONE
    }

    protected fun setUpToolbar(title: String?, isHomeUpEnabled: Boolean = true) {
        actionView.txtToolbarTitle.text = title
        homeUpEnable(isHomeUpEnabled)
    }

    protected fun setUpToolbar(resId: Int, isHomeUpEnabled: Boolean? = true) {
        setUpToolbar(getString(resId), isHomeUpEnabled!!)
    }

    protected fun changeTitle(string: String) {
        actionView.txtToolbarTitle.text = string
    }

    protected fun changeTitleColor(color: Int) {
        actionView.txtToolbarTitle.setTextColor(color)
    }

    protected fun changeIconColor(color: Int) {
        actionView.imgNotification.setColorFilter(color)
    }

    fun setSubTitleText(value: String?) {
        actionView.txtSubTitle.text = value
        actionView.txtSubTitle.visibility =
            View.GONE.takeIf { value == null || value.isEmpty() } ?: View.VISIBLE
    }

    fun setSubTitleTextColor(resId: Int) {
        actionView.txtSubTitle.setTextColor(ContextCompat.getColor(this, resId))
    }

    fun setSubTitleText(value: String, count: Long) {
        actionView.txtSubTitle.text = value
        actionView.txtSubTitle.visibility = View.GONE.takeIf { count <= 0 } ?: View.VISIBLE
    }

    fun setSubTitleEnable(value: Boolean) {
        actionView.txtSubTitle.visibility = View.VISIBLE.takeIf { value } ?: View.GONE
    }

    override fun onClick(view: View?) {
        when (view) {
            actionView.imgBack -> onBackPressed()
            actionView.imgNotification -> {

            }
        }
    }

    override fun onBackPressed() {
        hideKeyBoard()
        super.onBackPressed()
    }
}