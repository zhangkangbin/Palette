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
     * 根据图片实际尺寸动态调整所有参数
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

        // 获取屏幕密度和宽度
        val displayMetrics = applicationContext.resources.displayMetrics
        val screenWidthPx = displayMetrics.widthPixels.toFloat()
        
        // 计算缩放比例：原图宽度相对于屏幕宽度的比例
        // 这样可以让所有dp/sp值按照图片尺寸等比例放大
        val scale = originalBitmap.width / screenWidthPx
        
        // 根据缩放比例调整所有尺寸
        val margin = (15 * displayMetrics.density * scale).toInt() // 15dp按比例放大
        val textMarginBottom = (10 * displayMetrics.density * scale).toInt() // 10dp按比例放大
        
        // 设置文字画笔 - 12sp按比例放大
        val textPaint = Paint().apply {
            color = Color.GRAY  // 
            textSize = 12 * displayMetrics.scaledDensity * scale  // 12sp按比例放大
            isAntiAlias = true  // 抗锯齿
            style = Paint.Style.FILL
        }
        
        // 计算文字可用宽度（图片宽度，不包含边距）
        val maxTextWidth = originalBitmap.width.toFloat()
        
        // 处理文字换行 - 支持中文
        val lines = wrapTextForChinese(text, textPaint, maxTextWidth)
        
        // 计算文字区域的总高度 (lineHeight = 16dp按比例放大)
        val lineHeight = 16 * displayMetrics.density * scale
        val totalTextHeight = (lineHeight * lines.size).toInt() + textMarginBottom
        
        // 创建新的Bitmap：原图 + 上下左右边距 + 文字区域高度
        val resultWidth = originalBitmap.width + margin * 2
        val resultHeight = originalBitmap.height + margin * 2 + totalTextHeight
        val resultBitmap = Bitmap.createBitmap(resultWidth, resultHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(resultBitmap)
        
        // 1. 绘制白色背景
        canvas.drawColor(Color.WHITE)
        
        // 2. 绘制原图（留出边距）
        canvas.drawBitmap(originalBitmap, margin.toFloat(), margin.toFloat(), null)
        
        // 3. 绘制文字（在原图下方，左右各留边距）
        val textStartX = margin.toFloat()
        var currentY = originalBitmap.height + margin + lineHeight - textPaint.fontMetrics.ascent
        
        for (line in lines) {
            canvas.drawText(line, textStartX, currentY, textPaint)
            currentY += lineHeight
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