package com.z.palette.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * TODO: document your custom view class.
 */
class ZViewPager : ViewPager {

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

    }


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }

}
