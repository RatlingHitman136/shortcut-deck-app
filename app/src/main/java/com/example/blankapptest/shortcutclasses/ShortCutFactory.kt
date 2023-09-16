package com.example.blankapptest.shortcutclasses

import com.example.blankapptest.shortcutclasses.shortcuttypes.ShortCutBase
import com.example.blankapptest.shortcutclasses.shortcuttypes.ShortCutButton

class ShortCutFactory {

     private val SECOND_LEVEL_SPLIT_CHARACTER : String = ":";
     private val THIRD_LEVEL_SPLIT_CHARACTER : String = " ";

     private val SHORT_CUT_BUTTON_TAG : String = "b";
     private val SHORT_CUT_EMPTY_TAG : String = "e";

    fun profileFromStringsFromServer(profileID:String, dataOfShortCuts:List<String>):ShortCutProfile
    {
        val shortCuts : MutableList<ShortCutBase> = mutableListOf()
        for (data in dataOfShortCuts) {
            val splitted = data.split(SECOND_LEVEL_SPLIT_CHARACTER);
            if (splitted.isEmpty())
                continue
            when (splitted[0]) {
                SHORT_CUT_BUTTON_TAG -> {
                    shortCuts.add(ShortCutButton(splitted[1]))
                }
                SHORT_CUT_EMPTY_TAG -> {
                    shortCuts.add(ShortCutBase(""))
                }
            }
        }
        return ShortCutProfile(profileID, shortCuts)
    }
}