package com.z.palettedemo;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
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
import com.z.palettedemo.adapter.PaletteColorsBean;
import com.z.palettedemo.adapter.RecyclerViewAdapter;
import com.z.palettedemo.view.ColorSeekBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView imageCard;
    ColorSeekBar colorSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        imageCard = findViewById(R.id.imageCard);
        findViewById(R.id.selectView).setOnClickListener(v -> selectImage());
        findViewById(R.id.saveImage).setOnClickListener(v -> saveImage());
        colorSeekBar = findViewById(R.id.colorSeekBar);

        findViewById(R.id.themeDesign).setOnClickListener(v -> startActivity(new Intent(this, ThemeDesignActivity.class)));
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

    Bitmap newBmp;
    File imageFile;

    private void mergeColor(List<Palette.Swatch> swatches) {

        if (imagePath == null) return;

        imageFile = new File(imagePath);

        try {

            Bitmap bitmap1 = BitmapFactory.decodeStream(new FileInputStream(imageFile));

            newBmp = BitmapUtils.newBitmap2(bitmap1, swatches);
            imageCard.setImageBitmap(newBmp);

            Toast.makeText(this, "成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void saveImage() {
        File newFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), new Date().toString() + imageFile.getName());
        if (!newFile.exists()) {
            try {
                newFile.createNewFile();
                BitmapUtils.save(newBmp, newFile, Bitmap.CompressFormat.JPEG, true);

                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(newFile)));

                Toast.makeText(this, "save success", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private void getColor() {

        if (TextUtils.isEmpty(imagePath)) {
            return;
        }

        int maximumColorCount = colorSeekBar.getProgress();
        BitmapFactory.Options options = new BitmapFactory.Options();
        //不加载图片到内存里面
        options.inJustDecodeBounds = false;
        //目标bitmap
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        //  Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aa);

        Palette.Builder builder = Palette.from(bitmap);
        builder.maximumColorCount(maximumColorCount);
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

            private boolean isNearRedILine(float[] hslColor) {
                return hslColor[0] >= 10f && hslColor[0] <= 37f && hslColor[1] <= 0.82f;
            }

            @Override
            public boolean isAllowed(int rgb, @NonNull float[] hsl) {
                StringBuilder builder1 = new StringBuilder();
                for (Float f : hsl) {
                    builder1.append(f + "-");
                }
                Log.d("test", rgb + "____" + builder1.toString());
                return !isWhite(hsl) && !isBlack(hsl) && !isNearRedILine(hsl);

            }
        });
        builder.generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {

                if (palette == null) return;
                if (palette.getSwatches().isEmpty()) return;

                List<Palette.Swatch> swatchList = palette.getSwatches();
                List<PaletteColorsBean> paletteColorsBeans=new ArrayList<>();
                for(Palette.Swatch color :swatchList){
                    paletteColorsBeans.add(new PaletteColorsBean(color,color.getRgb()+""));
                }

                mergeColor(swatchList);
                Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();//获取有活力的颜色样本
                if (vibrantSwatch != null) {
                    paletteColorsBeans.add(new PaletteColorsBean(vibrantSwatch,"活力的颜色样本"));
                    Log.d("test", "活力的颜色样本:" + vibrantSwatch.getTitleTextColor());
                }
                Palette.Swatch drakVibrantSwatch = palette.getDarkVibrantSwatch();//获取有活力 暗色的样本

                if (drakVibrantSwatch != null) {
                    Log.d("test", "有活力暗色的样本:" + drakVibrantSwatch.getTitleTextColor());

                    paletteColorsBeans.add(new PaletteColorsBean(drakVibrantSwatch,"有活力暗色的样本"));
                }


                Palette.Swatch mutedSwatch = palette.getMutedSwatch();//获取有活力 暗色的样本

                if (mutedSwatch != null) {
                    Log.d("test", "柔和的颜色样本:" + mutedSwatch.getRgb());
                    paletteColorsBeans.add(new PaletteColorsBean(mutedSwatch,"柔和的颜色样本"));
                }


                Palette.Swatch lightMutedSwatch = palette.getLightMutedSwatch();//获取有活力 暗色的样本

                if (lightMutedSwatch != null) {
                    Log.d("test", "柔和的亮色颜色样本:" + lightMutedSwatch.getRgb());

                    paletteColorsBeans.add(new PaletteColorsBean(lightMutedSwatch,"柔和的亮色颜色样本"));
                }


                Palette.Swatch darkMutedSwatch = palette.getDarkMutedSwatch();//获取有活力 暗色的样本

                if (darkMutedSwatch != null) {
                    Log.d("test", "柔和暗色颜色样本:" + darkMutedSwatch.getRgb());

                    paletteColorsBeans.add(new PaletteColorsBean(darkMutedSwatch,"柔和暗色颜色样本1"+darkMutedSwatch.getTitleTextColor()));
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


                recyclerView.setAdapter(new RecyclerViewAdapter(paletteColorsBeans));
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
                imagePath = selectList.get(0).getCompressPath();
                getColor();
                //   merge(selectList);
            }
        }
    }
}
