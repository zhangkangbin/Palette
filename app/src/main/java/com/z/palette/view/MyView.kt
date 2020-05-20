package com.z.palette.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.z.palette.BitmapUtils
import com.z.palette.R


/**
 * TODO: document your custom view class.
 */
class MyView : View {

    private var _exampleString: String? = null // TODO: use a default from R.string...
    private var _exampleColor: Int = Color.RED // TODO: use a default from R.color...
    private var _exampleDimension: Float = 0f // TODO: use a default from R.dimen...

    private var textPaint: TextPaint? = null
    private var textWidth: Float = 0f
    private var textHeight: Float = 0f

    private var mPaletteColors: ArrayList<Int>? = null

   open fun setPaletteColors(paletteColors: ArrayList<Int> ?){

        this.mPaletteColors=paletteColors
    }
    /**
     * The text to draw
     */
    var exampleString: String?
        get() = _exampleString
        set(value) {
            _exampleString = value
            invalidateTextPaintAndMeasurements()
        }

    /**
     * The font color
     */
    var exampleColor: Int
        get() = _exampleColor
        set(value) {
            _exampleColor = value
            invalidateTextPaintAndMeasurements()
        }

    /**
     * In the example view, this dimension is the font size.
     */
    var exampleDimension: Float
        get() = _exampleDimension
        set(value) {
            _exampleDimension = value
            invalidateTextPaintAndMeasurements()
        }

    /**
     * In the example view, this drawable is drawn above the text.
     */
    var exampleDrawable: Drawable? = null

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
                attrs, R.styleable.MyView, defStyle, 0)

        _exampleString = a.getString(
                R.styleable.MyView_exampleString)
        _exampleColor = a.getColor(
                R.styleable.MyView_exampleColor,
                exampleColor)
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        _exampleDimension = a.getDimension(
                R.styleable.MyView_exampleDimension,
                exampleDimension)

        if (a.hasValue(R.styleable.MyView_exampleDrawable)) {
            exampleDrawable = a.getDrawable(
                    R.styleable.MyView_exampleDrawable)
            exampleDrawable?.callback = this
        }

        a.recycle()

        // Set up a default TextPaint object
        textPaint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            textAlign = Paint.Align.LEFT
        }

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements()
    }

    private fun invalidateTextPaintAndMeasurements() {
        textPaint?.let {
            it.textSize = exampleDimension
            it.color = exampleColor
            textWidth = it.measureText(exampleString)
            textHeight = it.fontMetrics.bottom
        }
    }

    // 创建画笔
    var p = Paint()
    val mRadius = 150F
    var mCenterX = 200F
    var mCenterY = 200F


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        p.isAntiAlias = true// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白了

        p.style = Paint.Style.STROKE;
     //   canvas.drawCircle(mCenterX, mCenterY, mRadius, p)// 大圆

        val x = (width - height / 2) / 2.toFloat()
        val y = height / 4.toFloat()

        val oval = RectF(x, y,
                width - x, height - y)
      //  RectF(137.5, 137.5, 412.5, 412.5)

        val size=mPaletteColors!!.size
        p.strokeWidth = 360F/size//宽度




        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.bb)
        val bitmapTemp = bitmap.copy(Bitmap.Config.RGB_565, true)

        var xx=x+10
        var yy=y+10

     //   bitmapTemp.width= (width - xx*2).toInt()
       // bitmapTemp.height= (height - yy*2).toInt()

        val newSizeBmp2 = BitmapUtils.resizeBitmap(bitmapTemp, (width - xx*2).toInt(), (height - yy*2).toInt())

        //canvas.drawBitmap(newSizeBmp2, width.toFloat(), 0f, null)

        canvas.drawBitmap(newSizeBmp2, xx, yy, p)
        var startAngle=0f
        for (i in 1..size) {
            val angle: Float = i * 360f/size
            Log.d("myview","-----------------:"+startAngle)
            Log.d("myview", "----------------ii-:$i")
            Log.d("myview",":"+(angle))

            p.color = mPaletteColors!![i-1]

            canvas.drawArc(oval,startAngle,(360f/size),false,p)
            startAngle += (360f/size)
        }


    }

}
