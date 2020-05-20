package com.z.palette.bean

import java.io.Serializable


/**
 * @author by zhangkangbin
 * on 2020/5/11
 * 主题卡序列化保存
 * Parcelize 序列化
 */

data class ThemeDataSaveBean  (
        val theme:String?,
        val style:String?,
        val clothes:String?,
        val styleColors:String?,
        val date:String?,
        val imagePathList:Set<String>?
):Serializable


