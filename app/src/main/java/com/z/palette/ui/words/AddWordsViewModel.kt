package com.z.palette.ui.words

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.view.View
import androidx.lifecycle.ViewModel
import com.z.palette.BitmapUtils

class AddWordsViewModel :ViewModel() {

    /**
     * 直接在原图上绘制文字 - 高清晰度版本
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

        // 创建一个可变的Bitmap副本
        val resultBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(resultBitmap)

        // 设置文字画笔
        val textPaint = Paint().apply {
            color = Color.WHITE  // 白色文字
            textSize = calculateTextSize(resultBitmap.width)  // 根据图片宽度计算字体大小
            isAntiAlias = true  // 抗锯齿
            style = Paint.Style.FILL
            typeface = Typeface.DEFAULT_BOLD  // 粗体
            setShadowLayer(8f, 0f, 0f, Color.BLACK)  // 添加阴影使文字更清晰
        }

        // 设置背景半透明黑色画笔（让文字更易读）
        val bgPaint = Paint().apply {
            color = Color.argb(120, 0, 0, 0)  // 半透明黑色背景
            isAntiAlias = true
            style = Paint.Style.FILL
        }

        // 计算文字绘制位置和大小
        val padding = resultBitmap.width * 0.05f  // 边距为图片宽度的5%
        val maxWidth = resultBitmap.width - (padding * 2)
        
        // 处理文字换行
        val lines = wrapText(text, textPaint, maxWidth)
        
        // 计算文字区域的总高度
        val lineHeight = textPaint.fontMetrics.let { it.descent - it.ascent + it.leading }
        val totalTextHeight = lineHeight * lines.size + padding * 2
        
        // 绘制在图片底部
        val startY = resultBitmap.height - totalTextHeight
        
        // 绘制半透明背景
        canvas.drawRect(
            0f,
            startY,
            resultBitmap.width.toFloat(),
            resultBitmap.height.toFloat(),
            bgPaint
        )
        
        // 绘制每一行文字
        var currentY = startY + padding - textPaint.fontMetrics.ascent
        for (line in lines) {
            canvas.drawText(line, padding, currentY, textPaint)
            currentY += lineHeight
        }

        // 保存图片
        BitmapUtils.saveImage(resultBitmap, applicationContext)

        onSuccess()
    }

    /**
     * 根据图片宽度计算合适的字体大小
     */
    private fun calculateTextSize(imageWidth: Int): Float {
        // 字体大小约为图片宽度的4%
        return imageWidth * 0.04f
    }

    /**
     * 文字换行处理
     */
    private fun wrapText(text: String, paint: Paint, maxWidth: Float): List<String> {
        val lines = mutableListOf<String>()
        val words = text.split(" ")
        var currentLine = ""
        
        for (word in words) {
            val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
            val bounds = Rect()
            paint.getTextBounds(testLine, 0, testLine.length, bounds)
            
            if (bounds.width() <= maxWidth) {
                currentLine = testLine
            } else {
                if (currentLine.isNotEmpty()) {
                    lines.add(currentLine)
                }
                currentLine = word
            }
        }
        
        if (currentLine.isNotEmpty()) {
            lines.add(currentLine)
        }
        
        return lines.ifEmpty { listOf(text) }
    }
}