package com.example.blankapptest.shortcutclasses.shortcuttypes

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class ShortCutBase(val shortCutId:String) {

    internal lateinit var onShortCutTriggered: ((msg:String) -> Unit)
    open fun initShortCutViewGroup(viewHolder: ViewHolder) {}

    fun setOnShortCutTriggered(onShortCutTriggered:(msg:String) -> Unit){
        this.onShortCutTriggered = onShortCutTriggered
    }

    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
}
