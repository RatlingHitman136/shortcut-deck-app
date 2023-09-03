package com.example.blankapptest.networking

import android.os.Handler
import android.os.Looper
import com.example.blankapptest.MainActivity
import com.example.blankapptest.actions.ActionFactory
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.util.LinkedList
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class ClientClass(address: String, mainActivity: MainActivity): Thread() {
    private var hostAddress: String = address
    private lateinit var inputStream: InputStream
    private lateinit var outputStream: OutputStream
    private lateinit var socket: Socket

    private val executorRead: ExecutorService = Executors.newSingleThreadExecutor()
    private val executorWrite: ExecutorService = Executors.newSingleThreadExecutor()

    private var sendingQueue = LinkedList<ByteArray>()

    private val actionFactory:ActionFactory = ActionFactory(mainActivity)

    override fun run() {
        try {
            connect()
        }catch (ex:IOException){
            ex.printStackTrace()
        }
    }

    private fun connect()
    {
        socket = Socket()
        val ip:String = hostAddress.split(":")[0]
        val port:Int = hostAddress.split(":")[1].toInt()
        socket.connect(InetSocketAddress(ip,port),500)
        inputStream = socket.getInputStream()
        outputStream = socket.getOutputStream()
        startExchangingMessages()
    }

    fun close()
    {
        socket.shutdownInput()
        socket.shutdownOutput()
        socket.close()
        executorRead.shutdownNow()
        executorWrite.shutdownNow()
        sendingQueue.clear()
    }

    private fun handleMessage(buffer:ByteArray, finalBytes:Int)
    {
        val tmpMessage = String(buffer,0,finalBytes)
        val actionRecieved = actionFactory.getActionFromStringFromServer(tmpMessage)
        actionRecieved.executeAction()
    }

    private fun startExchangingMessages() {
        val handler = Handler(Looper.getMainLooper())

        executorRead.execute(kotlinx.coroutines.Runnable //start reading thread
        {
            kotlin.run {
                readingProcess(handler)
            }
        })

        executorWrite.execute(kotlinx.coroutines.Runnable //start writing thread
        {
            kotlin.run {
                writingProcess()
            }
        })
    }
    private fun readingProcess(handler: Handler) {
        val buffer = ByteArray(2048)
        var byte:Int
        while (!socket.isClosed){
            try{
                byte = inputStream.read(buffer)
                if(byte>0){
                    val finalBytes = byte
                    handler.post {
                        kotlin.run {
                            handleMessage(buffer, finalBytes)
                        }
                    }
                }
            }catch (ex:IOException){
                ex.printStackTrace()
            }
        }
    }

    private fun writingProcess() {
        while (!socket.isClosed) {
            if(sendingQueue.isNotEmpty()) {
                try {
                    val data:ByteArray = sendingQueue.pop()
                    write(data)
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
            }
        }
    }

    private fun write(byteArray: ByteArray){
        try {
            outputStream.write(byteArray)
            outputStream.flush()
        }catch (ex: IOException){
            ex.printStackTrace()
        }
    }

    fun sendMessage(msg: String)
    {
        val dataToSend:ByteArray = msg.encodeToByteArray()
        sendingQueue.add(dataToSend)
    }
}