package com.degpeg.common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

internal abstract class BaseFragment : Fragment() {
    var rootView: View? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view
        initUi()
    }

    abstract fun initUi()

}