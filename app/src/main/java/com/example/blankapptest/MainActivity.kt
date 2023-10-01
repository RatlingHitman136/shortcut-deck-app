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

    private lateinit var tvMessageBox: TextView //TODO(only for logging and testing)
    private var client: ClientClass? = null

    private lateinit var rvButtonsHolder: RecyclerView
    private lateinit var sPossibleDevices: Spinner

    private lateinit var shortCutProfileManager: ShortCutProfileManager
    private lateinit var possibleDevicesManager: PossibleDevicesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initAllViews()
        shortCutProfileManager = ShortCutProfileManager(this, rvButtonsHolder)
        possibleDevicesManager = PossibleDevicesManager(this,
                                                        sPossibleDevices,
                                                        1000)
        possibleDevicesManager.startScanningForNewDevices()
    }

    private fun initAllViews() {
        assignAllViewReferences()

        //setting up grid manager
        //TODO(make it not hardcoded)
        val numberOfColumns: Int = 4
        val manager = CustomStaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL)
        manager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        rvButtonsHolder.layoutManager = manager
    }

    private fun assignAllViewReferences() {
        tvMessageBox = findViewById<TextView>(R.id.tvMessageBox)
        rvButtonsHolder = findViewById<RecyclerView>(R.id.rvButtonHolder)
        sPossibleDevices = findViewById<Spinner>(R.id.sPossibleDevices)
    }

    fun connect(deviceData: LocalNetworkScanner.DeviceData) {
        client?.close()
        client = ClientClass(deviceData, this)
        shortCutProfileManager.notifyNewClientConnected(client)
        client!!.start()
        client?.sendMessage(ActionFactory(this).getStringFromActionFromClient(ActionProfilesRequestSend()))
    }
    fun disconnect()
    {
        client?.close()
        shortCutProfileManager.clearProfiles()
    }

    fun getShortCutProfileManager(): ShortCutProfileManager {
        return shortCutProfileManager
    }
}