package com.z.palette.ui.home

import androidx.fragment.app.Fragment
import android.view.View
import androidx.viewpager.widget.ViewPager

import com.z.palette.R
import com.z.palette.adapter.BaseFragmentPagerAdapter
import com.z.palette.base.BaseFragment

/**
 * A simple [Fragment] subclass.
 */
class HomeViewPagerFragment : BaseFragment() {
    override fun initView(view: View) {

        val listFragment = ArrayList<Fragment>()
        listFragment.add(HomeFragment())
        listFragment.add(HomeFragment())

        val homeViewPager = view.findViewById<ViewPager>(R.id.homeViewPager)

        homeViewPager.adapter = BaseFragmentPagerAdapter(childFragmentManager,listFragment,null)


    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home_view_pager
    }


}
