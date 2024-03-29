package com.z.palette.ui.home


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.palette.Palette
import com.z.palette.BitmapUtils
import com.z.palette.R
import com.z.palette.adapter.PaletteColorsBean
import com.z.palette.adapter.RecyclerViewAdapter
import com.z.palette.base.BaseFragment
import com.z.palette.tool.ThemeUtils
import com.z.palette.view.ColorSeekBar
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
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


    override fun initView(view: View) {
        mRecyclerView =view. findViewById(R.id.recyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(activity)

        mColorSeekBar = view.findViewById(R.id.colorSeekBar)


        val pickMedia = selectImage()
        view.findViewById<Button>(R.id.selectView).setOnClickListener {
         //   selectImage()
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        }

     //   Beta.checkUpgrade()

        arguments?.let {
            imagePath = it.getString("imagePath")
        }
       // getColor(bitmap)
    }



    private fun getColor(bitmap: Bitmap) {

        val maximumColorCount: Int = mColorSeekBar.getProgress()
        val options = BitmapFactory.Options()
        //不加载图片到内存里面
        options.inJustDecodeBounds = false
        //目标bitmap
     //   val bitmap = BitmapFactory.decodeFile(imagePath, options)
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


          //  val bitmap1 = BitmapFactory.decodeStream(FileInputStream(File(imagePath)))

            paletteColorsBeans.add(PaletteColorsBean( BitmapUtils.newBitmap(bitmap, swatchList,BitmapUtils.Draw.left)))
            paletteColorsBeans.add(PaletteColorsBean( BitmapUtils.newBitmap(bitmap, swatchList,BitmapUtils.Draw.right)))
            paletteColorsBeans.add(PaletteColorsBean( BitmapUtils.newBitmap(bitmap, swatchList,BitmapUtils.Draw.top)))
            paletteColorsBeans.add(PaletteColorsBean( BitmapUtils.newBitmap(bitmap, swatchList,BitmapUtils.Draw.bottom)))

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
                Log.d("test", "柔和的亮色颜色样本:" + lightMutedSwatch.hsl)
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


                if(selectList[0].compressPath.isNullOrEmpty()){

                    if(selectList[0].path .isNullOrEmpty()){
                        Toast.makeText(activity, "图片路径为空！", Toast.LENGTH_SHORT).show()
                        return
                    }

                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        imagePath = selectList[0].androidQToPath
                    }else{
                        imagePath = selectList[0].path
                    }

                }else{
                    imagePath = selectList[0].compressPath
                }
                //getColor(bitmap)

                if(imagePath.isNullOrEmpty()){
                    return
                }

                //copyFile(imagePath!!,context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.path)

                //   merge(selectList);
            }
        }
    }

    /**
     * 复制文件
     * @param fileInputStream
     * @param fileOutputStream
     *
     *
     *
     * @return
     */
    fun copyFile(fileInput: String, fileOutput: String): Boolean {
        return try {

            val inputFile=File(fileInput);
            val outputFile=File(fileOutput,inputFile.name);
            val fileInputStream=FileInputStream(inputFile)
            val fileOutputStream=FileOutputStream(outputFile)
            val buffer = ByteArray(1024)
            var byteRead: Int
            while (fileInputStream.read(buffer).also { byteRead = it } != -1) {
                fileOutputStream.write(buffer, 0, byteRead)
            }
            fileInputStream.close()
            fileOutputStream.flush()
            fileOutputStream.close()
            true
        } catch (e: java.lang.Exception) {
            false
        }
    }
    private fun selectImage(): ActivityResultLauncher<PickVisualMediaRequest> {
        // Registers a photo picker activity launcher in single-select mode.
        return registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {

                val bitmap=BitmapFactory.decodeStream(this.context?.contentResolver?.openInputStream(uri))

                getColor(bitmap)

               // Glide.with(this).load(uri).into(addWordsImage)

                Log.d("PhotoPicker", "Selected URI: $uri")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
    }

    private fun dd(){
      //  val selectList = PictureSelector.obtainMultipleResult(data)
        // 例如 LocalMedia 里面返回四种path
        // 1.media.getPath(); 为原图path
        // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
        // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
        // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
        // 4.media.getAndroidQToPath();为Android Q版本特有返回的字段，此字段有值就用来做上传使用



        //copyFile(imagePath!!,context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.path)

        //   merge(selectList);
    }
}
