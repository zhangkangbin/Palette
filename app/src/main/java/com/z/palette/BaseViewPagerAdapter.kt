package com.z.palette

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter


/**
 * @author by zhangkangbin
 * on 2020/5/15
 * 界面说明
 */
class BaseViewPagerAdapter(private var mFragment :List<View>) : PagerAdapter() {

  //  lateinit var mFragment :List<Fragment>

    override fun isViewFromObject(view: View, `object`: Any): Boolean {

        return view === `object`
    }

    override fun getCount(): Int {
       return mFragment.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        container.addView(mFragment[position])
        return mFragment[position]
    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }
}