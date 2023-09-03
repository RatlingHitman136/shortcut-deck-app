package com.example.blankapptest.shortcutclasses.shortcuttypes

import android.view.View
import android.widget.ImageButton
import com.example.blankapptest.R

class ShortCutButton(
    shortCutId:String,
    //TODO(img var must be implemented here)
): ShortCutBase(shortCutId) {
    private var onShortCutPressed: ((msg:String) -> Unit)? = null
    fun setOnShortCutPressedFunction(onShortCutPressed: (msg: String) -> Unit) {
        this.onShortCutPressed = onShortCutPressed
    }

    override fun initShortCutViewGroup(viewHolder: ViewHolder) {
        if (viewHolder is ButtonViewHolder) {
            val buttonViewHolder: ButtonViewHolder = viewHolder as ButtonViewHolder
            buttonViewHolder.button.setOnClickListener { _ -> onShortCutPressed?.invoke(shortCutId) } //TODO(change to shorter and parsable message)
        }
    }

    class ButtonViewHolder(itemView: View) : ViewHolder(itemView)
    {
        val button: ImageButton = itemView.findViewById<ImageButton>(R.id.ibShortCut)
    }

}