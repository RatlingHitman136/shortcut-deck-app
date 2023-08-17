package com.example.blankapptest.shortcutclasses

import android.view.View
import android.widget.ImageButton
import com.example.blankapptest.R

class ShortCutButton(
    shortCutId:String,
    sendMessage:(msg:String) -> Unit
    //TODO(img var must be implemented here)
): ShortCutBase(shortCutId, sendMessage) {
    override fun initShortCutViewGroup(view: View)
    {
        val btn = view.findViewById<ImageButton>(R.id.ibShortCut)
        btn.setOnClickListener { _ -> sendMessage("shortcut with id = $shortCutId was pressed") } //TODO(change to shorter and parsable message)
    }

}