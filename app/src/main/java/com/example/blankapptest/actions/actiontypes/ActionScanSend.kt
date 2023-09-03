package com.example.blankapptest.actions.actiontypes

class ActionScanSend(private val passwordToSend:String,
                     private val deviceName:String): ActionBase() {
    fun getPasswordToSend():String{
        return passwordToSend
    }

    fun getDeviceName():String{
        return deviceName
    }
}