package com.z.palettedemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.z.palettedemo.R
import com.z.palettedemo.bean.ThemeDataSaveBean

/**
 * @author by zhangkangbin
 * on 2020/5/12
 * 界面说明
 */
class ThemeLocalListAdapter(private val mThemeDataSaveBean: List<ThemeDataSaveBean>) : RecyclerView.Adapter<ThemeLocalListAdapter.ViewHolder>() {


    private var context: Context? = null

    fun setSelectClickListener(selectImage: OnClickListener<ThemeDataSaveBean>?) {
        this.selectImage = selectImage
    }

    private var selectImage: OnClickListener<ThemeDataSaveBean> ? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        context = viewGroup.context
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_local_list, viewGroup, false))
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.theme.setText(mThemeDataSaveBean[i].theme)
        viewHolder.linearLayout.setOnClickListener {
            selectImage?.callData(mThemeDataSaveBean[i])
        }

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mView = itemView.findViewById<ImageView>(R.id.image)
        val theme = itemView.findViewById<TextView>(R.id.text)
        val linearLayout = itemView.findViewById<LinearLayout>(R.id.linearLayout)

    }

    override fun getItemCount(): Int {
        return mThemeDataSaveBean.size
    }

    interface OnClickListener<T> {
        fun callData(data: T?)
    }
}

