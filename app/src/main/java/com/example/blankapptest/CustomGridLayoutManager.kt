package com.example.blankapptest

import androidx.recyclerview.widget.StaggeredGridLayoutManager

class CustomStaggeredGridLayoutManager(numberOfColumns:Int, orientation:Int):StaggeredGridLayoutManager(numberOfColumns,orientation) {

    private var isVertScrollEnabled = false

    fun setVertScrollEnabled(flag: Boolean) {
        this.isVertScrollEnabled = flag
    }

    override fun canScrollVertically(): Boolean {
        return isVertScrollEnabled && super.canScrollVertically()
    }
}