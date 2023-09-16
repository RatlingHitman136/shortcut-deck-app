package com.example.blankapptest.shortcutclasses.shortcuttypes

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class ShortCutBase(val shortCutId:String) {

    private var onShortCutTriggered: ((msg:String) -> Unit)? = null
    open fun initShortCutViewGroup(viewHolder: ViewHolder) {}

    fun getOnShortCutTriggered() : ((msg:String) -> Unit)? {
        return onShortCutTriggered
    }

    fun setOnShortCutTriggered(onShortCutTriggered:(msg:String) -> Unit){
        this.onShortCutTriggered = onShortCutTriggered
    }

    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
}
