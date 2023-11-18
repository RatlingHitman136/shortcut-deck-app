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

    fun addNewRecievedProfile(newProfile:ShortCutProfile) {
        newProfile.setOnShortCutTriggeredFromProfileManager { msg: String -> onShortCutTriggered(msg) }

        val index = getProfileIndexByID(newProfile.getProfileID())
        if(index != -1)
        {
            profiles[index] = newProfile
            if(newProfile.getProfileID() == curProfileID)
                setCurrentProfile(curProfileID)
        }
        else {
            profiles.add(newProfile)
            if (profiles.count() == 1)
                setCurrentProfile(profiles[0].getProfileID())
        }
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
        profiles.clear()
        shortCutAdapter.clearAllShortCuts()
    }

    fun notifyNewClientConnected(client: ClientClass?)
    {
        clearProfiles()
        this.client = client
    }


    private fun onShortCutTriggered(msg:String)
    {
        val action = ActionShortCutTriggered(client,curProfileID,msg)
        action.executeAction()
    }

    /** Finds the index of profile from profiles by ID
     *  @param id
     *  @return index of profile in profiles and -1 if no profile with such id
     */
    private fun getProfileIndexByID(id: String) : Int
    {
        var index = -1;
        profiles.forEach { x: ShortCutProfile ->
            run {
                if(x.getProfileID() == id)
                {
                    index = profiles.indexOf(x)
                }
            }
        }
        return index
    }

}