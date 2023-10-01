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
    private var updatingExecutor:ExecutorService = Executors.newSingleThreadExecutor()
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
        if (isUpdatingPossibleDevices)
            return
        isUpdatingPossibleDevices = true
        updatingExecutor.execute {
            while (isUpdatingPossibleDevices) {
                Thread.sleep(timeBetweenUpdateOfPossibleDevices)
                localNetworkScanner.startGeneralScan()
            }
        }
    }
    fun stopUpdatingPossibleDevices() {
        if(!isUpdatingPossibleDevices)
            return
    }

    //TODO(make checking of already founded devices more efficient)
    private fun updatePossibleDevices() {
        try {
//            val tmpCopy = localNetworkScanner.updateConnectionWithSpecifiedDevices(possibleDevicesList)
//            possibleDevicesList.clear()
//            possibleDevicesList.addAll(tmpCopy)
//            possibleDevicesDropDownAdapter.updatePossibleDevicesList(possibleDevicesList)

            Thread.sleep(timeBetweenUpdateOfPossibleDevices)
        } catch (e: Exception) {
            println(e.message)
        }
    }

    //TODO(move checking selected device on connection status to client class)
    private fun handleFoundPossibleDeviceConnections(newFoundedPossibleDeviceData: MutableList<LocalNetworkScanner.DeviceData>) {
        val oldPossibleDevicesList = possibleDevicesList.toMutableList()
        possibleDevicesList = newFoundedPossibleDeviceData
        possibleDevicesDropDownAdapter.updatePossibleDevicesList(newFoundedPossibleDeviceData)

        if(sPossibleDevices.selectedItemId != INVALID_ROW_ID) {
            val oldSelectedDevice = oldPossibleDevicesList[sPossibleDevices.selectedItemId.toInt()]
            if (!possibleDevicesList.contains(oldSelectedDevice)) {
                if (possibleDevicesList.isNotEmpty()) {
                    sPossibleDevices.setSelection(0, true)
                    handleSelectedNewDevice(possibleDevicesList[0])
                } else {
                    handleSelectedNothing()
                }
            }
            else
            {
                val newIndexOfSelectedDevice = possibleDevicesList.indexOf(oldSelectedDevice)
                sPossibleDevices.setSelection(newIndexOfSelectedDevice,true)
            }
        }
    }
    private fun handleSelectedNewDevice(deviceData: LocalNetworkScanner.DeviceData) {
        mainActivity.connect(deviceData)
    }
    private fun handleSelectedNothing() {
        mainActivity.disconnect()
    }
}