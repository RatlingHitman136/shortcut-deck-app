package com.example.blankapptest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton


class ButtonsGridAdapter(
    context:Context,
    private val shortcutTagsArray: MutableList<String>,
    private val onButtonPressed: (view:View, tag:String) -> Unit
) : BaseAdapter(){

    val inflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return shortcutTagsArray.count()
    }

    override fun getItem(i: Int): Any {
        return shortcutTagsArray[i]
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
        //val imgButton = convertView.findViewById<ImageButton>(R.id.ibShortCut)
        //imgButton.setOnClickListener { buttonView: View -> onButtonPressed(buttonView, shortcutTagsArray[i]) }
        return view
    }


}