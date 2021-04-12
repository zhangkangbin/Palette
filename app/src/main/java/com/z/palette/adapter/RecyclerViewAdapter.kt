package com.z.palette.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.z.palette.BitmapUtils
import com.z.palette.R

/**
 * @author zhangkb
 */
class RecyclerViewAdapter(//  private List<Palette.Swatch> swatchList;
        private val paletteColorsBeans: List<PaletteColorsBean>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    var context: Context? = null
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        context = viewGroup.context
        return ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.adapter_main, viewGroup, false))
    }

    private var mHeight: Int = 0;
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        if (null != paletteColorsBeans[i].color) {
            viewHolder.mColor.visibility = View.VISIBLE
            viewHolder.mView.visibility = View.GONE
            viewHolder.mColor.setBackgroundColor(paletteColorsBeans[i].color.rgb)
            viewHolder.mText.text = paletteColorsBeans[i].colorText
        } else {

            viewHolder.mColor.visibility = View.GONE
            viewHolder.mView.visibility = View.VISIBLE
            //viewHolder.mView.maxHeight=paletteColorsBeans[i].bitmap.
            context?.let { Glide.with(it).load((paletteColorsBeans[i].bitmap)).into(viewHolder.mView) }
            //viewHolder.mView.setImageBitmap(paletteColorsBeans[i].bitmap)
        }
        viewHolder.mColor.setOnLongClickListener {
            //获取剪贴板管理器：
            val cm = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val mClipData = ClipData.newPlainText("Label", paletteColorsBeans[i].colorText)
            cm.setPrimaryClip(mClipData)
            Toast.makeText(context, "已复制到剪切板！", Toast.LENGTH_SHORT).show()
            false
        }
        viewHolder.mView.setOnLongClickListener {
            if (null != paletteColorsBeans[i].bitmap) {

                //获取剪贴板管理器：
                val cm = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val mClipData = ClipData.newPlainText("Label", paletteColorsBeans[i].colorText)
                cm.setPrimaryClip(mClipData);

                BitmapUtils.saveImage(paletteColorsBeans[i].bitmap, context)
                Toast.makeText(context, "保存成功！", Toast.LENGTH_SHORT).show()
            }
            false
        }
    }

    override fun getItemCount(): Int {
        return paletteColorsBeans.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        public val mView: ImageView
        public val mColor: ImageView
        public val mText: TextView

        init {
            mView = itemView.findViewById(R.id.image)
            mText = itemView.findViewById(R.id.text)
            mColor = itemView.findViewById(R.id.imageColor)
        }
    }

}