package com.example.blankapptest.shortcutclasses

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

class ShortCutProfileManager(ctx: Context,
                             private val shortCutHolder:RecyclerView)  {

    private val profiles : MutableList<ShortCutProfile> = mutableListOf()
    private var curProfileID:String = ""
    private var shortCutAdapter: ShortCutAdapter = ShortCutAdapter(ctx)

    init {
        shortCutHolder.adapter = shortCutAdapter
    }


    fun addNewProfile(newProfile:ShortCutProfile) {
        profiles.add(newProfile)
        newProfile.setOnShortCutTriggeredFromProfileManager { msg: String -> onShortCutTriggered(msg) }
        if (profiles.count() == 1)
            setCurrentProfile(profiles[0].getProfileID())

    }

    fun setCurrentProfile(profileID:String)
    {
        for (profile in profiles) {
            if (profile.getProfileID() == profileID) {
                shortCutAdapter.setShortCutsFromProfile(profile)
                curProfileID = profileID
            }
            return
        }
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