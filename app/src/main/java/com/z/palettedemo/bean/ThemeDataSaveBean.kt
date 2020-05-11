package com.z.palettedemo.bean



/**
 * @author by zhangkangbin
 * on 2020/5/11
 * 主题卡序列化保存
 */

data class ThemeDataSaveBean (
        val theme:String?,
        val style:String?,
        val clothes:String?,
        val styleColors:String?,
        val imagePathList:List<String>?
)


