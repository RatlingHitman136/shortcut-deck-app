package com.example.blankapptest

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.example.blankapptest.shortcutclasses.ShortCutBase
import com.example.blankapptest.shortcutclasses.ShortCutButton

class IdleActivity : AppCompatActivity() {

    private lateinit var tvMessageBox:TextView
    private lateinit var client:ClientClass
    private lateinit var gvButtonsHolder:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvMessageBox = findViewById<TextView>(R.id.tvMessageBox)
        gvButtonsHolder = findViewById<RecyclerView>(R.id.rvButtonHolder)
        val numberOfColumns:Int = 3
        val manager = GridLayoutManager(this,numberOfColumns)

        manager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return 3 - position % 3
            }
        }

        gvButtonsHolder.layoutManager = manager
        val buttonsGridAdapter = ButtonsGridAdapter(this,createTestButtons())
        gvButtonsHolder.adapter = buttonsGridAdapter

        connect()
    }

    private fun createTestButtons() : MutableList<ShortCutBase> {
        return mutableListOf(
            ShortCutButton("1") { s: String -> send(s) },
            ShortCutButton("2") { s: String -> send(s) },
            ShortCutButton("3") { s: String -> send(s) },
            ShortCutButton("4") { s: String -> send(s) },
            ShortCutButton("5") { s: String -> send(s) },
            ShortCutButton("6") { s: String -> send(s) },
            ShortCutButton("7") { s: String -> send(s) },
            ShortCutButton("8") { s: String -> send(s) },
            ShortCutButton("9") { s: String -> send(s) },
            ShortCutButton("10") { s: String -> send(s) },
            ShortCutButton("11") { s: String -> send(s) },
            ShortCutButton("12") { s: String -> send(s) },
            ShortCutButton("13") { s: String -> send(s) },
            ShortCutButton("14") { s: String -> send(s) },
            ShortCutButton("15") { s: String -> send(s) },
            ShortCutButton("16") { s: String -> send(s) }
        )
    }


    private fun connect()
    {
        client = ClientClass("192.168.178.41") {msg:String -> handleRecievedMessage(msg)}
        client.start()
    }


    private fun send(msg: String) {
        client.sendMessage(msg + "\n")
    }

    @SuppressLint("SetTextI18n")
    private fun handleRecievedMessage(message:String)
    {
        Log.i("client class", message)
        tvMessageBox.text = "${tvMessageBox.text} \n $message"
    }


}