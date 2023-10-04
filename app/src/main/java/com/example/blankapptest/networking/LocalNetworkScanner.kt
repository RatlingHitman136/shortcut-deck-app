package com.example.blankapptest.networking

import android.os.Handler
import android.os.Looper
import com.example.blankapptest.MainActivity
import com.example.blankapptest.actions.actiontypes.ActionBase
import com.example.blankapptest.actions.ActionFactory
import com.example.blankapptest.actions.actiontypes.ActionScanReceive
import com.example.blankapptest.actions.actiontypes.ActionScanSend
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
import kotlin.Exception
import kotlin.math.min

const val MAX_POSSIBLE_THREAD_COUNT_FOR_GENERAL_SCAN = 32

class LocalNetworkScanner(
    private val mainActivity:MainActivity,
    private val portToScanFor:Int,
    private val passwordSend:String,
    private val passwordReceive:String,
    private val mobileDeviceName:String,
    private val socketConnectionTimeOut: Int,
    private val serverResponseTimeOut:Long,
    numOfThreads: Int,
    val onPossibleDevicesFound: (MutableList<DeviceData>) -> Unit,
){
    private var mainHandler: Handler = Handler(Looper.getMainLooper())
    private val scanExecutorPool = Executors.newFixedThreadPool(min(numOfThreads, MAX_POSSIBLE_THREAD_COUNT_FOR_GENERAL_SCAN))

    fun startGeneralScan(devicesNotToScan:MutableList<DeviceData> = mutableListOf()) {
        val generalScanExecutor = Executors.newSingleThreadExecutor()
        generalScanExecutor.execute()
        {
            //TODO(remove this tmp logging of ip)
            val ip = getLocalIp()
            mainHandler.post {
                kotlin.run {
                    mainActivity.tvMessageBox.text = (buildString {
                        append(ip[0])
                        append(".")
                        append(ip[1])
                        append(".")
                        append(ip[2])
                        append(".")
                        append(ip[3])
                    })
                }
            }
            val possibleDevices = scanLocalNetworkForPossibleDevices(devicesNotToScan)
            possibleDevicesFound(possibleDevices)
        }
        generalScanExecutor.shutdown()
    }

    private fun scanLocalNetworkForPossibleDevices(devicesNotToScan:MutableList<DeviceData> = mutableListOf()):MutableList<DeviceData>{
        val ip:MutableList<String> = getLocalIp()
        if(ip.isEmpty())
            return mutableListOf()
        val allPossibleIpAddresses:MutableList<String> = mutableListOf()
        for (i in 1..254) {
            val ipAddress = ip[0] + "." + ip[1] + "." + ip[2] + "." + i.toString()
            var isSkipped = false
            devicesNotToScan.forEach() {
                isSkipped = it.ipAddress == ipAddress || isSkipped
            }
            if (!isSkipped)
                allPossibleIpAddresses.add(ipAddress)
        }
        return scanIpsForPossibleDevices(allPossibleIpAddresses)
    }
    private fun scanIpsForPossibleDevices(addresses:MutableList<String>):MutableList<DeviceData> {
        val foundedDevices = mutableListOf<DeviceData>()
        val scanFutureTasks: MutableList<Future<DeviceData?>> = mutableListOf()
        for (address in addresses) {
            val scanTask = Callable<DeviceData?> {
                return@Callable tryValidatePossibleDeviceByIpAddress(address)
            }
            scanFutureTasks.add(scanExecutorPool.submit(scanTask))
        }
        for (futureTask in scanFutureTasks) {
            val res = futureTask.get()
            if (res != null)
                foundedDevices.add(res)
        }
        return foundedDevices
    }
    private fun tryValidatePossibleDeviceByIpAddress(address:String):DeviceData? {
        var deviceData:DeviceData? = null
        val clientSocket = Socket()
        try {
            clientSocket.connect(
                InetSocketAddress(address, portToScanFor),
                socketConnectionTimeOut
            )
            if (clientSocket.isConnected) {
                try {
                    val disconnectionExecutor = Executors.newSingleThreadExecutor()
                    disconnectionExecutor.execute(kotlinx.coroutines.Runnable
                    {
                        Thread.sleep(serverResponseTimeOut)
                        clientSocket.close()
                    })

                    deviceData = checkValidityOfConnection(clientSocket)
                }
                catch (e : Exception)
                {
                    e.printStackTrace()
                    if(!clientSocket.isClosed)
                        clientSocket.close()
                }
            }
        }
        catch (e:Exception) {
            e.printStackTrace()
        }

        return deviceData
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

            if (recievedAction is ActionScanReceive) {
                return DeviceData(
                    clientSocket.remoteSocketAddress.toString().removePrefix("/").split(":")[0],
                    portToScanFor,
                    recievedAction.getDeviceName()
                )
            }
        }
        return null
    }
    private fun possibleDevicesFound(deviceData: MutableList<DeviceData>) {
        mainHandler.post{
            kotlin.run {
                onPossibleDevicesFound(deviceData)
            }
        }
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