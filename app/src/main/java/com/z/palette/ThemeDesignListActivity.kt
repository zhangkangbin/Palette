package com.z.palette


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.z.palette.adapter.ThemeLocalListAdapter
import com.z.palette.bean.ThemeDataSaveBean
import java.io.ObjectInputStream

/**
 * @author by zhangkangbin
 * on 2020/5/12
 * 界面说明
 * @本地调色卡list
 */
class ThemeDesignListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme_local_list)

        initAdapter()
    }

    private lateinit var recyclerView: RecyclerView
    private var mThemeDataSaveBean: MutableList<ThemeDataSaveBean> = ArrayList()
    private fun initAdapter() {

        recyclerView = findViewById(R.id.recyclerViewLocalTheme)
        recyclerView.layoutManager = GridLayoutManager(this, 1)


        val fileList = this.cacheDir.listFiles()

        for (file in fileList) {

            Log.d("mytest", "path:" + file.absoluteFile)
            val input: ObjectInputStream
            if (file.isFile) {
                input = ObjectInputStream(file.inputStream())
                try {
                    val data = input.readObject() as ThemeDataSaveBean
                    mThemeDataSaveBean.add(data)
                } catch (e:Exception){

                }finally {
                    input?.close()
                }

            }

        }


        val themeListAdapter = ThemeLocalListAdapter(mThemeDataSaveBean)

        themeListAdapter.setSelectClickListener(object : ThemeLocalListAdapter.OnClickListener<ThemeDataSaveBean> {
            override fun callData(data: ThemeDataSaveBean?) {
               val intent= Intent(getActivity(),ThemeDesignActivity::class.java)
                intent.putExtra(ThemeDataSaveBean::class.qualifiedName,data)
                startActivity(intent)
            }
        })
        recyclerView.adapter = themeListAdapter


    }

     fun getActivity(): Activity {
        return this
    }
}