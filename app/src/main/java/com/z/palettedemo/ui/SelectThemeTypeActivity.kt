package com.z.palettedemo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.z.palettedemo.BaseViewPagerAdapter
import com.z.palettedemo.R
import com.z.palettedemo.adapter.BaseFragmentPagerAdapter


class SelectThemeTypeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_theme_type)

        initView()

  /*      val myView = findViewById<MyView>(R.id.myView)

        val data = intent.getSerializableExtra("data") as ArrayList<Int>
        myView.setPaletteColors(data)*/
    }

    private fun initView(){

        val listFragment=ArrayList<Fragment>()
        listFragment.add(ThemeTypeFragment())
        listFragment.add(ThemeTypeFragment())
        listFragment.add(ThemeTypeFragment())

        val themeViewPager=findViewById<ViewPager>(R.id.themeViewPager)

        themeViewPager.adapter= BaseFragmentPagerAdapter(supportFragmentManager,listFragment,null)

      //  tablayout.setupWithViewPager(viewpager);//此方法就是让tablayout和ViewPager联动
    }
}
