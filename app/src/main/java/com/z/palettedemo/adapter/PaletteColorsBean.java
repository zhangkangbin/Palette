package com.z.palettedemo.adapter;

import androidx.palette.graphics.Palette;

/**
 * @author by zhangkangbin
 * on 2020/5/9
 * 界面说明
 */
public class PaletteColorsBean {

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

    private Palette.Swatch color;
    private String colorText;
}
