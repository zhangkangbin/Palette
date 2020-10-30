package com.z.palette.ui.theme

import android.content.Intent
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.z.palette.R
import com.z.palette.ThemeDesignActivity
import com.z.palette.adapter.ThemeLocalListAdapter
import com.z.palette.base.BaseFragment
import com.z.palette.bean.ThemeDataSaveBean
import java.io.ObjectInputStream

/**
 * A simple [Fragment] subclass.
 */
class ThemeDesignListFragment : BaseFragment() {
   private lateinit var views: View
    override fun initView(view: View) {

        views=view

        view.findViewById<View>(R.id.themeDesign).setOnClickListener { v: View? ->

            val intent=Intent()
            intent.putExtra("TYPE",1)
            activity?.let { intent.setClass(it,ThemeDesignActivity::class.java) }
            startActivity(intent)
        }
       // initAdapter(view)
    }

    override fun onResume() {
        super.onResume()
        initAdapter(views);
    }
    override fun getLayoutId(): Int {
        return R.layout.fragment_theme_design_list
    }

    private lateinit var recyclerView: RecyclerView
    private var mThemeDataSaveBean: MutableList<ThemeDataSaveBean> = ArrayList()
    private fun initAdapter(view: View) {

        recyclerView = view.findViewById(R.id.recyclerViewLocalTheme)
        recyclerView.layoutManager = GridLayoutManager(activity, 1)
        mThemeDataSaveBean.clear()

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
