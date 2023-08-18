package com.example.blankapptest.shortcutclasses

import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import com.example.blankapptest.R


class ShortCutSeekBar(
    shortCutId:String,
    sendMessage:(msg:String) -> Unit
) : ShortCutBase(shortCutId,sendMessage){

    override fun initShortCutViewGroup(viewHolder: ViewHolder) {
        if(viewHolder is SeekBarViewHolder)
        {
            val seekBarViewHolder = viewHolder as SeekBarViewHolder
            val onSeekBarListener = object : OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) { }

                override fun onStartTrackingTouch(seekBar: SeekBar) { }

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                        sendMessage("seekbar $shortCutId $progress")
                }
            }
            seekBarViewHolder.seekBar.setOnSeekBarChangeListener(onSeekBarListener)
        }
    }

    class SeekBarViewHolder(itemView: View) : ShortCutBase.ViewHolder(itemView)
    {
        val seekBar:SeekBar = itemView.findViewById<SeekBar>(R.id.sbShortCutSeekBar)
    }
}