package com.z.palette.ui.words

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import androidx.lifecycle.ViewModel
import com.z.palette.BitmapUtils

class AddWordsViewModel :ViewModel() {

    /**
     * view转bitmap
     */
    fun saveBitmap(applicationContext:Context,v: View){
        val w = v.width
        val h = v.height
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(bmp)
        c.drawColor(Color.WHITE)
        /** 如果不设置canvas画布为白色，则生成透明  */
        v.layout(0, 0, w, h)
        v.draw(c)

       // Toast.makeText(this,"saved successful", Toast.LENGTH_SHORT).show()
        BitmapUtils.saveImage(bmp,applicationContext)


    }
}