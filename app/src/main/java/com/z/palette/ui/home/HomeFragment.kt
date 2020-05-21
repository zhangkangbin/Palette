package com.z.palette.ui.home


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.palette.Palette
import com.tencent.bugly.beta.Beta
import com.z.palette.BitmapUtils
import com.z.palette.R
import com.z.palette.adapter.PaletteColorsBean
import com.z.palette.adapter.RecyclerViewAdapter
import com.z.palette.base.BaseFragment
import com.z.palette.tool.ThemeUtils
import com.z.palette.view.ColorSeekBar
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : BaseFragment() {

    private lateinit var mRecyclerView: RecyclerView

    private lateinit var mColorSeekBar: ColorSeekBar
    private var imagePath: String? = null
    private var newBmp: Bitmap? = null
    private var imageFile: File? = null

/*
    fun  newFragment(imagePath:String):Fragment{
        val fragment = HomeFragment()
        val bundle = Bundle()
        bundle.putString("imagePath", imagePath)
        fragment.arguments = bundle

        return fragment
    }*/


    override fun initView(view: View) {
        mRecyclerView =view. findViewById(R.id.recyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(activity)


        mColorSeekBar = view.findViewById(R.id.colorSeekBar)

        view.findViewById<Button>(R.id.selectView).setOnClickListener {
            selectImage()
        }



  /*      findViewById<View>(R.id.themeDesign).setOnClickListener { v: View? ->
            startActivity(Intent(this, ThemeDesignActivity::class.java))
        }
        findViewById<View>(R.id.themeDesignList).setOnClickListener { v: View? ->
            startActivity(Intent(this, ThemeDesignListActivity::class.java))
        }*/

        Beta.checkUpgrade()


        arguments?.let {
            imagePath = it.getString("imagePath")
        }
        getColor()
    }
    private fun selectImage() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(2)
                .compress(true)
                .forResult(PictureConfig.CHOOSE_REQUEST)
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
        //颜色量化算法 ：中位切割
        val builder = Palette.from(bitmap)
        builder.maximumColorCount(maximumColorCount)
        builder.addFilter(object : Palette.Filter {
            //通过明度判定
            private val BLACK_MAX_LIGHTNESS = 0.05f
            private val WHITE_MIN_LIGHTNESS = 0.95f

            /**
             * @return true if the color represents a color which is close to black.
             */
            private fun isBlack(hslColor: FloatArray): Boolean {

                return hslColor[2] <= BLACK_MAX_LIGHTNESS
            }

            /**
             * @return true if the color represents a color which is close to white.
             */
            private fun isWhite(hslColor: FloatArray): Boolean {

                Log.d("test", "过滤白色")

                return hslColor[2] >= WHITE_MIN_LIGHTNESS
            }

            private fun isNearRedILine(hslColor: FloatArray): Boolean {
                return hslColor[0] >= 10f && hslColor[0] <= 37f && hslColor[1] <= 0.82f
            }

            override fun isAllowed(rgb: Int, hsl: FloatArray): Boolean {

                //H：https://zh.wikipedia.org/wiki/%E8%89%B2%E7%9B%B8
                //Hue(色调)。0(或360)表示红色，120表示绿色，240表示蓝色，也可取其他数值来指定颜色。取值为：0 - 360
                //S：
                //Saturation(饱和度)。取值为：0.0% - 100.0%
                //L：
                //Lightness(亮度)。取值为：0.0% - 100.0%
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


            val bitmap1 = BitmapFactory.decodeStream(FileInputStream(File(imagePath)))

            paletteColorsBeans.add(PaletteColorsBean( BitmapUtils.newBitmap(bitmap1, swatchList,BitmapUtils.Draw.left)))
            paletteColorsBeans.add(PaletteColorsBean( BitmapUtils.newBitmap(bitmap1, swatchList,BitmapUtils.Draw.right)))
            paletteColorsBeans.add(PaletteColorsBean( BitmapUtils.newBitmap(bitmap1, swatchList,BitmapUtils.Draw.top)))
            paletteColorsBeans.add(PaletteColorsBean( BitmapUtils.newBitmap(bitmap1, swatchList,BitmapUtils.Draw.bottom)))

            val colorsList = ArrayList<Int>()
            for (color in swatchList) {

                //  paletteColorsBeans.add(PaletteColorsBean(color, "#"+getRgb(color.rgb) + "+color:"+color.hsl.toString()))
                paletteColorsBeans.add(PaletteColorsBean(color, ThemeUtils.getRgb(color.rgb)))
                colorsList.add(color.rgb)
            }


           // mergeColor(swatchList)
            val vibrantSwatch = palette.vibrantSwatch //获取有活力的颜色样本
            if (vibrantSwatch != null) {
                paletteColorsBeans.add(PaletteColorsBean(vibrantSwatch, "活力的颜色样本${ThemeUtils.getRgb(vibrantSwatch.rgb)}"))
                Log.d("test", "活力的颜色样本:" + vibrantSwatch.titleTextColor)
            }
            val drakVibrantSwatch = palette.darkVibrantSwatch //获取有活力 暗色的样本
            if (drakVibrantSwatch != null) {
                Log.d("test", "有活力暗色的样本:" + drakVibrantSwatch.titleTextColor)
                paletteColorsBeans.add(PaletteColorsBean(drakVibrantSwatch, "有活力暗色的样本${ThemeUtils.getRgb(drakVibrantSwatch.rgb)}"))
            }
            val mutedSwatch = palette.mutedSwatch //获取有活力 暗色的样本
            if (mutedSwatch != null) {
                Log.d("test", "柔和的颜色样本:" + mutedSwatch.rgb)
                paletteColorsBeans.add(PaletteColorsBean(mutedSwatch, "柔和的颜色样本${ThemeUtils.getRgb(mutedSwatch.rgb)}"))
            }
            val lightMutedSwatch = palette.lightMutedSwatch //获取有活力 暗色的样本
            if (lightMutedSwatch != null) {
                Log.d("test", "柔和的亮色颜色样本:" + lightMutedSwatch.rgb)
                paletteColorsBeans.add(PaletteColorsBean(lightMutedSwatch, "柔和的亮色颜色样本${ThemeUtils.getRgb(lightMutedSwatch.rgb)}"))
            }
            val darkMutedSwatch = palette.darkMutedSwatch //获取有活力 暗色的样本
            if (darkMutedSwatch != null) {
                Log.d("test", "柔和暗色颜色样本:" + darkMutedSwatch.rgb)
                paletteColorsBeans.add(PaletteColorsBean(darkMutedSwatch, "柔和暗色颜色样本${ThemeUtils.getRgb(darkMutedSwatch.rgb)}" ))
            }

            mRecyclerView.adapter = RecyclerViewAdapter(paletteColorsBeans)
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
         //   mImageCard.setImageBitmap(newBmp)
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
