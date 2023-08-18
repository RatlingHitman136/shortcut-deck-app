package com.example.blankapptest

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.blankapptest.networking.ClientClass
import com.example.blankapptest.networking.LocalNetworkScanner
import com.example.blankapptest.shortcutclasses.ShortCutBase
import com.example.blankapptest.shortcutclasses.ShortCutButton
import com.example.blankapptest.shortcutclasses.ShortCutSeekBar


class IdleActivity : AppCompatActivity() {

    private lateinit var tvMessageBox:TextView
    private lateinit var client: ClientClass
    private lateinit var gvButtonsHolder:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvMessageBox = findViewById<TextView>(R.id.tvMessageBox)
        gvButtonsHolder = findViewById<RecyclerView>(R.id.rvButtonHolder)
        val numberOfColumns: Int = 4
        val manager = CustomStaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL)
        manager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS

        gvButtonsHolder.layoutManager = manager
        //gvButtonsHolder.addItemDecoration(GridSpanDecoration(R.dimen.buttons_margin))
        val buttonsGridAdapter = ButtonsGridAdapter(this, createTestButtons())
        gvButtonsHolder.adapter = buttonsGridAdapter

        val localNetworkScanner = LocalNetworkScanner(
            this,
            8888,
            "a1b2c3",
            "3c2b1a",
            "test device",
            200,
        ) { possibleDevices: LocalNetworkScanner.DeviceData -> handleFoundPossibleDeviceConnection(possibleDevices) }
        localNetworkScanner.start()

        //connect()
    }

    private fun createTestButtons() : MutableList<ShortCutBase> {
        return mutableListOf(
            ShortCutButton("1") { s: String -> send(s) },
            ShortCutButton("2") { s: String -> send(s) },
            ShortCutButton("3") { s: String -> send(s) },
            ShortCutSeekBar("4") { s: String -> send(s) },
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
            ShortCutButton("16") { s: String -> send(s) },
            ShortCutButton("17") { s: String -> send(s) }
        )
    }
    private fun handleFoundPossibleDeviceConnection(device:LocalNetworkScanner.DeviceData) {

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