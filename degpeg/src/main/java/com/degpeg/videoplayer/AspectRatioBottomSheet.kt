package com.degpeg.videoplayer

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.degpeg.databinding.PlayerSettingBottomsheetBinding
import com.degpeg.ui.bottomsheet.BaseBottomSheet

internal class AspectRatioBottomSheet(
    private var currentRatio: PlayerAspectRatio = PlayerAspectRatio.RESIZE_MODE_FIT,
    private val ratioChangeCallback: (ratio: PlayerAspectRatio) -> Unit,
    private val dismissCallback: () -> Unit,
) : BaseBottomSheet() {
    private var _binding: PlayerSettingBottomsheetBinding? = null
    private val binding get() = _binding!!

    private var mList = ArrayList<String>()
    private var adapter: PlayerSpeedSettingsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = PlayerSettingBottomsheetBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnClose.setOnClickListener { dismiss() }
        setUpSpeedSetting()
    }

    private fun setUpSpeedSetting() {
        if (adapter == null) {
            if (mList.isEmpty()) {
                setUpList()
            }
            adapter = PlayerSpeedSettingsAdapter(mList, this::onItemClick)
            adapter?.checkPosition = mList.indexOf(currentRatio.value)
        }
        binding.rvSpeed.layoutManager = LinearLayoutManager(context)
        binding.rvSpeed.adapter = adapter
        binding.rvSpeed.itemAnimator = null
    }

    private fun onItemClick(position: Int) {
        val selected = PlayerAspectRatio[mList[position]]
        if (currentRatio != selected) {
            currentRatio = selected
            ratioChangeCallback.invoke(selected)
        }
        dismiss()
    }

    private fun setUpList() {
        PlayerAspectRatio.values().forEach {
            mList.add(it.value)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissCallback.invoke()
    }
}