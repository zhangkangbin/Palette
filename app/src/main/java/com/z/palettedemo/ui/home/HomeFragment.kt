package com.z.palettedemo.ui.home


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.tencent.bugly.beta.Beta
import com.z.palettedemo.BitmapUtils

import com.z.palettedemo.R

import com.z.palettedemo.adapter.PaletteColorsBean
import com.z.palettedemo.adapter.RecyclerViewAdapter
import com.z.palettedemo.base.BaseFragment
import com.z.palettedemo.tool.ThemeUtils
import com.z.palettedemo.view.ColorSeekBar
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : BaseFragment() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mImageCard: ImageView
    private lateinit var mColorSeekBar: ColorSeekBar
    private var imagePath: String? = null
    var newBmp: Bitmap? = null
    var imageFile: File? = null

    override fun initView(view: View) {
        mRecyclerView =view. findViewById(R.id.recyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(activity)

        mImageCard = view.findViewById(R.id.imageCard)
        mColorSeekBar = view.findViewById(R.id.colorSeekBar)

        view.findViewById<Button>(R.id.selectView).setOnClickListener {
            selectImage()
        }
        view.findViewById<Button>(R.id.saveImage).setOnClickListener {
            saveImage()
        }


  /*      findViewById<View>(R.id.themeDesign).setOnClickListener { v: View? ->
            startActivity(Intent(this, ThemeDesignActivity::class.java))
        }
        findViewById<View>(R.id.themeDesignList).setOnClickListener { v: View? ->
            startActivity(Intent(this, ThemeDesignListActivity::class.java))
        }*/

        Beta.checkUpgrade()
    }
    private fun selectImage() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(2)
                .compress(true)
                .forResult(PictureConfig.CHOOSE_REQUEST)
    }

    private fun saveImage() {

        if (imageFile == null) return
        val newFile = File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), Date().toString() + imageFile!!.name)
        if (!newFile.exists()) {
            try {
                newFile.createNewFile()
                BitmapUtils.save(newBmp, newFile, Bitmap.CompressFormat.JPEG, true)
                activity?.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(newFile)))
                Toast.makeText(activity, "save success", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }


    private fun getColor() {
        if (TextUtils.isEmpty(imagePath)) {
            return
        }
        val maximumColorCount: Int = mColorSeekBar.getProgress()
        val options = BitmapFactory.Options()
        //不加载图片到内存里面
        options.inJustDecodeBounds = false
        //目标bitmap
        val bitmap = BitmapFactory.decodeFile(imagePath, options)
        //  Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aa);
        val builder = Palette.from(bitmap)
        builder.maximumColorCount(maximumColorCount)
        builder.addFilter(object : Palette.Filter {
            private val BLACK_MAX_LIGHTNESS = 0.05f
            private val WHITE_MIN_LIGHTNESS = 0.95f

            /**
             * @return true if the color represents a color which is close to black.
             */
            private fun isBlack(hslColor: FloatArray): Boolean {
                if (hslColor[2] >= BLACK_MAX_LIGHTNESS) {
                    Log.d("test", "过滤黑色")
                }
                return hslColor[2] <= BLACK_MAX_LIGHTNESS
            }

            /**
             * @return true if the color represents a color which is close to white.
             */
            private fun isWhite(hslColor: FloatArray): Boolean {
                if (hslColor[2] >= WHITE_MIN_LIGHTNESS) {
                    Log.d("test", "过滤白色")
                }
                return hslColor[2] >= WHITE_MIN_LIGHTNESS
            }

            private fun isNearRedILine(hslColor: FloatArray): Boolean {
                return hslColor[0] >= 10f && hslColor[0] <= 37f && hslColor[1] <= 0.82f
            }

            override fun isAllowed(rgb: Int, hsl: FloatArray): Boolean {
                val builder1 = StringBuilder()
                for (f in hsl) {
                    builder1.append("$f-")
                }
                Log.d("test", rgb.toString() + "____" + builder1.toString())
                return !isWhite(hsl) && !isBlack(hsl) && !isNearRedILine(hsl)
            }
        })
        builder.generate(Palette.PaletteAsyncListener { palette ->
            if (palette == null) {
                return@PaletteAsyncListener
            }
            if (palette.swatches.isEmpty()) {
                return@PaletteAsyncListener
            }
            val swatchList = palette.swatches
            val paletteColorsBeans: MutableList<PaletteColorsBean> = ArrayList()
            val colorsList = ArrayList<Int>()
            for (color in swatchList) {

                //  paletteColorsBeans.add(PaletteColorsBean(color, "#"+getRgb(color.rgb) + "+color:"+color.hsl.toString()))
                paletteColorsBeans.add(PaletteColorsBean(color, ThemeUtils.getRgb(color.rgb)))
                colorsList.add(color.rgb)
            }


            /*           val intent = Intent(this, SelectThemeTypeActivity::class.java)
                       intent.putIntegerArrayListExtra("data",colorsList)
                       startActivity(intent)
           */
            mergeColor(swatchList)
            val vibrantSwatch = palette.vibrantSwatch //获取有活力的颜色样本
            if (vibrantSwatch != null) {
                paletteColorsBeans.add(PaletteColorsBean(vibrantSwatch, "活力的颜色样本"))
                Log.d("test", "活力的颜色样本:" + vibrantSwatch.titleTextColor)
            }
            val drakVibrantSwatch = palette.darkVibrantSwatch //获取有活力 暗色的样本
            if (drakVibrantSwatch != null) {
                Log.d("test", "有活力暗色的样本:" + drakVibrantSwatch.titleTextColor)
                paletteColorsBeans.add(PaletteColorsBean(drakVibrantSwatch, "有活力暗色的样本"))
            }
            val mutedSwatch = palette.mutedSwatch //获取有活力 暗色的样本
            if (mutedSwatch != null) {
                Log.d("test", "柔和的颜色样本:" + mutedSwatch.rgb)
                paletteColorsBeans.add(PaletteColorsBean(mutedSwatch, "柔和的颜色样本"))
            }
            val lightMutedSwatch = palette.lightMutedSwatch //获取有活力 暗色的样本
            if (lightMutedSwatch != null) {
                Log.d("test", "柔和的亮色颜色样本:" + lightMutedSwatch.rgb)
                paletteColorsBeans.add(PaletteColorsBean(lightMutedSwatch, "柔和的亮色颜色样本"))
            }
            val darkMutedSwatch = palette.darkMutedSwatch //获取有活力 暗色的样本
            if (darkMutedSwatch != null) {
                Log.d("test", "柔和暗色颜色样本:" + darkMutedSwatch.rgb)
                paletteColorsBeans.add(PaletteColorsBean(darkMutedSwatch, "柔和暗色颜色样本1" + darkMutedSwatch.titleTextColor))
            }

            mRecyclerView.setAdapter(RecyclerViewAdapter(paletteColorsBeans))
        })
    }


    private fun mergeColor(swatches: List<Palette.Swatch>) {
        if (imagePath == null) {
            return
        }
        imageFile = File(imagePath)
        try {
            val bitmap1 = BitmapFactory.decodeStream(FileInputStream(imageFile))
            newBmp = BitmapUtils.drawLeft(bitmap1, swatches)
            mImageCard.setImageBitmap(newBmp)
            Toast.makeText(activity, "成功", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            // 图片、视频、音频选择结果回调
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                val selectList = PictureSelector.obtainMultipleResult(data)
                // 例如 LocalMedia 里面返回四种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                // 4.media.getAndroidQToPath();为Android Q版本特有返回的字段，此字段有值就用来做上传使用
                imagePath = selectList[0].compressPath
                getColor()
                //   merge(selectList);
            }
        }
    }

}
