package com.z.palette.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.z.palette.BitmapUtils
import com.z.palette.R
import com.z.palette.app.LoadImageEngine
import java.lang.Exception


class AddWordsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_words)

        val addWordsImage=findViewById<ImageView>(R.id.addWordsImage)
        val addWordsTvWords=findViewById<TextView>(R.id.addWordsTvWords)
        val addWordsTvAuthor=findViewById<TextView>(R.id.addWordsTvAuthor)
        findViewById<View>(R.id.addWordsLayout).setOnLongClickListener { v ->
            if (v != null) {
                saveBitmap(v)
            }
            true
        }

        findViewById<View>(R.id.addWordsImage).setOnClickListener {

            PictureSelector.create(this)
                    .openGallery(PictureMimeType.ofImage())
                    .maxSelectNum(1)
                    .isCompress(true)
                    .imageEngine(LoadImageEngine())
                    .forResult(object:OnResultCallbackListener<LocalMedia>{
                        override fun onResult(result: MutableList<LocalMedia>?) {

                            if(result?.get(0)?.isCompressed!!){
                             Glide.with(this@AddWordsActivity).load(result[0].compressPath).into(addWordsImage)
                            }

                        }

                        override fun onCancel() {

                        }

                    })

        }
     val addWordsEdt=   findViewById<EditText>(R.id.addWordsEdt)
       // addWordsEdt.setText("这个五彩斑斓的世界，只需要RGB三原色就可以混合出来，三原色可以表示为向量组，所有颜色构成的向量空间，即色彩空间。——线性代数")
        addWordsEdt.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val text=s.toString();

                try {
                    val lists = text.split("——")

                    if(lists.isNullOrEmpty()){
                        Toast.makeText(baseContext,"format error",Toast.LENGTH_SHORT).show()
                    } else if (lists.size==1){

                        addWordsTvWords.text=lists[0]
                        addWordsTvAuthor.text=""
                        //hide the view if there is no author.
                        addWordsTvAuthor.visibility=View.GONE
                      //  addWordsTvAuthor.text="———"+lists[1]
                    }else{
                        addWordsTvAuthor.visibility=View.VISIBLE
                        addWordsTvWords.text=lists[0]
                        addWordsTvAuthor.text="——"+lists[1]
                    }



                }catch (e:Exception){

                }




            }

        })

    }
    /**
     * view转bitmap
     */
    fun saveBitmap(v: View){
        val w = v.width
        val h = v.height
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(bmp)
        c.drawColor(Color.WHITE)
        /** 如果不设置canvas画布为白色，则生成透明  */
        v.layout(0, 0, w, h)
        v.draw(c)

        Toast.makeText(this,"saved successful",Toast.LENGTH_SHORT).show()
        BitmapUtils.saveImage(bmp,applicationContext)


    }
}
