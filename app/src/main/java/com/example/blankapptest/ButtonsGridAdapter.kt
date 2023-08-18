package com.example.blankapptest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.blankapptest.shortcutclasses.ShortCutBase
import com.example.blankapptest.shortcutclasses.ShortCutButton
import com.example.blankapptest.shortcutclasses.ShortCutSeekBar
import java.lang.Exception


class ButtonsGridAdapter(
    context:Context,
    private val shortCutList: MutableList<ShortCutBase>
) : RecyclerView.Adapter<ShortCutBase.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getItemViewType(position: Int): Int {
            when(shortCutList[position]){
                is ShortCutButton -> return 1
                is ShortCutSeekBar -> return 2
            }
        throw Exception("wrong type")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortCutBase.ViewHolder {
        when(viewType) {
            1 -> {
                val view = inflater.inflate(R.layout.button_layout, parent, false)
                return ShortCutButton.ButtonViewHolder(view)
            }

            2 -> {
                val view = inflater.inflate(R.layout.seekbar_layout, parent, false)
                return ShortCutSeekBar.SeekBarViewHolder(view)
            }
        }
        throw Exception("wrong type")
    }


    override fun getItemCount(): Int {
        return shortCutList.count()
    }

    override fun onBindViewHolder(holder: ShortCutBase.ViewHolder, position: Int) {
        shortCutList[position].initShortCutViewGroup(holder)
    }
}