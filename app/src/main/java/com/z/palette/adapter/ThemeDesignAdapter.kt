package com.z.palette.adapter

import android.content.Context
import android.content.SharedPreferences
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.z.palette.R
import com.z.palette.bean.ThemeDataSaveBean
import com.z.palette.constant.Constant
import java.io.File
import java.io.ObjectOutputStream
import java.util.*
import kotlin.collections.LinkedHashSet


/**
 * @author by zhangkangbin
 * on 2020/5/11
 * 界面说明
 */
class ThemeDesignAdapter(private var imageList: LinkedHashSet<String>,private var themeDataSaveBean:ThemeDataSaveBean?) : RecyclerView.Adapter<ThemeDesignAdapter.ViewHolder>() {

    private var mSelectImageListener: View.OnClickListener? = null
    private var mPreferences: SharedPreferences? = null
    private var mContext: Context? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var edtTheme: EditText? = view.findViewById(R.id.edtTheme)
        var edtStyle: EditText? = view.findViewById(R.id.edtStyle)
        var edtStyleColors: EditText? = view.findViewById(R.id.edtStyleColors)
        var edtClothes: EditText? = view.findViewById(R.id.edtClothes)
        var imageView: ImageView? = view.findViewById(R.id.image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        return if (viewType == 0) {
            ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_theme_select_image, parent, false))
        } else {

            val view = LayoutInflater.from(mContext).inflate(R.layout.head_theme_list, parent, false)
            view.findViewById<TextView>(R.id.themeImageReference).setOnClickListener { v: View? ->
                mSelectImageListener?.onClick(v)
            }
            val holder = ViewHolder(view)
            initEditText(holder)

            holder
        }


    }

    private var mTheme: String? = null
    private var mStyle: String? = null
    private var mClothes: String? = null
    private var mStyleColors: String? = null

    private fun initEditText(holder: ViewHolder) {

        holder.edtTheme?.addTextChangedListener(ThemeTextWatcher(Constant.THEME_SAVE_TYPE_THEME))
        holder.edtStyle?.addTextChangedListener(ThemeTextWatcher(Constant.THEME_SAVE_TYPE_STYLE))
        holder.edtClothes?.addTextChangedListener(ThemeTextWatcher(Constant.THEME_SAVE_TYPE_CLOTHES))
        holder.edtStyleColors?.addTextChangedListener(ThemeTextWatcher(Constant.THEME_SAVE_TYPE_STYLECOLORS))

        if(themeDataSaveBean==null){

            mPreferences = mContext?.getSharedPreferences(Constant.SAVE_THEME_TEMP, Context.MODE_PRIVATE)
            mTheme = mPreferences?.getString(Constant.THEME_SAVE_TYPE_THEME, "")
            mStyle = mPreferences?.getString(Constant.THEME_SAVE_TYPE_STYLE, "")
            mClothes = mPreferences?.getString(Constant.THEME_SAVE_TYPE_CLOTHES, "")
            mStyleColors = mPreferences?.getString(Constant.THEME_SAVE_TYPE_STYLECOLORS, "")
          //  mStyleColors = mPreferences?.get(Constant.THEME_SAVE_TYPE_IMAGE, "")

            holder.edtTheme?.setText(mTheme)
            holder.edtStyle?.setText(mStyle)
            holder.edtClothes?.setText(mClothes)
            holder.edtStyleColors?.setText(mStyleColors)




        }else{
            holder.edtTheme?.setText(themeDataSaveBean?.theme)
            holder.edtStyle?.setText(themeDataSaveBean?.style)
            holder.edtClothes?.setText(themeDataSaveBean?.clothes)
            holder.edtStyleColors?.setText(themeDataSaveBean?.styleColors)

            imageList= themeDataSaveBean?.imagePathList as LinkedHashSet<String>
        }

    }


    inner class ThemeTextWatcher(private val saveType: String) : TextWatcher {

        override fun afterTextChanged(s: Editable?) {

            when (saveType) {
                Constant.THEME_SAVE_TYPE_THEME -> {
                    mTheme = s.toString()
                }
                Constant.THEME_SAVE_TYPE_STYLE -> {
                    mStyle = s.toString()
                }
                Constant.THEME_SAVE_TYPE_CLOTHES -> {
                    mClothes = s.toString()
                }
                Constant.THEME_SAVE_TYPE_STYLECOLORS -> {
                    mStyleColors = s.toString()
                }

            }

            val edit = mPreferences?.edit()
            edit?.putString(saveType, s.toString())
            edit?.apply()

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

    }


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

    /**
     *
     */
    fun setSelectImage(selectImage: View.OnClickListener?) {
        this.mSelectImageListener = selectImage
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position != 0) {
            //   Glide.with(mContext).load(imageList.get(position)).into(holder.mImageView)
            Glide.with(mContext!!).load(imageList.elementAt(position)).into(holder.imageView!!)
            holder.imageView!!.setOnLongClickListener {
                imageList.remove(imageList.elementAt(position))
                notifyDataSetChanged()
                saveImages()
                true
            }


        }

    }

    private fun saveImages(){

        val edit = mPreferences?.edit()
        edit?.putStringSet(Constant.THEME_SAVE_TYPE_IMAGE, imageList)
        edit?.apply()
    }

    /**
     * save theme
     */
    fun saveTheme(): Boolean {

        val data = ThemeDataSaveBean(theme = mTheme, style = mStyle, clothes = mClothes, date = Date().toString(),styleColors = mStyleColors, imagePathList = imageList)
        val time = System.nanoTime()
        val path = mContext?.cacheDir?.absolutePath + "/${mTheme}${time}.theme"
        val createFile = File(path)
        if (!createFile.exists()) {
            val isOk = createFile.createNewFile()
            if (!isOk) return false
        }


        var out: ObjectOutputStream? = null
        try {
            out = ObjectOutputStream(createFile.outputStream())
            out.writeObject(data)

        } catch (e: Exception) {
            return false
        } finally {
            out?.close()
        }

        return true
    }
}
