package com.z.palettedemo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.z.palettedemo.*
import com.z.palettedemo.adapter.BaseFragmentPagerAdapter

import com.z.palettedemo.ui.home.HomeFragment
import com.z.palettedemo.ui.theme.ThemeDesignListFragment

import kotlin.collections.ArrayList


/**
 * @author by zhangkangbin
 * on 2020/5/13
 * 界面说明
 */
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {

        val listFragment = ArrayList<Fragment>()
        listFragment.add(HomeFragment())
        listFragment.add(ThemeDesignListFragment())

        val listString = ArrayList<String>()
        listString.add("首页")
        listString.add("我的")
        val mainViewPager = findViewById<ViewPager>(R.id.mainViewPager)

        mainViewPager.adapter = BaseFragmentPagerAdapter(supportFragmentManager, listFragment, listString)

        mainViewPager.isNestedScrollingEnabled = true
        val mainTabLayout = findViewById<TabLayout>(R.id.mainTabLayout)

        mainTabLayout.setupWithViewPager(mainViewPager)

    }


}