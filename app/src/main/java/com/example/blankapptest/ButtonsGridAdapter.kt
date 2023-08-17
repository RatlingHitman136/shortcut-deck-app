package com.example.blankapptest

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton


class ButtonsGridAdapter(
    private val shortcutTagsArray: MutableList<String>,
    private val onButtonPressed: (view:View, tag:String) -> Unit
) : BaseAdapter(){
    override fun getCount(): Int {
        return shortcutTagsArray.count()
    }

    override fun getItem(p0: Int): Any? {
        return null
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(i: Int, view: View, viewGroup: ViewGroup): View {
        val imgButton = view.findViewById<ImageButton>(R.id.ibShortCut)
        imgButton.setOnClickListener { buttonView: View -> onButtonPressed(buttonView, shortcutTagsArray[i]) }
        return view
    }


}