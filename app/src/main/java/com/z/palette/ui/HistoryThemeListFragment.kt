package com.z.palette.ui

import android.net.Uri
import android.os.Environment
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.z.palette.R
import com.z.palette.base.BaseFragment
import com.z.palette.recycleview.BaseRecycleViewAdapter
import com.z.palette.recycleview.BaseViewHolder
import java.io.File


/**
 * @author by zhangkangbin
 * on 2020/5/13
 * 历史
 */
class HistoryThemeListFragment : BaseFragment() {
    override fun initView(view: View) {
        val recyclerView=view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager= LinearLayoutManager(context)

       val dirs= ContextCompat.getExternalFilesDirs(context!!,Environment.DIRECTORY_PICTURES)

        val list=dirs.asList()
        var dataList= listOf<File>()
        if(list.isNotEmpty()){
            dataList=list.get(0).listFiles().toList()
        }

       val adapter= object  :BaseRecycleViewAdapter<File>(R.layout.history_theme_list_adapter,dataList){
           override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {

               val uri = Uri.fromFile(dataList.get(position))
               holder.setImageView(R.id.image,uri)
           }


       }

        recyclerView.adapter=adapter;
    }

    override fun getLayoutId(): Int {

        return R.layout.fragment_history_theme_list
    }


}