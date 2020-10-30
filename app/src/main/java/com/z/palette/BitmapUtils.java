package com.z.palette;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;


import com.palette.Palette;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.util.Date;
import java.util.List;

public class BitmapUtils {



    private static Bitmap newBitmap(Bitmap bmp1, Bitmap bmp2) {
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

   private static int mCardColorScale=20;
    public enum Draw {
        left, right, top, bottom
    }
    public static Bitmap drawLeft(Bitmap bmp, List<Palette.Swatch> swatchList) {

      return  newBitmap(bmp,swatchList,Draw.left);
    }
    public static Bitmap drawRight(Bitmap bmp, List<Palette.Swatch> swatchList) {

        return   newBitmap(bmp,swatchList,Draw.right);
    }
    public static Bitmap newBitmap(Bitmap bmp, List<Palette.Swatch> swatchList,Draw draw) {
        Bitmap retBmp = null;

        int width = bmp.getWidth();
        int height = bmp.getHeight();

        int colorCardWith = 0;

        Bitmap.Config config = bmp.getConfig();

        Canvas canvas = null;

        switch (draw){

            case left:
                colorCardWith = width / mCardColorScale;
                retBmp = Bitmap.createBitmap(width + colorCardWith, bmp.getHeight(), Bitmap.Config.ARGB_8888);
                canvas = new Canvas(retBmp);
                canvas.drawBitmap(bmp, colorCardWith, 0, null);
                width=0;
                break;
            case right:
                colorCardWith = width / mCardColorScale;
                retBmp = Bitmap.createBitmap(width + colorCardWith, bmp.getHeight(), Bitmap.Config.ARGB_8888);
                canvas = new Canvas(retBmp);
                canvas.drawBitmap(bmp, 0, 0, null);

                break;
            case top:
                colorCardWith = height / mCardColorScale;
                retBmp = Bitmap.createBitmap(width , bmp.getHeight()+colorCardWith, Bitmap.Config.ARGB_8888);
                canvas = new Canvas(retBmp);
                canvas.drawBitmap(bmp, 0, colorCardWith, null);

                height=0;
                break;
            case bottom:
                colorCardWith = height / mCardColorScale;
                retBmp = Bitmap.createBitmap(width , bmp.getHeight()+colorCardWith, Bitmap.Config.ARGB_8888);
                canvas = new Canvas(retBmp);
                canvas.drawBitmap(bmp, 0, 0, null);


                break;

            default:

                break;
        }



        int h ;
        //间距
        int jianju = 0;
        int  size=swatchList.size();

        for (int i = size - 1; i >= 0; i--) {
            Palette.Swatch color = swatchList.get(i);
            Paint paint=new Paint();
            paint.setColor(color.getRgb());

            //画左和右边
            if(draw==Draw.left||Draw.right==draw){
                h=height / swatchList.size();
                canvas.drawRect(width,jianju,width+colorCardWith,jianju+h,paint);
            }else {
                h=width / swatchList.size();
            //    canvas.drawRect(jianju,height,jianju+h,height+colorCardWith,paint);
                ////        设置画笔属性
                ////        paint.setStyle(Paint.Style.FILL);//画笔属性是实心圆
                //        paint.setStyle(Paint.Style.STROKE);//画笔属性是空心圆
                canvas.drawCircle(jianju+h,height,30,paint);
            }

          //  canvas.drawRect(0,jianju,colorCardWith,jianju+h,paint);
            jianju=jianju+h;
        }



        return retBmp;
    }


/*
    private void drawRectangle(Canvas canvas, List<Palette.Swatch> swatchList,Draw draw){
        int h ;
        //间距
        int jianju = 0;
        int  size=swatchList.size();

        for (int i = size - 1; i >= 0; i--) {
            Palette.Swatch color = swatchList.get(i);
            Paint paint=new Paint();
            paint.setColor(color.getRgb());

            //画左和右边
            if(draw==Draw.left||Draw.right==draw){
                h=height / swatchList.size();
                canvas.drawRect(width,jianju,width+colorCardWith,jianju+h,paint);
            }else {
                h=width / swatchList.size();
                //    canvas.drawRect(jianju,height,jianju+h,height+colorCardWith,paint);
                ////        设置画笔属性
                ////        paint.setStyle(Paint.Style.FILL);//画笔属性是实心圆
                //        paint.setStyle(Paint.Style.STROKE);//画笔属性是空心圆
                canvas.drawCircle(jianju+h,height,30,paint);
            }

            //  canvas.drawRect(0,jianju,colorCardWith,jianju+h,paint);
            jianju=jianju+h;
        }
*/




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
        if (isEmptyBitmap(src)) {
            return false;
        }

        OutputStream os=null;
        boolean ret = false;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            ret = src.compress(format, 100, os);
            if (recycle && !src.isRecycled()) {
                src.recycle();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(os!=null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return ret;
    }

    /**
     * Bitmap对象是否为空。
     */
    private static boolean isEmptyBitmap(Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

    public static void saveImage(Bitmap imageFile, Context context) {

        if (imageFile == null) {
            return;
        }

        File newFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), new Date().toString()+".jpeg");
        if (!newFile.exists()) {
            try {
                newFile.createNewFile();
                BitmapUtils.save(imageFile, newFile, Bitmap.CompressFormat.JPEG, true);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(newFile)));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
