package com.example.blankapptest.shortcutclasses

import com.example.blankapptest.shortcutclasses.shortcuttypes.ShortCutBase

class ShortCutProfile(
    private val profileID:String,
    private var shortCutsList: MutableList<ShortCutBase>
) {
    private var shortCutTriggeredProfileManager: ((msg:String) -> Unit)? = null

    init {
        for (shortCut in shortCutsList)
        {
            shortCut.setOnShortCutTriggered { msg:String -> shortCutTriggered(msg) }
        }
    }

    fun getProfileID():String{
        return profileID
    }


    fun shortCutTriggered(msg:String)
    {
        shortCutTriggeredProfileManager?.invoke(msg)
    }
}