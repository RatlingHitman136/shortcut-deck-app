package com.example.blankapptest.actions

import com.example.blankapptest.MainActivity
import com.example.blankapptest.actions.actiontypes.*

class ActionFactory(private val mainActivity: MainActivity) {

    private val FIRST_LEVEL_SPLIT_CHARACTER : String = "/"

    private val ACTION_SCAN_TAG : String = "scan"
    private val ACTION_RECIEVED_PROFILE_TAG:String = "setP"
    private val ACTION_PROFILES_REQUEST_TAG:String = "reqAllProf"

    fun getActionFromStringFromServer(actionString: String): ActionBase {
        val actionStringList = actionString.split(FIRST_LEVEL_SPLIT_CHARACTER)
        if (actionStringList.isEmpty())
            return ActionBase()

        when (actionStringList[0]) {
            ACTION_SCAN_TAG -> {
                if (actionStringList.count() == 3)
                    return ActionScanReceive(actionStringList[1], actionStringList[2])
            }

            ACTION_RECIEVED_PROFILE_TAG -> {
                if (actionStringList.count() >= 3) {
                    return ActionRecievedProfile(
                        mainActivity,
                        actionStringList[1],
                        actionStringList.subList(2, actionStringList.count())
                    )
                }
            }
        }
        return ActionBase()
    }

    fun getStringFromActionFromClient(action: ActionBase) : String {
        var actionString: String = ""

        when (action) {
            is ActionScanSend -> {
                actionString = ACTION_SCAN_TAG + FIRST_LEVEL_SPLIT_CHARACTER + action.getPasswordToSend() + FIRST_LEVEL_SPLIT_CHARACTER + action.getDeviceName()
            }
            is ActionProfilesRequestSend -> {
                actionString+= ACTION_PROFILES_REQUEST_TAG
            }
        }

        return actionString
    }
}