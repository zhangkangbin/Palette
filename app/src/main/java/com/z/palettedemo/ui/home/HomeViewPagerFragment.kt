package com.z.palettedemo.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

import com.z.palettedemo.R
import com.z.palettedemo.adapter.BaseFragmentPagerAdapter
import com.z.palettedemo.base.BaseFragment
import com.z.palettedemo.ui.theme.ThemeDesignListFragment

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
