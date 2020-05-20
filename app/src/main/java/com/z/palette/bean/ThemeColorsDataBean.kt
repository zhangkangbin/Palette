package com.z.palette.bean

import com.z.palette.adapter.PaletteColorsBean
import java.io.Serializable


/**
 * @author by zhangkangbin
 * on 2020/5/11
 * 主题卡序列化保存
 * Parcelize 序列化
 */

data class ThemeColorsDataBean  (
        val colorsList:MutableList<PaletteColorsBean>?
):Serializable


