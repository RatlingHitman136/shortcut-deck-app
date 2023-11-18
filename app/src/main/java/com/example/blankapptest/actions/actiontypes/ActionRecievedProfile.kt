package com.example.blankapptest.actions.actiontypes

import com.example.blankapptest.MainActivity
import com.example.blankapptest.shortcutclasses.ShortCutProfileFactory
import com.example.blankapptest.shortcutclasses.ShortCutProfile

class ActionRecievedProfile(private val mainActivity: MainActivity,
                            private val profileID:String,
                            private val profileData:List<String>) : ActionBase() {
    override fun executeAction() {
        super.executeAction()
        val factory:ShortCutProfileFactory = ShortCutProfileFactory()
        val profile:ShortCutProfile = factory.profileFromStringsFromServer(profileID, profileData)
        mainActivity.getShortCutProfileManager().addNewRecievedProfile(profile)
    }
}