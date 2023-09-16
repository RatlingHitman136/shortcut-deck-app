package com.example.blankapptest.shortcutclasses

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.blankapptest.R
import com.example.blankapptest.shortcutclasses.shortcuttypes.ShortCutBase
import com.example.blankapptest.shortcutclasses.shortcuttypes.ShortCutButton
import com.example.blankapptest.shortcutclasses.shortcuttypes.ShortCutSeekBar
import java.lang.Exception


class ShortCutAdapter(
    context:Context
) : RecyclerView.Adapter<ShortCutBase.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val shortCutList: MutableList<ShortCutBase> = mutableListOf()

    override fun getItemViewType(position: Int): Int {
        return when (shortCutList[position]) {
            is ShortCutButton -> 1
            is ShortCutSeekBar -> 2
            else -> 0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortCutBase.ViewHolder {
        when (viewType) {
            0->{
                val view = inflater.inflate(R.layout.empty_shortcut_layout, parent, false)
                return ShortCutBase.ViewHolder(view)
            }
            1 -> {
                val view = inflater.inflate(R.layout.button_shortcut_layout, parent, false)
                return ShortCutButton.ButtonViewHolder(view)
            }

            2 -> {
                val view = inflater.inflate(R.layout.seekbar_shortcut_layout, parent, false)
                return ShortCutSeekBar.SeekBarViewHolder(view)
            }
        }
        throw Exception("wrong type")
    }


    override fun getItemCount(): Int {
        return shortCutList.count()
    }

    override fun onBindViewHolder(holder: ShortCutBase.ViewHolder, position: Int) {
        shortCutList[position].initShortCutViewGroup(holder)
    }

    fun addShortCut(shortCutBase: ShortCutBase) {
        shortCutList.add(shortCutBase)
        notifyItemInserted(shortCutList.count() - 1)
    }

    fun addShortCut(shortCutCollection: Collection<ShortCutBase>) {
        shortCutList.addAll(shortCutCollection)
        for (i in shortCutList.count() - shortCutCollection.count() until shortCutList.count())
            notifyItemInserted(i)
    }

    fun setShortCutsFromProfile(shortCutProfile: ShortCutProfile)
    {
        shortCutList.clear()
        shortCutList.addAll(shortCutProfile.getShortCuts())
        notifyDataSetChanged()
    }
}