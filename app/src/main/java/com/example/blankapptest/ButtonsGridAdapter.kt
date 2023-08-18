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


class ButtonsGridAdapter(
    context:Context,
    private val shortCutList: MutableList<ShortCutBase>
) : RecyclerView.Adapter<ShortCutBase.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortCutButton.ButtonViewHolder {
        val view:View = inflater.inflate(R.layout.button_layout, parent,false)
        return ShortCutButton.ButtonViewHolder(view)
    }


    override fun getItemCount(): Int {
        return shortCutList.count()
    }

    override fun onBindViewHolder(holder: ShortCutBase.ViewHolder, position: Int) {
        shortCutList[position].initShortCutViewGroup(holder)
    }
}