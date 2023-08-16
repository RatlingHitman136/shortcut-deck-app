package com.example.blankapptest

import android.os.Handler
import android.os.Looper
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.Executors


class ClientClass(hostAddress: String,  handleMessage: (message:String) -> Unit): Thread() {

    var hostAddress: String? = hostAddress
    var handleMessageOuterFunction = handleMessage
    lateinit var inputStream: InputStream
    lateinit var outputStream: OutputStream
    lateinit var socket: Socket


    private fun write(byteArray: ByteArray){
        try {
            outputStream.write(byteArray)
        }catch (ex: IOException){
            handleMessageOuterFunction("!exception! " + ex.message)
            ex.printStackTrace()
        }
    }

    override fun run() {
        try {
            socket = Socket()
            socket.connect(InetSocketAddress(hostAddress,8888),500)
            inputStream = socket.getInputStream()
            outputStream = socket.getOutputStream()

            //start reading messages if connection is successful
            startReadingMessages()
        }catch (ex:IOException){
            ex.printStackTrace()
        }
    }

    private fun handleMessage(buffer:ByteArray, finalBytes:Int)
    {
        val tmpMessage = String(buffer,0,finalBytes)
        handleMessageOuterFunction(tmpMessage)
    }

    private fun startReadingMessages()
    {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        //start reading thread
        executor.execute(kotlinx.coroutines.Runnable {
            kotlin.run {
                val buffer =ByteArray(1024)
                var byte:Int
                while (true){
                    try{
                        //reading proccess
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
                        handleMessageOuterFunction("!exception! "+ ex.message)
                        ex.printStackTrace()
                    }
                }
            }
        })
    }

    fun sendMessage(msg: String)
    {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute(kotlinx.coroutines.Runnable {
            kotlin.run {
                try {
                    handleMessageOuterFunction("found Item to send: ${msg}")
                    write((msg + "\n").encodeToByteArray())
                }catch (ex:IOException){
                    handleMessageOuterFunction("!exception! " + ex.message)
                    ex.printStackTrace()
                }
            }
        })
    }
}