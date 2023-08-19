package com.example.blankapptest.networking

import android.os.Handler
import android.os.Looper
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.util.LinkedList
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class ClientClass(handleMessage: (message:String) -> Unit): Thread() {
    lateinit var hostAddress: String
    private var handleMessageOuterFunction = handleMessage
    private lateinit var inputStream: InputStream
    private lateinit var outputStream: OutputStream
    private lateinit var socket: Socket

    private val executorRead: ExecutorService = Executors.newSingleThreadExecutor()
    private val executorWrite: ExecutorService = Executors.newSingleThreadExecutor()

    private var sendingQueue = LinkedList<ByteArray>()

    private fun write(byteArray: ByteArray){
        try {
            outputStream.write(byteArray)
            outputStream.flush()
        }catch (ex: IOException){
            handleMessageOuterFunction(ex.message.toString())
            ex.printStackTrace()
        }
    }

    fun stopAndReconnect(hostAdress:String)
    {
        this.hostAddress = hostAdress
        try {
            socket.close()
            executorRead.shutdownNow()
            executorWrite.shutdownNow()
            sendingQueue.clear()

            connect()
            startExchangingMessages()
        }catch (ex:IOException){
            handleMessageOuterFunction(ex.message.toString())
            ex.printStackTrace()
        }
    }

    override fun run() {
        try {
            socket = Socket()
            connect()
            startExchangingMessages()
        }catch (ex:IOException){
            handleMessageOuterFunction(ex.message.toString())
            ex.printStackTrace()
        }
    }

    private fun connect()
    {
        val ip:String = hostAddress.split(":")[0]
        val port:Int = hostAddress.split(":")[1].toInt()
        socket.connect(InetSocketAddress(ip,port),500)
        inputStream = socket.getInputStream()
        outputStream = socket.getOutputStream()
    }

    private fun handleMessage(buffer:ByteArray, finalBytes:Int)
    {
        val tmpMessage = String(buffer,0,finalBytes)
        handleMessageOuterFunction(tmpMessage)
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
        val buffer = ByteArray(1024)
        var byte:Int
        while (socket.isConnected){
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
                handleMessageOuterFunction(ex.message.toString())//TODO
                ex.printStackTrace()
            }
        }
    }

    private fun writingProcess() {
        while (socket.isConnected) {
            if(sendingQueue.isNotEmpty()) {
                try {
                    val data:ByteArray = sendingQueue.pop()
                    write(data)
                } catch (ex: IOException) {
                    handleMessageOuterFunction(ex.message.toString())//TODO
                    ex.printStackTrace()
                }
            }
        }
    }


    fun sendMessage(msg: String)
    {
        val dataToSend:ByteArray = msg.encodeToByteArray()
        sendingQueue.add(dataToSend)
    }
}