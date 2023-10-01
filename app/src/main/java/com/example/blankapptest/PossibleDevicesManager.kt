package com.example.blankapptest

import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import com.example.blankapptest.networking.LocalNetworkScanner
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class PossibleDevicesManager(
    private val mainActivity: MainActivity,
    sPossibleDevices: Spinner,
    private val timeBetweenUpdateOfPossibleDevices:Long,
) {
    private val possibleDevicesDropDownAdapter = PossibleDevicesDropDownAdapter(mainActivity)
    private val possibleDevicesList:MutableList<LocalNetworkScanner.DeviceData> = mutableListOf()
    private var isUpdatingPossibleDevices:Boolean = false
    private var updatingExecutor:ExecutorService = Executors.newSingleThreadExecutor()
    private val localNetworkScanner = LocalNetworkScanner(
        mainActivity,
        8888,
        R.string.connection_password.toString(),
        R.string.connection_password.toString().reversed(),
        "test device",
        200,
    ) { possibleDevices: LocalNetworkScanner.DeviceData -> handleFoundPossibleDeviceConnection(possibleDevices) }

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

    fun startScanningForNewDevices()
    {
        localNetworkScanner.startGeneralScan(8)
        startUpdatingPossibleDevices()
    }

    fun startUpdatingPossibleDevices() {
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
    }

    private fun updatePossibleDevices() {
        try {
            val tmpCopy = localNetworkScanner.updateConnectionWithSpecifiedDevices(possibleDevicesList)
            possibleDevicesList.clear()
            possibleDevicesList.addAll(tmpCopy)
            possibleDevicesDropDownAdapter.updatePossibleDevicesList(possibleDevicesList)

            Thread.sleep(timeBetweenUpdateOfPossibleDevices)
        } catch (e: Exception) {
            println(e.message)
        }
    }

    private fun handleFoundPossibleDeviceConnection(possibleDeviceData: LocalNetworkScanner.DeviceData) {
        possibleDevicesList.add(possibleDeviceData)
        possibleDevicesDropDownAdapter.updatePossibleDevicesList(possibleDevicesList)
    }

    private fun handleSelectedNewDevice(deviceData: LocalNetworkScanner.DeviceData) {
        mainActivity.connect(deviceData)
    }
    private fun handleSelectedNothing()
    {
        mainActivity.disconnect()
    }

}