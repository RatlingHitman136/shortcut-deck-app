package com.example.blankapptest.shortcutclasses

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.blankapptest.actions.actiontypes.ActionShortCutTriggered
import com.example.blankapptest.networking.ClientClass

class ShortCutProfileManager(ctx: Context,
                             shortCutHolder:RecyclerView)  {

    private val profiles : MutableList<ShortCutProfile> = mutableListOf()
    private var curProfileID:String = ""
    private var shortCutAdapter: ShortCutAdapter = ShortCutAdapter(ctx)
    private var client:ClientClass? = null

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

    private fun clearProfiles()
    {
        //some things with ui stuff
    }

    fun notifyNewClientConnected(client: ClientClass?)
    {
        clearProfiles()
        this.client = client
    }


    private fun onShortCutTriggered(msg:String)
    {
        val action = ActionShortCutTriggered(client,msg)
        action.executeAction()
    }
}