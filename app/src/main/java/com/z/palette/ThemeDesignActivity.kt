package com.z.palette

import android.app.Activity
import android.content.Context
import android.content.Intent

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.z.palette.adapter.ThemeDesignAdapter
import com.z.palette.app.LoadImageEngine
import com.z.palette.bean.ThemeDataSaveBean
import com.z.palette.constant.Constant

/**
 * @author by zhangkangbin
 * on 2020/5/11
 * 主题卡添加
 */
class ThemeDesignActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme_design2)

        initAdapter()
    }

    private lateinit var recyclerView:RecyclerView
    private var stringList: ArrayList<String> = ArrayList()
    private fun  initAdapter(){
        var data: ThemeDataSaveBean? =null;
        val type=intent.getIntExtra("TYPE",0)
        if(type==0){
             data= intent.getSerializableExtra(ThemeDataSaveBean::class.qualifiedName) as ThemeDataSaveBean ?
        }

        recyclerView=findViewById(R.id.recyclerViewTheme)
        recyclerView.layoutManager=GridLayoutManager(this,2)
        if(data==null){
            stringList.add("header")
        }


        data?.imagePathList?.let { stringList.addAll(it) }

        val themeListAdapter= ThemeDesignAdapter(stringList,data)
        themeListAdapter.setSelectImage(View.OnClickListener {

            selectImage()
        })

        recyclerView.adapter=themeListAdapter

        findViewById<View>(R.id.btnSaveTheme).setOnClickListener {

            //保存
            if (themeListAdapter.saveTheme()){
                finish()
                Toast.makeText(this,"saved  successfully ",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"saved  fail ",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setListImage(selectList :List<LocalMedia>){

        for (image in selectList){
            //if(image.androidQToPath)
            stringList.add(image.path)
        }

        recyclerView.adapter?.notifyDataSetChanged()


/*
        if (stringList.isNotEmpty()){
            val edit = this.getSharedPreferences(Constant.SAVE_THEME_TEMP, Context.MODE_PRIVATE)?.edit()
            edit?.putStringSet(Constant.THEME_SAVE_TYPE_IMAGE, stringList)
            edit?.apply()
        }*/


    }

    private fun selectImage() {
      PictureSelector.create(this).openGallery(PictureMimeType.ofImage())
              .maxSelectNum(10)
              //.compress(true)
              .imageEngine(LoadImageEngine())
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