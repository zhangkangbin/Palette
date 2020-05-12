package com.z.palettedemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.z.palettedemo.R
import com.z.palettedemo.bean.ThemeDataSaveBean

/**
 * @author by zhangkangbin
 * on 2020/5/12
 * 界面说明
 */
class ThemeLocalListAdapter (private val mThemeDataSaveBean: List<ThemeDataSaveBean>) : RecyclerView.Adapter<ThemeLocalListAdapter.ViewHolder>(){



    private var context: Context? = null

    fun setSelectImage(selectImage: View.OnClickListener?) {
        this.selectImage = selectImage
    }

    private var selectImage: View.OnClickListener? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        context = viewGroup.context
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_local_list, viewGroup, false))
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.theme.setText(mThemeDataSaveBean[i].theme)
    }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val mView = itemView.findViewById<ImageView>(R.id.image)
         val theme=itemView.findViewById<TextView>(R.id.text)

    }

    override fun getItemCount(): Int {
        return  mThemeDataSaveBean.size
    }
}