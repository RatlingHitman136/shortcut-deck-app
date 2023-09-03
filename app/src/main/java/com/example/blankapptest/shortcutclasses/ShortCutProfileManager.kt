package com.example.blankapptest.shortcutclasses

class ShortCutProfileManager {
    private val profiles : MutableList<ShortCutProfile> = mutableListOf()
    private val curProfileID:String = ""
    fun addNewProfile(newProfile:ShortCutProfile){
        profiles.add(newProfile)
        if(profiles.count() == 1)
        {
            setCurrentProfile(profiles[0].getProfileID())
        }
    }

    fun setCurrentProfile(profileID:String)
    {
        //some things with ui stuff
    }

    fun clearProfiles()
    {
        //some things with ui stuff
    }


    fun onShortCutTriggered(msg:String)
    {
        //some things with client class
    }
}