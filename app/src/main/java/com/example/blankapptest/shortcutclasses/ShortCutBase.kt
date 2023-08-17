package com.example.blankapptest.shortcutclasses

import android.view.View

abstract class ShortCutBase(val shortCutId:String, val sendMessage:(msg:String) -> Unit) {
    open fun initShortCutViewGroup(view: View) {}
}
