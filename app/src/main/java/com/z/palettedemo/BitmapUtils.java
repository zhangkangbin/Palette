package com.z.palettedemo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import androidx.palette.graphics.Palette;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class BitmapUtils {

    public static Bitmap newBitmap(Bitmap bmp1, Bitmap bmp2) {
        Bitmap retBmp;

        int width = bmp1.getWidth();

        int colorCardWith = width / 5;

        if (bmp2.getWidth() != width) {
            Log.d("mytest", "newBitmap1");
            //以第一张图片的宽度为标准，对第二张图片进行缩放。
            int h2 = bmp2.getHeight() * width / bmp2.getWidth();
            retBmp = Bitmap.createBitmap(width + colorCardWith, bmp1.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(retBmp);
            Bitmap newSizeBmp2 = resizeBitmap(bmp2, width, h2);
            canvas.drawBitmap(bmp1, 0, 0, null);
            //    canvas.drawBitmap(newSizeBmp2, 0, bmp1.getHeight(), null);
            canvas.drawBitmap(newSizeBmp2, width, 0, null);
        } else {
            //两张图片宽度相等，则直接拼接。

            retBmp = Bitmap.createBitmap(width + colorCardWith, bmp1.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(retBmp);
            canvas.drawBitmap(bmp1, 0, 0, null);
            canvas.drawBitmap(bmp2, width, 0, null);


            Log.d("mytest", "newBitmap2");
        }

        return retBmp;
    }

    public static Bitmap newBitmap2(Bitmap bmp,  List<Palette.Swatch> swatchList) {
        Bitmap retBmp;

        int width = bmp.getWidth();
        int height = bmp.getHeight();

        int colorCardWith = width / 5;


        retBmp = Bitmap.createBitmap(width + colorCardWith, bmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(retBmp);
        canvas.drawBitmap(bmp, 0, 0, null);


        int h=height/ swatchList.size();

        int jianju=0;

        for(Palette.Swatch color:swatchList){
            Paint paint=new Paint();
            paint.setColor(color.getRgb());
            canvas.drawRect(width,jianju,width+colorCardWith,jianju+h,paint);
            jianju=jianju+h;
        }

        Log.d("mytest", "newBitmap2");


        return retBmp;
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        float scaleWidth = ((float) newWidth) / bitmap.getWidth();
        float scaleHeight = ((float) newHeight) / bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bmpScale = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bmpScale;
    }

    /**
     * 保存图片到文件File。
     *
     * @param src     源图片
     * @param file    要保存到的文件
     * @param format  格式
     * @param recycle 是否回收
     * @return true 成功 false 失败
     */
    public static boolean save(Bitmap src, File file, Bitmap.CompressFormat format, boolean recycle) {
        if (isEmptyBitmap(src))
            return false;

        OutputStream os;
        boolean ret = false;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            ret = src.compress(format, 100, os);
            if (recycle && !src.isRecycled())
                src.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    /**
     * Bitmap对象是否为空。
     */
    public static boolean isEmptyBitmap(Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

}
