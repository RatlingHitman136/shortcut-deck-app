package com.example.blankapptest.networking

import android.content.Context
import android.os.Handler
import android.os.Looper
import java.lang.Exception
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class LocalNetworkScanner(
    val ctx: Context,
    val portToScanFor:Int,
    val passwordSend:String,
    val passwordReceive:String,
    val mobileDeviceName:String,
    val responseTimeOut:Long,
    val onPossibleDeviceFound: (DeviceData) -> Unit,
) : Thread() {

    private lateinit var prefix: String

    val tagForMessage: String = "scan"

    override fun run() {
        val ip = getLocalIp()
        if(ip.isEmpty())
            return
        prefix = ip[0] + "." + ip[1] + "." + ip[2] + "."

        var i = 1
        do {
            try {
                val clientSocket = Socket()
                clientSocket.connect(InetSocketAddress(prefix + i.toString(), portToScanFor), 100)
                if (clientSocket.isConnected) {
                    //Toast.makeText(ctx, "found possibility on $prefix$i", Toast.LENGTH_SHORT).show() //TODO(remove this)
                    val executor = Executors.newSingleThreadExecutor()
                    executor.execute(kotlinx.coroutines.Runnable
                    {
                        kotlin.run {
                            val deviceData = checkValidityOfConnection(clientSocket)
                            if (deviceData != null)
                                onPossibleDeviceFound(deviceData)
                        }
                    })
                    executor.awaitTermination(responseTimeOut, TimeUnit.MILLISECONDS)
                }
            } catch (e: Exception) {
                print(e.toString())
            }
            i++
        } while (i < 255)
    }

    private fun checkValidityOfConnection(clientSocket:Socket) : DeviceData? {
        val inputStream = clientSocket.getInputStream()
        val outputStream = clientSocket.getOutputStream()
        val handler = Handler(Looper.getMainLooper())

        val msg = "$tagForMessage/$mobileDeviceName/$passwordSend\n"

        //send invitation
        val data = msg.encodeToByteArray()
        outputStream.write(data)

        //wait for the answer
        val buffer = ByteArray(1024)
        val byte: Int = inputStream.read(buffer)
        if (byte > 0) {
            val recievedMsg = String(buffer, 0, byte).split("/") as MutableList<String>
            //if recieved msg is valid as reply to invitation
            if ((recievedMsg.count() == 3) && (recievedMsg[0] == tagForMessage) && (recievedMsg[2] == passwordReceive)) {
                return DeviceData(
                    clientSocket.remoteSocketAddress.toString(),
                    portToScanFor,
                    recievedMsg[1]
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
    ) {}
}