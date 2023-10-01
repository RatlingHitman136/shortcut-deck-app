package com.example.blankapptest.actions

import com.example.blankapptest.MainActivity
import com.example.blankapptest.actions.actiontypes.*

const val FIRST_LEVEL_SPLIT_CHARACTER : String = "/"

const val ACTION_SCAN_TAG : String = "scan"
const val ACTION_RECIEVED_PROFILE_TAG:String = "setP"
const val ACTION_PROFILES_REQUEST_TAG:String = "reqAllProf"
const val ACTION_SHORTCUT_TRIGGERED:String = "shortCutTrig"

//TODO(move all consts to one place)
class ActionFactory(private val mainActivity: MainActivity? = null) {

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
                if (actionStringList.count() >= 3 && mainActivity != null) {
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
        var actionString = ""

        when (action) {
            is ActionScanSend -> {
                actionString = ACTION_SCAN_TAG + FIRST_LEVEL_SPLIT_CHARACTER + action.getPasswordToSend() + FIRST_LEVEL_SPLIT_CHARACTER + action.getDeviceName()
            }
            is ActionProfilesRequestSend -> {
                actionString+= ACTION_PROFILES_REQUEST_TAG
            }
            is ActionShortCutTriggered -> {
                actionString = ACTION_SHORTCUT_TRIGGERED + FIRST_LEVEL_SPLIT_CHARACTER + action.getProfileID() + FIRST_LEVEL_SPLIT_CHARACTER + action.getMSGFromShortCut()
            }
        }

        return actionString
    }
}