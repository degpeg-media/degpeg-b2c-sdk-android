package com.degpeg.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.degpeg.common.BaseFragment
import com.degpeg.common.Navigation
import com.degpeg.databinding.FragmentHomePagerItemBinding
import com.degpeg.model.VideoContentItem
import com.degpeg.utility.loadImage

internal class HomePagerItemFragment(val data: VideoContentItem) : BaseFragment() {

    private var _binding: FragmentHomePagerItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomePagerItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initUi() {
        if (data.userDetail != null) {
            binding.imgProfile.setImageWithInitial(
                data.userDetail?.avatar,
                data.userDetail?.getInitials()
            )
            binding.txtUserName.text = data.userDetail?.getNonNullName() ?: ""
        }
        binding.imgVideoThumb.loadImage(data.bannerUrl)
        binding.txtEventName.text = data.name
        binding.txtEventDesc.text = data.description
        binding.txtLive.text = data.status
        binding.imgPlay.setOnClickListener { Navigation.startPlayer(requireActivity(), data) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}