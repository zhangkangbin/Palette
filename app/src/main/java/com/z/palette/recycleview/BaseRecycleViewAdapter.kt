package com.z.palette.recycleview

import android.view.LayoutInflater
import android.view.View
import com.z.palette.adapter.PaletteColorsBean


import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView


/**
 * @author zhangkb
 */
abstract  class BaseRecycleViewAdapter<T>(private val mLayout:Int,private val mList: List<T>) :
        RecyclerView.Adapter<BaseViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view=LayoutInflater.from(parent.getContext()).inflate(mLayout, parent, false)
        return BaseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }




}