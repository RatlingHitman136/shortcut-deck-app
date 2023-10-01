package com.example.blankapptest

import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import com.example.blankapptest.networking.LocalNetworkScanner
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

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
        16,
    ) { possibleDevices: MutableList<LocalNetworkScanner.DeviceData> -> handleFoundPossibleDeviceConnection(possibleDevices) }

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
    private fun startUpdatingPossibleDevices() {
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
    private fun stopUpdatingPossibleDevices() {
        if(!isUpdatingPossibleDevices)
            return
    }
    private fun updatePossibleDevices() {
        try {
//            val tmpCopy = localNetworkScanner.updateConnectionWithSpecifiedDevices(possibleDevicesList)
//            possibleDevicesList.clear()
//            possibleDevicesList.addAll(tmpCopy)
//            possibleDevicesDropDownAdapter.updatePossibleDevicesList(possibleDevicesList)

            Thread.sleep(timeBetweenUpdateOfPossibleDevices)
        } catch (e: Exception) {
            Log.e(TAG,Log.getStackTraceString(e));
        }
    }
    private fun handleFoundPossibleDeviceConnection(newFoundedPossibleDevicesData: MutableList<LocalNetworkScanner.DeviceData>) {
        val newPossibleDevices = mutableListOf<LocalNetworkScanner.DeviceData>()
        var isDevicesListModified = false
        for (oldDevice in possibleDevicesList) {
            //TODO("can be optimized")
            if (newFoundedPossibleDevicesData.contains(oldDevice)) {
                newPossibleDevices.add(oldDevice)
                newFoundedPossibleDevicesData.remove(oldDevice)
            }
            else
                isDevicesListModified = true
        }

        for (newDevice in newFoundedPossibleDevicesData) {
            newPossibleDevices.add(newDevice)
            isDevicesListModified = true
        }

        var newSelectedIndex:Int = 0

        if(sPossibleDevices.selectedItemId != AdapterView.INVALID_ROW_ID && isDevicesListModified) {
            val selectedOldDeviceId = sPossibleDevices.selectedItemId.toInt()
            val selectedOldDevice = possibleDevicesList[selectedOldDeviceId]
            if (newPossibleDevices.contains(selectedOldDevice))
            {
                newSelectedIndex = newPossibleDevices.indexOf(selectedOldDevice)
                sPossibleDevices.setSelection(newSelectedIndex)
            }
        }
        possibleDevicesList = newPossibleDevices
        if(isDevicesListModified) {
            possibleDevicesDropDownAdapter.updatePossibleDevicesList(possibleDevicesList)
        }
        sPossibleDevices.setSelection(newSelectedIndex)
    }
    private fun handleSelectedNewDevice(deviceData: LocalNetworkScanner.DeviceData) {
        mainActivity.connect(deviceData)
    }
    private fun handleSelectedNothing() {
        mainActivity.disconnect()
    }
}