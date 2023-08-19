package com.example.blankapptest

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
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


class MainActivity : AppCompatActivity() {

    private lateinit var tvMessageBox:TextView
    private var client: ClientClass =  ClientClass { msg: String -> handleRecievedMessage(msg) }
    private lateinit var gvButtonsHolder:RecyclerView
    private lateinit var buttonsGridAdapter:ButtonsGridAdapter
    private lateinit var sPossibleDevices: Spinner
    private lateinit var possibleDevicesDropDownAdapter: PossibleDevicesDropDownAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initAllViews()

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

    private fun getAllViewReferences() {
        tvMessageBox = findViewById<TextView>(R.id.tvMessageBox)
        gvButtonsHolder = findViewById<RecyclerView>(R.id.rvButtonHolder)
        sPossibleDevices = findViewById<Spinner>(R.id.sPossibleDevices)
    }

    private fun initAllViews() {
        getAllViewReferences()

        //setting up grid manager
        val numberOfColumns: Int = 4
        val manager = CustomStaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL)
        manager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        gvButtonsHolder.layoutManager = manager

        //setting up grid adapter
        buttonsGridAdapter = ButtonsGridAdapter(this)
        gvButtonsHolder.adapter = buttonsGridAdapter

        //setting up device adapter
        possibleDevicesDropDownAdapter = PossibleDevicesDropDownAdapter(this)
        sPossibleDevices.adapter = possibleDevicesDropDownAdapter

        sPossibleDevices.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                handleSelectedNewDevice(possibleDevicesDropDownAdapter.getItem(position) as LocalNetworkScanner.DeviceData)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // TODO your code here
            }
        }

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
        possibleDevicesDropDownAdapter.addNewPossibleDevice(device)
    }

    @SuppressLint("SetTextI18n")
    private fun handleRecievedMessage(message:String)
    {
        Log.i("client class", message)
        tvMessageBox.text = "${tvMessageBox.text} \n $message"
    }

    private fun handleSelectedNewDevice(deviceData: LocalNetworkScanner.DeviceData) {
        connect(deviceData.ipAddress)
    }


    private fun connect(address:String) {
        if (!client.isAlive) {
            client.hostAddress = address
            client.start()
        } else
            client.stopAndReconnect(address)

        buttonsGridAdapter.addShortCut(createTestButtons())
    }


    private fun send(msg: String) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
        client.sendMessage(msg + "\n")
    }
}