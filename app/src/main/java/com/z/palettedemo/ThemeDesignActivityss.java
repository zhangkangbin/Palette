package com.z.palettedemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.z.palettedemo.adapter.ThemeListAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * @author zhangkangbin
 */
public class ThemeDesignActivityss extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_design2);


        setTitle("主题卡");

        initAdapter();
    }

    private RecyclerView recyclerView;
    private List<String> stringList = new ArrayList<>();

    private void initAdapter() {

        recyclerView = findViewById(R.id.recyclerViewTheme);
       recyclerView.setLayoutManager(new GridLayoutManager(this,2));
      //  recyclerView.setLayoutManager(new LinearLayoutManager(this));


        stringList.add("header");
        ThemeListAdapter themeListAdapter = new ThemeListAdapter(stringList);
        themeListAdapter.setSelectImage(v -> {
            selectImage();
        });
        recyclerView.setAdapter(themeListAdapter);
    }


    private void selectImage() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(9)
                .compress(true)
             //   .enableCrop(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }


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

                setListImage(selectList);
                //   merge(selectList);
            }
        }
    }

    private void setListImage(List<LocalMedia> selectList) {


        for (LocalMedia image : selectList) {
            stringList.add(image.getPath());

        }

        recyclerView.getAdapter().notifyDataSetChanged();

    }
}
