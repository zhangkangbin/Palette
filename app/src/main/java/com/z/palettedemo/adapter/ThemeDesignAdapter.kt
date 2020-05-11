package com.z.palettedemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.z.palettedemo.R


/**
 * @author by zhangkangbin
 * on 2020/5/11
 * 界面说明
 */
class ThemeDesignAdapter(private val imageList: List<String>) : RecyclerView.Adapter<ThemeDesignAdapter.ViewHolder>() {


    private var mContext: Context? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
         var mImageView: ImageView ?

        init {
            mImageView = view.findViewById(R.id.image)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        if (viewType == 0) {

            return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_theme_select_image, parent, false))
        } else {


        /*    val view = LayoutInflater.from(mContext).inflate(R.layout.head_theme_list, parent, false)
            view.findViewById<TextView>(R.id.themeImageReference).setOnClickListener {
                mSelectImageListener?.onClick(it)
            }
*/
            val view  = LayoutInflater.from(mContext).inflate(R.layout.head_theme_list, parent, false)
            view.findViewById<TextView>(R.id.themeImageReference).setOnClickListener { v: View? ->
                mSelectImageListener?.onClick(v)
            }

            return ViewHolder(view)
        }


    }

    private var mSelectImageListener: View.OnClickListener? = null


    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            1
        } else {
            0
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) {
            manager.spanSizeLookup = object : SpanSizeLookup() {

                override fun getSpanSize(position: Int): Int {
                    //注意，这里的position返回的是item在recyclerview中的位置，不是item的数据在数据列表中的位置，是把header和footer算进去的
                    return if (position == 0) {
                        manager.getSpanCount()
                    } else {
                        1

                    }

                }
            }
        }
    }

    override fun getItemCount(): Int {

        return imageList.size
    }
    fun setSelectImage(selectImage: View.OnClickListener?) {
        this.mSelectImageListener = selectImage
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position != 0) {
         //   Glide.with(mContext).load(imageList.get(position)).into(holder.mImageView)
            Glide.with(mContext!!).load(imageList[position]).into(holder.mImageView!!)
        }

    }
}
