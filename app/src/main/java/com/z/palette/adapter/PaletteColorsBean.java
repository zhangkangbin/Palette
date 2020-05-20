package com.z.palette.adapter;

import android.graphics.Bitmap;


import com.palette.Palette;

import java.io.Serializable;

/**
 * @author by zhangkangbin
 * on 2020/5/9
 * 界面说明
 */
public class PaletteColorsBean implements Serializable {

    public Palette.Swatch getColor() {
        return color;
    }

    public void setColor(Palette.Swatch color) {
        this.color = color;
    }

    public String getColorText() {
        return colorText;
    }

    public void setColorText(String colorText) {
        this.colorText = colorText;
    }

    public PaletteColorsBean(Palette.Swatch color, String colorText) {
        this.color = color;
        this.colorText = colorText;
    }
    public PaletteColorsBean(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public Bitmap getBitmap() {
        return bitmap;
    }
    private Palette.Swatch color;
    private String colorText;


    private Bitmap bitmap;
}
