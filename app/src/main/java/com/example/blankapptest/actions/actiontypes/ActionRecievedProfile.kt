package com.example.blankapptest.actions.actiontypes

import com.example.blankapptest.MainActivity
import com.example.blankapptest.shortcutclasses.ShortCutFactory
import com.example.blankapptest.shortcutclasses.ShortCutProfile

class ActionRecievedProfile(private val mainActivity: MainActivity,
                            private val profileID:String,
                            private val profileData:List<String>) : ActionBase() {
    override fun executeAction() {
        super.executeAction()
        val factory:ShortCutFactory = ShortCutFactory()
        val profile:ShortCutProfile = factory.profileFromStringsFromServer(profileID, profileData)

    }
}