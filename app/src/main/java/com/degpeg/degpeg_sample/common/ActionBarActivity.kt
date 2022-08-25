package com.degpeg.degpeg_sample.common

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.degpeg.degpeg_sample.databinding.LayoutToolbarHomeBinding

abstract class ActionBarActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var actionView: LayoutToolbarHomeBinding

    override fun setContentView(view: View?) {
        if (view != null) {
            actionView = LayoutToolbarHomeBinding.bind(view)
            setSupportActionBar(actionView.toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            clickListeners()
        }
        super.setContentView(view)
    }

    private fun clickListeners() {
        actionView.imgBack.setOnClickListener(this)
        actionView.imgSetting.setOnClickListener(this)
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
        actionView.imgSetting.setColorFilter(color)
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
            actionView.imgSetting -> {
                onSettingClick()
            }
        }
    }

    abstract fun onSettingClick()
}