package com.z.palettedemo.ui.theme

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.z.palettedemo.R
import com.z.palettedemo.ThemeDesignActivity
import com.z.palettedemo.adapter.ThemeLocalListAdapter
import com.z.palettedemo.base.BaseFragment
import com.z.palettedemo.bean.ThemeDataSaveBean
import java.io.ObjectInputStream

/**
 * A simple [Fragment] subclass.
 */
class ThemeDesignListFragment : BaseFragment() {
    override fun initView(view: View) {

        initAdapter(view)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_theme_design_list
    }

    private lateinit var recyclerView: RecyclerView
    private var mThemeDataSaveBean: MutableList<ThemeDataSaveBean> = ArrayList()
    private fun initAdapter(view: View) {

        recyclerView = view.findViewById(R.id.recyclerViewLocalTheme)
        recyclerView.layoutManager = GridLayoutManager(activity, 1)


        val fileList = activity?.cacheDir?.listFiles() ?: return

        for (file in fileList) {

            Log.d("mytest", "path:" + file.absoluteFile)
            val input: ObjectInputStream
            if (file.isFile) {
                input = ObjectInputStream(file.inputStream())
                try {
                    val data = input.readObject() as ThemeDataSaveBean
                    mThemeDataSaveBean.add(data)
                } catch (e: Exception) {

                } finally {
                    input?.close()
                }

            }

        }


        val themeListAdapter = ThemeLocalListAdapter(mThemeDataSaveBean)

        themeListAdapter.setSelectClickListener(object : ThemeLocalListAdapter.OnClickListener<ThemeDataSaveBean> {
            override fun callData(data: ThemeDataSaveBean?) {
                val intent = Intent(activity, ThemeDesignActivity::class.java)
                intent.putExtra(ThemeDataSaveBean::class.qualifiedName, data)
                startActivity(intent)
            }
        })
        recyclerView.adapter = themeListAdapter


    }

}
