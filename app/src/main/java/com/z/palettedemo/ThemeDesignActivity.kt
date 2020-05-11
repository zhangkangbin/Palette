package com.z.palettedemo

import android.app.Activity
import android.content.Intent

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.z.palettedemo.adapter.ThemeDesignAdapter

/**
 * @author by zhangkangbin
 * on 2020/5/11
 * 界面说明
 */
class ThemeDesignActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme_design2)

        initAdapter()
    }

    private lateinit var recyclerView:RecyclerView
    private var stringList: MutableList<String> = ArrayList()
    private fun  initAdapter(){

        recyclerView=findViewById(R.id.recyclerViewTheme)
        recyclerView.layoutManager=GridLayoutManager(this,2)
        stringList.add("header")

        val themeListAdapter= ThemeDesignAdapter(stringList)
        themeListAdapter.setSelectImage(View.OnClickListener {

            selectImage()
        })

        recyclerView.adapter=themeListAdapter

        findViewById<Button>(R.id.btnSaveTheme).setOnClickListener {

            themeListAdapter.saveTheme()
        }

    }

    private fun setListImage(selectList :List<LocalMedia>){

        for (image in selectList){
            stringList.add(image.path)
        }

        recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun selectImage() {
      PictureSelector.create(this).openGallery(PictureMimeType.ofImage())
              .maxSelectNum(9)
              .compress(true)
              .forResult(PictureConfig.CHOOSE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode!= Activity.RESULT_OK){
            return
        }
        // 图片、视频、音频选择结果回调
        if(requestCode== PictureConfig.CHOOSE_REQUEST){

            val selectList=PictureSelector.obtainMultipleResult(data)
            setListImage(selectList)

        }

    }
}