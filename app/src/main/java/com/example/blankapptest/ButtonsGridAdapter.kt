package com.example.blankapptest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton


class ButtonsGridAdapter(
    context:Context,
    private val shortCutList: MutableList<ShortCutBase>
) : BaseAdapter(){

    val inflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return shortCutList.count()
    }

    override fun getItem(i: Int): Any {
        return shortCutList[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, convertView: View?, viewGroup: ViewGroup): View {
        val view:View
        if(convertView == null)
            view = inflater.inflate(R.layout.button_layout,null)
        else
            view = convertView

        shortCutList[i].initShortCutViewGroup(view)
        return view
    }


}