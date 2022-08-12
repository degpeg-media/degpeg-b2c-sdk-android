package com.degpeg.degpeg_sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.degpeg.b2csdk.DegpegSDKProvider
import com.degpeg.degpeg_sample.databinding.ActivityMainBinding
import com.degpeg.model.User

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
    }

    private fun initUi() {
        binding.clickListener = this
        DegpegSDKProvider.init(appId = appId, secretKey = secretKey)
        DegpegSDKProvider.updateUser(
            User(userName = "Dhaval Patel", userId = "6278c4556cb38a7a9c10df6e")
        )
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnStart -> {
                DegpegSDKProvider.startAsActivity(
                    activity = this,
                    publisherId = publisherId
                )
            }
            binding.btnFragment -> {
                startActivity(Intent(this, FragmentSampleActivity::class.java))
            }
        }
    }

    companion object {
        const val appId = /*"degpeg__ckYh0Pu9" */ "degpegdegpeg _mediaXuUwyvni"
        const val secretKey = /*"8fxccSiQ8yRr8kVS"*/ "Nnra8P2iGqT2uJFU"
        const val publisherId = "6007cf41f2895e2eabcc2ac2"
    }
}