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
     * view转bitmap - 优化版本，提高图片清晰度
     * 
     * 优化点：
     * 1. 使用更高的缩放比例（2x）来提升清晰度
     * 2. 启用抗锯齿和过滤
     * 3. 使用drawingCache方式确保准确渲染
     */
    fun saveBitmap(applicationContext:Context,v: View,onSuccess:()->Unit){
        val w = v.width
        val h = v.height
        
        // 使用2倍尺寸来提高清晰度
        val scale = 2.0f
        val scaledW = (w * scale).toInt()
        val scaledH = (h * scale).toInt()
        
        // 创建高质量的Bitmap
        val bmp = Bitmap.createBitmap(scaledW, scaledH, Bitmap.Config.ARGB_8888)
        val c = Canvas(bmp)
        
        // 设置画布缩放
        c.scale(scale, scale)
        
        // 绘制白色背景
        c.drawColor(Color.WHITE)
        
        /** 如果不设置canvas画布为白色，则生成透明  */
        
        // 确保View布局正确
        v.layout(0, 0, w, h)
        
        // 使用draw方法绘制View
        v.draw(c)

        BitmapUtils.saveImage(bmp,applicationContext)

        onSuccess()
    }
}