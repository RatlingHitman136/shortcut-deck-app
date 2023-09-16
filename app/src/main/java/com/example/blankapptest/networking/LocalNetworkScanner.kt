package com.example.blankapptest.networking

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.example.blankapptest.MainActivity
import com.example.blankapptest.actions.actiontypes.ActionBase
import com.example.blankapptest.actions.ActionFactory
import com.example.blankapptest.actions.actiontypes.ActionScanRecieve
import com.example.blankapptest.actions.actiontypes.ActionScanSend
import java.lang.Exception
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class LocalNetworkScanner(
    //val ctx: Context,
    private val mainActivity:MainActivity,
    private val portToScanFor:Int,
    private val passwordSend:String,
    private val passwordReceive:String,
    private val mobileDeviceName:String,
    private val responseTimeOut:Long,
    val onPossibleDeviceFound: (DeviceData) -> Unit,
){
    private var mainHandler: Handler = Handler(Looper.getMainLooper())
    private val generalScanExecutor:ExecutorService = Executors.newSingleThreadExecutor()
    private val specificScanExecutor:ExecutorService = Executors.newSingleThreadExecutor()

    public fun startGeneralScan()
    {
        generalScanExecutor.execute()
        {
            kotlin.run {
                runGeneralScan()
            }
        }
    }

    public fun startSpecificScan(listOfAddresses:MutableList<String>)
    {
        for (address in listOfAddresses)
            startSpecificScan(address)
    }

    public fun startSpecificScan(address:String)
    {
        specificScanExecutor.execute()
        {
            kotlin.run{
                runSpecificScan(address)
            }
        }

    }

    private fun runSpecificScan(address:String)
    {
        try {
            val clientSocket = Socket()
            clientSocket.connect(InetSocketAddress(address, portToScanFor), 200)
            if (clientSocket.isConnected) {
                val executor = Executors.newSingleThreadExecutor()
                executor.execute(kotlinx.coroutines.Runnable
                {
                    kotlin.run {
                        val deviceData = checkValidityOfConnection(clientSocket)
                        if (deviceData != null) {
                            clientSocket.close()
                            possibleDeviceFound(deviceData)
                        }
                    }
                })
                executor.awaitTermination(responseTimeOut, TimeUnit.MILLISECONDS)
            }
        } catch (e: Exception) {
            print(e.toString())
        }
    }

    private fun runGeneralScan() {
        val ip:MutableList<String> = getLocalIp()
        //startSpecificScan(ip[0] + "." + ip[1] + "." + ip[2] + ".136") //TODO(for quick test)
        if(ip.isEmpty())
            return
        var i = 1
        do {
            try {
                startSpecificScan(ip[0] + "." + ip[1] + "." + ip[2] + "." + i.toString())
            } catch (e: Exception) {
                print(e.toString())
            }
            i++
        } while (i < 255)
    }

    private fun possibleDeviceFound(deviceData: DeviceData)
    {
        mainHandler.post{
            kotlin.run {
                onPossibleDeviceFound(deviceData)
            }
        }
    }


    private fun checkValidityOfConnection(clientSocket:Socket) : DeviceData? {
        val inputStream = clientSocket.getInputStream()
        val outputStream = clientSocket.getOutputStream()

        val actionFactory = ActionFactory(mainActivity)
        val actionScanSend = ActionScanSend(passwordSend, mobileDeviceName)
        val msg = actionFactory.getStringFromActionFromClient(actionScanSend)

        //send invitation
        val data = msg.encodeToByteArray()
        outputStream.write(data)

        //wait for the answer
        val buffer = ByteArray(1024)
        val byte: Int = inputStream.read(buffer)
        if (byte > 0) {
            val recievedMsg = String(buffer, 0, byte).trim()
            //if recieved msg is valid as reply to invitation

            val recievedAction : ActionBase = actionFactory.getActionFromStringFromServer(recievedMsg)

            if (recievedAction is ActionScanRecieve) {
                return DeviceData(
                    clientSocket.remoteSocketAddress.toString().removePrefix("/"),
                    portToScanFor,
                    recievedAction.getDeviceName()
                )
            }
        }
        return null
    }

    private fun getLocalIp(): MutableList<String> {
        DatagramSocket().use { socket ->
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002)
            if (socket.localAddress.hostAddress == null)
                return mutableListOf()
            return socket.localAddress.hostAddress?.split(".") as MutableList<String>
        }
    }

    data class DeviceData(
        val ipAddress: String,
        val port: Int,
        val deviceName: String
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as DeviceData

            if (ipAddress != other.ipAddress) return false
            return port == other.port
        }

        override fun hashCode(): Int {
            var result = ipAddress.hashCode()
            result = 31 * result + port
            result = 31 * result + deviceName.hashCode()
            return result
        }

    }
}