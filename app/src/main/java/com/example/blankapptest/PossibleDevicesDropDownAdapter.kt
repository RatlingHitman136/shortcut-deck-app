package com.example.blankapptest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.blankapptest.networking.LocalNetworkScanner

class PossibleDevicesDropDownAdapter(
    context: Context
):BaseAdapter() {
    private var deviceDataList:MutableList<LocalNetworkScanner.DeviceData> = mutableListOf()
    private val inflater:LayoutInflater = LayoutInflater.from(context)
    override fun getCount(): Int {
        return deviceDataList.count()
    }

    override fun getItem(p0: Int): Any {
        return deviceDataList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view:View = p1 ?: inflater.inflate(R.layout.possible_device_layout, p2, false)

        val textView = view.findViewById<TextView>(R.id.tvDeviceName)
        textView.text = deviceDataList[p0].ipAddress
        return view
    }

    fun updatePossibleDevicesList(possibleDevicesList: MutableList<LocalNetworkScanner.DeviceData>) {
        if(deviceDataList != possibleDevicesList) {
            deviceDataList = possibleDevicesList
            notifyDataSetChanged()
        }
    }
}