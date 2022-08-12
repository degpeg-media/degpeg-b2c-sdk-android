package com.degpeg.videoplayer

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.degpeg.R
import com.degpeg.databinding.PlayerSettingBottomsheetBinding
import com.degpeg.ui.bottomsheet.BaseBottomSheet
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.PlaybackParameters
import kotlin.math.roundToInt

internal class PlaybackSpeedBottomSheet(
    private val player: ExoPlayer?,
    private val dismissCallback: () -> Unit
) : BaseBottomSheet() {

    private var _binding: PlayerSettingBottomsheetBinding? = null
    private val binding get() = _binding!!

    private var playbackSpeedTextList = ArrayList<String>()
    private var playbackSpeedMultBy100List = ArrayList<Int>()
    private var selectedPlaybackSpeedIndex = 0
    private var speedSettingsAdapter: PlayerSpeedSettingsAdapter? = null

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
        if (speedSettingsAdapter == null) {
            if (playbackSpeedTextList.isEmpty()) {
                setUpList()
            }
            speedSettingsAdapter =
                PlayerSpeedSettingsAdapter(playbackSpeedTextList, this::onSpeedItemClick)
            speedSettingsAdapter?.checkPosition = selectedPlaybackSpeedIndex
        }
        binding.rvSpeed.layoutManager = LinearLayoutManager(context)
        binding.rvSpeed.adapter = speedSettingsAdapter
    }

    private fun onSpeedItemClick(position: Int) {
        if (position != selectedPlaybackSpeedIndex) {
            val speed = playbackSpeedMultBy100List[position] / 100.0f
            player?.playbackParameters = PlaybackParameters(speed)
        }
        dismiss()
    }

    private fun setUpList() {
        playbackSpeedTextList =
            ArrayList(listOf(*resources.getStringArray(R.array.exo_playback_speeds)))
        playbackSpeedMultBy100List = ArrayList()
        val speeds = resources.getIntArray(R.array.exo_speed_multiplied_by_100)
        for (speed in speeds) {
            playbackSpeedMultBy100List.add(speed)
        }
        selectedPlaybackSpeedIndex = getCurrentSpeedIndex()
    }

    private fun getCurrentSpeedIndex(): Int {
        val indexForCurrentSpeed: Int
        val speed = player?.playbackParameters?.speed ?: return 0
        val currentSpeedMultBy100 = (speed * 100).roundToInt()
        indexForCurrentSpeed = playbackSpeedMultBy100List.indexOf(currentSpeedMultBy100)
        if (indexForCurrentSpeed == -1) return playbackSpeedMultBy100List.indexOf(100)
        return indexForCurrentSpeed
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