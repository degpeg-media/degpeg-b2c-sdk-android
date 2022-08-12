package com.degpeg.ui.activity

import android.os.Bundle
import androidx.fragment.app.commit
import com.degpeg.common.BaseActivity
import com.degpeg.databinding.ActivityDegpegHomeBinding
import com.degpeg.ui.fragment.HomeFragment

internal class DegpegHomeActivity : BaseActivity() {

    private lateinit var binding: ActivityDegpegHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDegpegHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initUi() {
        val fragment = HomeFragment.newInstance()
        supportFragmentManager.commit {
            replace(binding.containerView.id, fragment, fragment::class.java.simpleName)
        }
    }
}