package com.z.palettedemo

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
import com.z.palettedemo.adapter.ThemeDesignAdapter
import com.z.palettedemo.bean.ThemeDataSaveBean
import com.z.palettedemo.constant.Constant

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
    private var stringList: LinkedHashSet<String> = LinkedHashSet()
    private fun  initAdapter(){

       val data= intent.getSerializableExtra(ThemeDataSaveBean::class.qualifiedName) as ThemeDataSaveBean ?

        recyclerView=findViewById(R.id.recyclerViewTheme)
        recyclerView.layoutManager=GridLayoutManager(this,2)
        stringList.add("header")



        val sharedPreferences = this.getSharedPreferences(Constant.SAVE_THEME_TEMP, Context.MODE_PRIVATE);
        val local=sharedPreferences?.getStringSet(Constant.THEME_SAVE_TYPE_IMAGE, stringList)

        if (local != null) {
            stringList.addAll(local)
        }

        val themeListAdapter= ThemeDesignAdapter(stringList,data)
        themeListAdapter.setSelectImage(View.OnClickListener {

            selectImage()
        })

        recyclerView.adapter=themeListAdapter

        findViewById<Button>(R.id.btnSaveTheme).setOnClickListener {

            //保存
            if (themeListAdapter.saveTheme()){
                Toast.makeText(this,"saved  successfully ",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"saved  fail ",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setListImage(selectList :List<LocalMedia>){

        for (image in selectList){
            stringList.add(image.path)
        }

        recyclerView.adapter?.notifyDataSetChanged()



        if (stringList.isNotEmpty()){
            val edit = this.getSharedPreferences(Constant.SAVE_THEME_TEMP, Context.MODE_PRIVATE)?.edit()
            edit?.putStringSet(Constant.THEME_SAVE_TYPE_IMAGE, stringList)
            edit?.apply()
        }


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