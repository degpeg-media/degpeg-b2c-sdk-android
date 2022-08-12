package com.degpeg.videoplayer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.degpeg.databinding.PlayerSettingItemBinding
import com.degpeg.utility.gone
import com.degpeg.utility.invisible
import com.degpeg.utility.visible

internal class PlayerSpeedSettingsAdapter(
    private var texts: ArrayList<String>,
    private val onItemClick: (position: Int) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {
    var checkPosition = 0

    fun setTexts(texts: ArrayList<String>) {
        this.texts = texts
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            PlayerSettingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding, this)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (viewHolder is DataViewHolder) {
            viewHolder.bind(texts[position])
        }
    }

    override fun getItemCount(): Int {
        return texts.size
    }

    companion object {
        class DataViewHolder(
            val binding: PlayerSettingItemBinding,
            val adapter: PlayerSpeedSettingsAdapter
        ) : ViewHolder(binding.root) {

            init {
                binding.root.setOnClickListener {
                    adapter.onItemClick.invoke(absoluteAdapterPosition)
                }
            }

            fun bind(data: String) {
                binding.exoText.text = data
                binding.exoCheck.visible()
                    .takeIf { absoluteAdapterPosition == adapter.checkPosition }
                    ?: binding.exoCheck.invisible()
            }
        }
    }
}