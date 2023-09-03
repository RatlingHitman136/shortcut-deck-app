package com.example.blankapptest.actions.actiontypes

class ActionScanRecieve(private val passwordRecieved:String,
                        private val deviceName:String) : ActionBase() {
    fun isPasswordMatches(passCheck:String):Boolean {
        return passCheck == passwordRecieved
    }

    fun getDeviceName():String{
        return  deviceName
    }
}