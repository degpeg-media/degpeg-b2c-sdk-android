package com.degpeg.degpeg_sample.bottomsheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.degpeg.Controller
import com.degpeg.b2csdk.DegpegSDKProvider
import com.degpeg.b2csdk.UserRole
import com.degpeg.degpeg_sample.LocalDataHelper
import com.degpeg.degpeg_sample.databinding.BottomSheetSettingBinding
import com.degpeg.utility.gone
import com.degpeg.utility.visible

class SettingBottomSheet() : BaseBottomSheet(), View.OnClickListener {

    private lateinit var binding: BottomSheetSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = BottomSheetSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initUi() {
        binding.clickListener = this

        binding.rdgUser.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                binding.rbPublisher.id -> {
                    binding.tvInputPublisher.visible()
                    binding.tvInputProvider.gone()
                }
                binding.rbProvider.id -> {
                    binding.tvInputPublisher.gone()
                    binding.tvInputProvider.visible()
                }
            }
        }

        binding.edtAppId.setText(LocalDataHelper.appId)
        binding.edtSecretKey.setText(LocalDataHelper.secretKey)
        binding.edtPublisherId.setText(LocalDataHelper.publisherId)
        binding.edtProviderId.setText(LocalDataHelper.providerId)
        LocalDataHelper.userRole.typeCall(
            publisher = { binding.rbPublisher.isChecked = true },
            provider = { binding.rbProvider.isChecked = true }
        )
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.btnUpdate -> {
                if (!isValid()) {
                    Toast.makeText(
                        requireActivity(),
                        "Please enter all the details",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                LocalDataHelper.appId = binding.edtAppId.text.toString().trim()
                LocalDataHelper.secretKey = binding.edtSecretKey.text.toString().trim()
                LocalDataHelper.publisherId = binding.edtPublisherId.text.toString().trim()
                LocalDataHelper.providerId = binding.edtProviderId.text.toString().trim()
                LocalDataHelper.userRole =
                    UserRole.PUBLISHER.takeIf { binding.rbPublisher.isChecked } ?: UserRole.PROVIDER

                DegpegSDKProvider.init(
                    appId = LocalDataHelper.appId,
                    secretKey = LocalDataHelper.secretKey,
                    publisherId = LocalDataHelper.publisherId,
                    providerId = LocalDataHelper.providerId,
                    userRole = LocalDataHelper.userRole,
                    requiredReset = true,
                    onSuccess = {
                        requireActivity().runOnUiThread {
                            Toast.makeText(
                                requireActivity(),
                                "Auth Token Updated Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            dismiss()
                        }
                        dismiss()
                    },
                    onError = {
                        requireActivity().runOnUiThread {
                            Toast.makeText(
                                requireActivity(),
                                it,
                                Toast.LENGTH_SHORT
                            ).show()
                            dismiss()
                        }
                    }
                )
            }
        }
    }

    private fun isValid(): Boolean {
        return !binding.edtAppId.text.isNullOrEmpty()
                && !binding.edtSecretKey.text.isNullOrEmpty()
                &&
                ((binding.rbPublisher.isChecked && !binding.edtPublisherId.text.isNullOrEmpty())
                        || (binding.rbProvider.isChecked && !binding.edtProviderId.text.isNullOrEmpty()))
    }

    companion object {
        fun newInstance(): SettingBottomSheet {
            val fragment = SettingBottomSheet()
            return fragment
        }
    }
}