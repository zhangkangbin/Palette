package com.z.palette.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * @author by zhangkangbin
 * on 2020/5/15
 * 界面说明
 */
class BaseFragmentPagerAdapter(fm: FragmentManager, private var fragmentList: List<Fragment>, private var titles: List<String>?) : FragmentPagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {

        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles?.get(position)
    }

}