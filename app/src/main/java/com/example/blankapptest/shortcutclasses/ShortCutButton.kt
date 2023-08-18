package com.example.blankapptest.shortcutclasses

import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.blankapptest.R

class ShortCutButton(
    shortCutId:String,
    sendMessage:(msg:String) -> Unit
    //TODO(img var must be implemented here)
): ShortCutBase(shortCutId, sendMessage) {
    override fun initShortCutViewGroup(viewHolder: ViewHolder) {
        if (viewHolder is ButtonViewHolder) {
            val buttonViewHolder:ButtonViewHolder = viewHolder as ButtonViewHolder
            buttonViewHolder.button.setOnClickListener { _ -> sendMessage("button $shortCutId ") } //TODO(change to shorter and parsable message)
        }
    }

    class ButtonViewHolder(itemView: View) : ShortCutBase.ViewHolder(itemView)
    {
        val button: ImageButton = itemView.findViewById<ImageButton>(R.id.ibShortCut)
    }

}