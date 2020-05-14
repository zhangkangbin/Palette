package com.z.palettedemo.app

import android.app.Application
import com.tencent.bugly.Bugly

/**
 * @author by zhangkangbin
 * on 2020/5/12
 * 界面说明
 */
class MyApplication :Application() {

    override fun onCreate() {
        super.onCreate()
        Bugly.init(this, AppKey.BUGLY_KEY_ID, true)
    }
}