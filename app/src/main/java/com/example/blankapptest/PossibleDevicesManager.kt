package com.example.blankapptest

import android.media.midi.MidiDevice
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.INVALID_ROW_ID
import android.widget.Spinner
import com.example.blankapptest.networking.LocalNetworkScanner
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.max

class PossibleDevicesManager(
    private val mainActivity: MainActivity,
    private val sPossibleDevices: Spinner,
) {
    private val possibleDevicesDropDownAdapter = PossibleDevicesDropDownAdapter(mainActivity)

    private var possibleDevicesList:MutableList<LocalNetworkScanner.DeviceData> = mutableListOf()


    private var isUpdatingPossibleDevices:Boolean = false
    private lateinit var updatingExecutor:ExecutorService
    private val timeBetweenUpdateOfPossibleDevices:Long = 2000 //TODO(must be changed)
    private val localNetworkScanner = LocalNetworkScanner(
        mainActivity,
        8888,
        R.string.connection_password.toString(),
        R.string.connection_password.toString().reversed(),
        "test device",
        200,
        200,
        32,
    ) { possibleDevices: MutableList<LocalNetworkScanner.DeviceData> -> handleFoundPossibleDeviceConnections(possibleDevices) }

    init {
        sPossibleDevices.adapter = possibleDevicesDropDownAdapter

        sPossibleDevices.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                handleSelectedNewDevice(possibleDevicesList[position])
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                handleSelectedNothing()
            }
        }
    }

    fun startScanningForNewDevices() {
        localNetworkScanner.startGeneralScan()
        startUpdatingPossibleDevices()
    }

    fun startUpdatingPossibleDevices() {
        updatingExecutor = Executors.newSingleThreadExecutor()
        if (isUpdatingPossibleDevices)
            return
        isUpdatingPossibleDevices = true
        updatingExecutor.execute {
            while (isUpdatingPossibleDevices) {
                updatePossibleDevices()
            }
        }
    }
    fun stopUpdatingPossibleDevices() {
        if(!isUpdatingPossibleDevices)
            return
        isUpdatingPossibleDevices = false
        updatingExecutor.shutdown()
    }

    //TODO(make checking of already founded devices more efficient)
    private fun updatePossibleDevices() {
        try {
            Thread.sleep(timeBetweenUpdateOfPossibleDevices)
            val devicesDataNotToScan = mutableListOf<LocalNetworkScanner.DeviceData>()
            if(sPossibleDevices.selectedItemId != INVALID_ROW_ID)
            {
                devicesDataNotToScan.add(possibleDevicesList[sPossibleDevices.selectedItemId.toInt()])
            }
            localNetworkScanner.startGeneralScan(devicesDataNotToScan)
        } catch (e: Exception) {
            println(e.message)
        }
    }

    //TODO(move checking selected device on connection status to client class)
    private fun handleFoundPossibleDeviceConnections(newFoundedPossibleDeviceData: MutableList<LocalNetworkScanner.DeviceData>) {
        val oldPossibleDevicesList = possibleDevicesList.toMutableList()

        possibleDevicesList = newFoundedPossibleDeviceData

        if(sPossibleDevices.selectedItemId != INVALID_ROW_ID)
        {
            val oldSelectedDevice = oldPossibleDevicesList[sPossibleDevices.selectedItemId.toInt()]
            possibleDevicesList.remove(oldSelectedDevice)
            possibleDevicesList.add(0, oldSelectedDevice)
            possibleDevicesDropDownAdapter.updatePossibleDevicesList(possibleDevicesList)
            sPossibleDevices.setSelection(0, true)
        }
        else
            possibleDevicesDropDownAdapter.updatePossibleDevicesList(possibleDevicesList)
    }

    private fun handleSelectedNewDevice(deviceData: LocalNetworkScanner.DeviceData) {
        possibleDevicesList.remove(deviceData)
        possibleDevicesList.add(0,deviceData)
        possibleDevicesDropDownAdapter.updatePossibleDevicesList(possibleDevicesList)
        sPossibleDevices.setSelection(0,true)
        mainActivity.connect(deviceData)
    }
    private fun handleSelectedNothing() {
        mainActivity.disconnect()
    }
}