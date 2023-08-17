package com.example.blankapptest

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.TextView

class IdleActivity : AppCompatActivity() {

    private lateinit var btnSend:Button
    private lateinit var edMessageToSend:EditText
    private lateinit var tvMessageBox:TextView
    private lateinit var client:ClientClass
    private lateinit var gvButtonsHolder:GridView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnSend = findViewById<Button>(R.id.btnSend)
        edMessageToSend = findViewById<EditText>(R.id.edMessageToSend)
        tvMessageBox = findViewById<TextView>(R.id.tvMessageBox)
        gvButtonsHolder = findViewById<GridView>(R.id.gvButtonHolder)

        val buttonsGridAdapter = ButtonsGridAdapter(this,mutableListOf("1","2","3","4",
                                                                              "5","6","7","8",
                                                                              "9","10","11", "12",
                                                                              "13","14","15", "16"))
                                                                              {view:View, tag:String -> buttonWithTagPressed(view, tag)}
        gvButtonsHolder.adapter = buttonsGridAdapter

        btnSend.setOnClickListener { _ ->
            run {
                send(edMessageToSend.text.toString())
                edMessageToSend.text.clear()
            }
        }

        connect()
    }

    private fun buttonWithTagPressed(view: View, tag:String)
    {
        //handleRecievedMessage("button ${tag} is pressed")
        send("button ${tag} is pressed")
    }

    private fun connect()
    {
        client = ClientClass("192.168.178.41") {msg:String -> handleRecievedMessage(msg)}
        client.start()
    }

    private fun send(msg: String)
    {
        client.sendMessage(msg + "\n")
    }

    @SuppressLint("SetTextI18n")
    private fun handleRecievedMessage(message:String)
    {
        Log.i("client class", message)
        tvMessageBox.text = "${tvMessageBox.text} \n $message"
    }


}