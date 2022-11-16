package com.degpeg.degpeg_sample.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.degpeg.b2csdk.DegpegSDKProvider
import com.degpeg.degpeg_sample.databinding.ActivityFragmentSampleBinding
import com.degpeg.utility.Log

class FragmentSampleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFragmentSampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentSampleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        DegpegSDKProvider.useAsFragment(
            supportFragmentManager = supportFragmentManager,
            containerId = binding.container.id,
            onError = {
            }
        )
    }
}