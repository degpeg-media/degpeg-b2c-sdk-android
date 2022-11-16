package com.degpeg.degpeg_sample.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.PagerSnapHelper
import com.degpeg.b2csdk.DegpegSDKProvider
import com.degpeg.degpeg_sample.LocalDataHelper.appId
import com.degpeg.degpeg_sample.LocalDataHelper.providerId
import com.degpeg.degpeg_sample.LocalDataHelper.publisherId
import com.degpeg.degpeg_sample.LocalDataHelper.secretKey
import com.degpeg.degpeg_sample.LocalDataHelper.userRole
import com.degpeg.degpeg_sample.R
import com.degpeg.degpeg_sample.adapter.ContentAdapter
import com.degpeg.degpeg_sample.adapter.SliderAdapter
import com.degpeg.degpeg_sample.bottomsheet.SettingBottomSheet
import com.degpeg.degpeg_sample.common.ActionBarActivity
import com.degpeg.degpeg_sample.databinding.ActivityMainBinding
import com.degpeg.degpeg_sample.model.ContentModel
import com.degpeg.model.User

class MainActivity : ActionBarActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
    }

    private fun initUi() {
        setUpToolbar("Degpeg Sample", false)
        binding.clickListener = this

        DegpegSDKProvider.init(appId = appId,
            secretKey = secretKey,
            publisherId = publisherId,
            providerId = providerId,
            userRole = userRole,
            onSuccess = { },
            onError = {
                runOnUiThread { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
            })

        DegpegSDKProvider.updateUser(
            User(userName = "Dhaval Patel", userId = "6278c4556cb38a7a9c10df6e")
        )

        setUpDummyData()
    }

    private fun setUpDummyData() {
        setUpImageSlider()
        val adapter = ContentAdapter(this) { binding.btnStart.performClick() }
        binding.recyclerView.adapter = adapter
        adapter.set(dummyData())
    }

    /**
     * image slider
     * */
    private fun setUpImageSlider() {
        binding.rvSliderImage.onFlingListener = null
        PagerSnapHelper().attachToRecyclerView(binding.rvSliderImage)
        binding.rvSliderImage.adapter = SliderAdapter(getDummyImage(), callback = { _, _ ->
            DegpegSDKProvider.startPlayer(
                activity = this, videoSessionId = "6264d7678737f6bbe4d1c37"
            )
        })
    }

    private fun dummyData(): MutableList<ContentModel> {
        val list = ArrayList<ContentModel>()
        list.add(ContentModel("ColorFit Ultra 2", "$68", R.drawable.product_1))
        list.add(ContentModel("Malden Aviator Reading Glasses", "$22", R.drawable.product_2))
        list.add(ContentModel("Men Slim Fit Solid Formal Shirt", "$08", R.drawable.product_3))
        list.add(ContentModel("JBL Headphone", "$87", R.drawable.product_4))
        list.add(ContentModel("ColorFit Ultra 2", "$68", R.drawable.product_1))
        list.add(ContentModel("Malden Aviator Reading Glasses", "$22", R.drawable.product_2))
        list.add(ContentModel("Men Slim Fit Solid Formal Shirt", "$08", R.drawable.product_3))
        list.add(ContentModel("JBL Headphone", "$87", R.drawable.product_4))
        return list
    }

    private fun getDummyImage(): ArrayList<Int> {
        val list = ArrayList<Int>()
        list.add(R.drawable.product_2)
        list.add(R.drawable.product_3)
        list.add(R.drawable.product_2)
        list.add(R.drawable.product_3)
        list.add(R.drawable.product_2)
        list.add(R.drawable.product_3)
        return list
    }

    override fun onSettingClick() {
        SettingBottomSheet.newInstance().show(this, "SettingBottomSheet")
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            binding.btnStart -> {
                DegpegSDKProvider.startAsActivity(activity = this, onError = {
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                })
            }
            binding.btnFragment -> {
                startActivity(Intent(this, FragmentSampleActivity::class.java))
            }
        }
    }

    companion object {
//        const val appId = /*"degpeg__ckYh0Pu9" */ "degpegdegpeg _mediaXuUwyvni"
//        const val secretKey = /*"8fxccSiQ8yRr8kVS"*/ "Nnra8P2iGqT2uJFU"
//        const val publisherId = "6007cf41f2895e2eabcc2ac2"
    }
}