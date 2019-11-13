package com.z.palettedemo;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.z.palettedemo.adapter.RecyclerViewAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView imageCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        imageCard = findViewById(R.id.imageCard);
        findViewById(R.id.selectView).setOnClickListener(v -> selectImage());


    }


    private void merge(List<LocalMedia> selectList) {


        File zhang = new File(selectList.get(0).getPath());
        File phil = new File(selectList.get(1).getPath());

        try {
            Bitmap bitmap1 = BitmapFactory.decodeStream(new FileInputStream(zhang));
            Bitmap bitmap2 = BitmapFactory.decodeStream(new FileInputStream(phil));

            Bitmap newBmp = BitmapUtils.newBitmap(bitmap1, bitmap2);

            File zhangphil = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "zhangphil.jpg");
            if (!zhangphil.exists())
                zhangphil.createNewFile();
            BitmapUtils.save(newBmp, zhangphil, Bitmap.CompressFormat.JPEG, true);
            // Toast.makeText(this, "成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void mergeColor(List<Palette.Swatch> swatches) {


            File zhang = new File(imagePath);

            try {
                Bitmap bitmap1 = BitmapFactory.decodeStream(new FileInputStream(zhang));

                Bitmap newBmp = BitmapUtils.newBitmap2(bitmap1, swatches);
                imageCard.setImageBitmap(newBmp);
 /*               File zhangphil = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "zhangphil.jpg");
                if (!zhangphil.exists())
                    zhangphil.createNewFile();

              BitmapUtils.save(newBmp, zhangphil, Bitmap.CompressFormat.JPEG, true);*/
                 Toast.makeText(this, "成功", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }



    }

    private void getColor() {

        if (TextUtils.isEmpty(imagePath)) {
            return;
        }
        //目标bitmap
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        //  Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aa);

        Palette.Builder builder = Palette.from(bitmap);
        builder.maximumColorCount(10);
        builder.addFilter(new Palette.Filter() {

            private static final float BLACK_MAX_LIGHTNESS = 0.05f;
            private static final float WHITE_MIN_LIGHTNESS = 0.95f;


            /**
             * @return true if the color represents a color which is close to black.
             */
            private boolean isBlack(float[] hslColor) {
                if (hslColor[2] >= BLACK_MAX_LIGHTNESS) {
                    Log.d("test", "过滤黑色");
                }
                return hslColor[2] <= BLACK_MAX_LIGHTNESS;
            }

            /**
             * @return true if the color represents a color which is close to white.
             */
            private boolean isWhite(float[] hslColor) {

                if (hslColor[2] >= WHITE_MIN_LIGHTNESS) {
                    Log.d("test", "过滤白色");
                }
                return hslColor[2] >= WHITE_MIN_LIGHTNESS;
            }

            @Override
            public boolean isAllowed(int rgb, @NonNull float[] hsl) {
                StringBuilder builder1 = new StringBuilder();
                for (Float f : hsl) {
                    builder1.append(f + "-");
                }
                Log.d("test", rgb + "____" + builder1.toString());
                return !isWhite(hsl) && !isBlack(hsl);

            }
        });
        builder.generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {

                List<Palette.Swatch> swatchList = palette.getSwatches();

                recyclerView.setAdapter(new RecyclerViewAdapter(swatchList));
                mergeColor(swatchList);
                Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();//获取有活力的颜色样本
                if (vibrantSwatch != null) {
                    Log.d("test", "活力的颜色样本:" + vibrantSwatch.getTitleTextColor());
                }
                Palette.Swatch drakVibrantSwatch = palette.getDarkVibrantSwatch();//获取有活力 暗色的样本

                if (drakVibrantSwatch != null) {
                    Log.d("test", "有活力暗色的样本:" + drakVibrantSwatch.getTitleTextColor());
                }


                Palette.Swatch mutedSwatch = palette.getMutedSwatch();//获取有活力 暗色的样本

                if (mutedSwatch != null) {
                    Log.d("test", "柔和的颜色样本:" + mutedSwatch.getRgb());
                }


                Palette.Swatch lightMutedSwatch = palette.getLightMutedSwatch();//获取有活力 暗色的样本

                if (lightMutedSwatch != null) {
                    Log.d("test", "柔和的亮色颜色样本:" + lightMutedSwatch.getRgb());
                }


                Palette.Swatch darkMutedSwatch = palette.getDarkMutedSwatch();//获取有活力 暗色的样本

                if (darkMutedSwatch != null) {
                    Log.d("test", "柔和暗色颜色样本:" + darkMutedSwatch.getRgb());
                }
 /*               Palette.Swatch lightVibrantSwatch = palette.getLightVibrantSwatch();//获取有活力 亮色的样本
                view2.setBackgroundColor(lightVibrantSwatch.getRgb());

                Palette.Swatch drakVibrantSwatch = palette.getDarkVibrantSwatch();//获取有活力 暗色的样本
                view3.setBackgroundColor(drakVibrantSwatch.getRgb());
                Palette.Swatch mutedSwatch = palette.getMutedSwatch();//获取柔和的颜色样本
                view4.setBackgroundColor(mutedSwatch.getRgb());
                Palette.Swatch lightMutedSwatch = palette.getLightMutedSwatch();//获取柔和 亮色的样本
                view5.setBackgroundColor(lightMutedSwatch.getRgb());*/
                //  Palette.Swatch darkMutedSwatch = palette.getDarkMutedSwatch();//获取柔和 暗色的样本
                //  view1.setBackgroundColor(darkMutedSwatch.getRgb());
            }
        });

    }


    private void selectImage() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(2)
                .compress(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    String imagePath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {// 图片、视频、音频选择结果回调
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                // 例如 LocalMedia 里面返回四种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                // 4.media.getAndroidQToPath();为Android Q版本特有返回的字段，此字段有值就用来做上传使用
                imagePath = selectList.get(0).getPath();
                getColor();
                //   merge(selectList);
            }
        }
    }
}
