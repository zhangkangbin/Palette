package com.z.palettedemo.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewParent
import android.widget.Button
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.palette.graphics.Palette
import androidx.palette.graphics.Palette.PaletteAsyncListener
import androidx.palette.graphics.Palette.Swatch
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.tencent.bugly.beta.Beta
import com.z.palettedemo.*
import com.z.palettedemo.adapter.BaseFragmentPagerAdapter

import com.z.palettedemo.adapter.PaletteColorsBean
import com.z.palettedemo.adapter.RecyclerViewAdapter
import com.z.palettedemo.bean.ThemeColorsDataBean
import com.z.palettedemo.tool.ThemeUtils
import com.z.palettedemo.ui.home.HomeFragment
import com.z.palettedemo.ui.theme.ThemeDesignListFragment
import com.z.palettedemo.view.ColorSeekBar
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*
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

        mainViewPager.adapter = BaseFragmentPagerAdapter(supportFragmentManager,listFragment,listString)


        val mainTabLayout=findViewById<TabLayout>(R.id.mainTabLayout)

        mainTabLayout.setupWithViewPager(mainViewPager)

    }


}