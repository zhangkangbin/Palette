package com.z.palette.recycleview

import android.net.Uri
import android.text.TextUtils
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BaseViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {

    private var mViewArray: SparseArray<View>? = null


    init {
        mViewArray = SparseArray()
    }


    fun <T : View?> getView(id: Int): T {
        var v: View? = mViewArray?.get(id)
        if (v == null) {
            v = itemView.findViewById(id)
            mViewArray?.put(id, v)
        }
        return v as T
    }
    fun setTextView(id: Int, s: String?): TextView? {
        val textView = getView<TextView>(id)
        if (!TextUtils.isEmpty(s)) {
            textView.text = s
        }
        return textView
    }
    fun setImageView(id: Int, uri: Uri?): ImageView? {
        if(uri==null) return null

        val imageView = getView<ImageView>(id)
        imageView.setImageURI(uri)
        return imageView
    }

}