package com.example.blankapptest.actions.actiontypes

import com.example.blankapptest.networking.ClientClass

class ActionShortCutTriggered(
    private val clientClassToNotify: ClientClass?,
    private val msg:String
) : ActionBase() {

    override fun executeAction() {
        super.executeAction()
        if(clientClassToNotify == null)
            return
        clientClassToNotify.sendMessage(msg)
    }
}