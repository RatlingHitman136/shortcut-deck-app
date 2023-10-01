package com.example.blankapptest

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.blankapptest.actions.ActionFactory
import com.example.blankapptest.actions.actiontypes.ActionProfilesRequestSend
import com.example.blankapptest.networking.ClientClass
import com.example.blankapptest.networking.LocalNetworkScanner
import com.example.blankapptest.shortcutclasses.ShortCutProfileManager


class MainActivity : AppCompatActivity() {

    lateinit var tvMessageBox:TextView
    private var client: ClientClass? = null

    private lateinit var rvButtonsHolder:RecyclerView
    private lateinit var sPossibleDevices: Spinner
    private lateinit var possibleDevicesDropDownAdapter: PossibleDevicesDropDownAdapter

    private lateinit var shortCutProfileManager: ShortCutProfileManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initAllViews()
        shortCutProfileManager = ShortCutProfileManager(this, rvButtonsHolder)
        val localNetworkScanner = LocalNetworkScanner(
            this,
            8888,
            R.string.connection_password.toString(),
            R.string.connection_password.toString().reversed(),
            "test device",
            200,
        ) { possibleDevices: LocalNetworkScanner.DeviceData -> handleFoundPossibleDeviceConnection(possibleDevices) }
        localNetworkScanner.startGeneralScan(8)

    }
    private fun initAllViews() {
        assignAllViewReferences()

        //setting up grid manager
        val numberOfColumns: Int = 4
        val manager = CustomStaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL)
        manager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        rvButtonsHolder.layoutManager = manager

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
    private fun assignAllViewReferences() {
        tvMessageBox = findViewById<TextView>(R.id.tvMessageBox)
        rvButtonsHolder = findViewById<RecyclerView>(R.id.rvButtonHolder)
        sPossibleDevices = findViewById<Spinner>(R.id.sPossibleDevices)
    }
    private fun handleFoundPossibleDeviceConnection(device:LocalNetworkScanner.DeviceData) {
        possibleDevicesDropDownAdapter.tryAddNewPossibleDevice(device)
    }
    private fun handleSelectedNewDevice(deviceData: LocalNetworkScanner.DeviceData) {
        connect(deviceData.ipAddress)
    }
    private fun connect(address:String) {
        client?.close()
        client = ClientClass(address, this)
        shortCutProfileManager.notifyNewClientConnected(client)
        client!!.start()
        client?.sendMessage(ActionFactory(this).getStringFromActionFromClient(ActionProfilesRequestSend()))
    }
    fun getShortCutProfileManager():ShortCutProfileManager
    {
        return shortCutProfileManager
    }


}