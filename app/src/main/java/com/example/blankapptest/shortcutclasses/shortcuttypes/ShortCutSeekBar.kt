package com.example.blankapptest.shortcutclasses.shortcuttypes

import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.example.blankapptest.R


class ShortCutSeekBar(
    shortCutId:String
) : ShortCutBase(shortCutId){

    override fun initShortCutViewGroup(viewHolder: ViewHolder) {
        if(viewHolder is SeekBarViewHolder)
        {
            val seekBarViewHolder = viewHolder as SeekBarViewHolder
            val onSeekBarListener = object : OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) { }

                override fun onStartTrackingTouch(seekBar: SeekBar) { }

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    //getOnShortCutTriggered()?.invoke("seekbar $shortCutId $progress")
                    //TODO add message sending process
                }
            }
            seekBarViewHolder.seekBar.setOnSeekBarChangeListener(onSeekBarListener)
        }
    }

    class SeekBarViewHolder(itemView: View) : ViewHolder(itemView)
    {
        val seekBar:SeekBar = itemView.findViewById<SeekBar>(R.id.sbShortCutSeekBar)
    }
}