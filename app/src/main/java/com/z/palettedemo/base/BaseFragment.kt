package com.z.palettedemo.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment


/**
 * A simple [Fragment] subclass.
 * Use the [BaseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
abstract class BaseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(getLayoutId(), container, false)
        initView(view)

        return view
    }

    abstract fun initView(view: View)
    abstract fun getLayoutId(): Int


}
