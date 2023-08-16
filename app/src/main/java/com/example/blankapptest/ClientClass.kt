package com.example.blankapptest

import android.os.Handler
import android.os.Looper
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.util.LinkedList
import java.util.concurrent.Executors


class ClientClass(hostAddress: String,  handleMessage: (message:String) -> Unit): Thread() {

    private var hostAddress: String? = hostAddress
    private var handleMessageOuterFunction = handleMessage
    private lateinit var inputStream: InputStream
    private lateinit var outputStream: OutputStream
    private lateinit var socket: Socket

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

    override fun run() {
        try {
            socket = Socket()
            socket.connect(InetSocketAddress(hostAddress,8888),500)
            inputStream = socket.getInputStream()
            outputStream = socket.getOutputStream()

            //start reading and wending messages if connection is successful
            startExchangingMessages()
        }catch (ex:IOException){
            handleMessageOuterFunction(ex.message.toString())
            ex.printStackTrace()
        }
    }

    private fun handleMessage(buffer:ByteArray, finalBytes:Int)
    {
//        val tmpMessage = String(buffer,0,finalBytes)
//        handleMessageOuterFunction(tmpMessage)

    }

    private fun startExchangingMessages() {
        val executorRead = Executors.newSingleThreadExecutor()
        val executorWrite = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        executorRead.execute(kotlinx.coroutines.Runnable //start reading thread
        {
            kotlin.run {
                handleMessageOuterFunction("started reading thread")
                readingProcess(handler)
            }
        })

        executorWrite.execute(kotlinx.coroutines.Runnable //start writing thread
        {
            kotlin.run {
                handleMessageOuterFunction("started writing thread")
                writingProcess()
            }
        })
    }
    private fun readingProcess(handler: Handler) {
        val buffer = ByteArray(1024)
        var byte:Int
        while (true){
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
                handleMessageOuterFunction(ex.message.toString())
                ex.printStackTrace()
            }
        }
    }

    private fun writingProcess() {
        while (true) {
            if(sendingQueue.isNotEmpty()) {
                try {
                    val data:ByteArray = sendingQueue.pop()
                    handleMessageOuterFunction("found Item to send")
                    write(data)
                } catch (ex: IOException) {
                    handleMessageOuterFunction(ex.message.toString())
                    ex.printStackTrace()
                }
            }
        }
    }


    fun sendMessage(msg: String)
    {
        val dataToSend:ByteArray = msg.encodeToByteArray()
        sendingQueue.add(dataToSend)
        handleMessageOuterFunction("added message to queue")
    }
}