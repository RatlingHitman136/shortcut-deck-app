package com.example.blankapptest.shortcutclasses.shortcuttypes

import android.view.View
import android.widget.ImageButton
import com.example.blankapptest.R

class ShortCutButton(
    shortCutId:String,
    //TODO(img var must be implemented here)
): ShortCutBase(shortCutId) {
    override fun initShortCutViewGroup(viewHolder: ViewHolder) {
        if (viewHolder is ButtonViewHolder) {
            val buttonViewHolder: ButtonViewHolder = viewHolder as ButtonViewHolder
            buttonViewHolder.button.setOnClickListener { _ -> onShortCutTriggered.invoke(shortCutId) }
        }
    }

    class ButtonViewHolder(itemView: View) : ViewHolder(itemView)
    {
        val button: ImageButton = itemView.findViewById<ImageButton>(R.id.ibShortCut)
    }

}