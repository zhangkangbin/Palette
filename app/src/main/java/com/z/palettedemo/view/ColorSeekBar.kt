package com.z.palettedemo.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.SeekBar
import android.widget.TextView
import com.z.palettedemo.R


/**
 * @author by zhangkangbin
 * on 2020/5/12
 * 界面说明
 */
class ColorSeekBar : FrameLayout {
/*   constructor(ColorSeekBar(context: Context) {
        super(context)
        initView(context)
    }

    fun ColorSeekBar(context: Context, @Nullable attrs: AttributeSet?) {
        super(context, attrs)
        initView(context)
    }

    fun ColorSeekBar(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) {
        super(context, attrs, defStyleAttr)
        initView(context)
    }*/

    constructor(context: Context?) : super(context) {
        initView(context)
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    var mProgress = 6
    fun initView(context: Context?){
       val  view=LayoutInflater.from(context).inflate(R.layout.color_seek_bar,null,false)
        val seekBar=view.findViewById<SeekBar>(R.id.colorCount)
        val seekBarText=view.findViewById<TextView>(R.id.colorCountText)
        seekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mProgress = progress
                seekBarText.setText(progress.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        addView(view)
    }

    fun getProgress(): Int {
        return mProgress
    }
}