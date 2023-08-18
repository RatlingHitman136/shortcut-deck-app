package com.example.blankapptest.shortcutclasses

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class ShortCutBase(val shortCutId:String, val sendMessage:(msg:String) -> Unit) {
    open fun initShortCutViewGroup(viewHolder: ViewHolder) {}

    abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    { }
}
