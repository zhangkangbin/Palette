package com.z.palettedemo.tool

import android.graphics.Bitmap
import android.graphics.Matrix

/**
 * @author by zhangkangbin
 * on 2020/5/15
 * 界面说明
 */
class ThemeUtils {

    companion object{

        fun getRgb(color: Int): String {
            val red: Int = color and 0xff0000 shr 16
            val green: Int = color and 0x00ff00 shr 8
            val blue: Int = color and 0x0000ff
            val hr = Integer.toHexString(red)
            val hg = Integer.toHexString(green)
            val hb = Integer.toHexString(blue)
            return "#$hr$hg$hb"
        }


    }

}