package com.z.palette.recycleview

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecycleViewHelper {

    companion object{

        fun  initRecycleView(context:Activity,resId:Int):RecyclerView{
            val recyclerView=context.findViewById<RecyclerView>(resId)
            recyclerView.layoutManager=LinearLayoutManager(context)
            recyclerView.adapter
            return recyclerView
        }

    }


}