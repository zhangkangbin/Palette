package com.z.palettedemo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.z.palettedemo.R
import com.z.palettedemo.adapter.PaletteColorsBean
import com.z.palettedemo.bean.ThemeColorsDataBean
import com.z.palettedemo.view.MyView

class SelectThemeTypeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_theme_type)

        val myView = findViewById<MyView>(R.id.myView)

        val data = intent.getSerializableExtra("data") as ArrayList<Int>
        myView.setPaletteColors(data)
    }
}
