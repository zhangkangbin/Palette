package com.z.palette.ui.words

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.lifecycle.ViewModel
import com.z.palette.BitmapUtils

class AddWordsViewModel :ViewModel() {

    /**
     * 直接在原图上绘制文字 - 复现原布局效果
     * 保持原来的15dp白色边距和布局样式
     * 
     * @param applicationContext 应用上下文
     * @param originalBitmap 原始图片
     * @param text 要绘制的文字
     * @param onSuccess 成功回调
     */
    fun saveBitmap(applicationContext: Context, originalBitmap: Bitmap?, text: String, onSuccess: () -> Unit) {
        if (originalBitmap == null) {
            return
        }

        val density = applicationContext.resources.displayMetrics.density
        val margin = (15 * density).toInt() // 15dp转为像素
        val textMarginBottom = (10 * density).toInt() // TextView底部额外间距
        
        // 设置文字画笔 - 复现原TextView样式 (12sp, 黑色)
        val textPaint = Paint().apply {
            color = Color.BLACK  // 黑色文字
            textSize = 12 * applicationContext.resources.displayMetrics.scaledDensity  // 12sp
            isAntiAlias = true  // 抗锯齿
            style = Paint.Style.FILL
        }
        
        // 计算文字可用宽度（图片宽度）
        val maxTextWidth = originalBitmap.width.toFloat()
        
        // 处理文字换行 - 支持中文
        val lines = wrapTextForChinese(text, textPaint, maxTextWidth)
        
        // 计算文字区域的总高度 (lineHeight = 16dp)
        val lineHeightDp = 16 * density
        val totalTextHeight = (lineHeightDp * lines.size).toInt() + textMarginBottom
        
        // 创建新的Bitmap：原图 + 上下左右15dp边距 + 文字区域高度
        val resultWidth = originalBitmap.width + margin * 2
        val resultHeight = originalBitmap.height + margin * 2 + totalTextHeight
        val resultBitmap = Bitmap.createBitmap(resultWidth, resultHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(resultBitmap)
        
        // 1. 绘制白色背景
        canvas.drawColor(Color.WHITE)
        
        // 2. 绘制原图（留出15dp边距）
        canvas.drawBitmap(originalBitmap, margin.toFloat(), margin.toFloat(), null)
        
        // 3. 绘制文字（在原图下方，左右各15dp边距）
        val textStartX = margin.toFloat()
        var currentY = originalBitmap.height + margin + lineHeightDp - textPaint.fontMetrics.ascent
        
        for (line in lines) {
            canvas.drawText(line, textStartX, currentY, textPaint)
            currentY += lineHeightDp
        }

        // 保存图片
        BitmapUtils.saveImage(resultBitmap, applicationContext)

        onSuccess()
    }

    /**
     * 文字换行处理 - 适配中文
     * 按字符逐个测量，而不是按空格分词
     */
    private fun wrapTextForChinese(text: String, paint: Paint, maxWidth: Float): List<String> {
        val lines = mutableListOf<String>()
        var currentLine = ""
        
        for (char in text) {
            val testLine = currentLine + char
            val width = paint.measureText(testLine)
            
            if (width <= maxWidth) {
                currentLine = testLine
            } else {
                if (currentLine.isNotEmpty()) {
                    lines.add(currentLine)
                }
                currentLine = char.toString()
            }
        }
        
        if (currentLine.isNotEmpty()) {
            lines.add(currentLine)
        }
        
        return lines.ifEmpty { listOf(text) }
    }
}