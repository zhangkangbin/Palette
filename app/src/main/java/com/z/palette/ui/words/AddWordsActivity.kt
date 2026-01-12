package com.z.palette.ui.words

import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.z.palette.R
import java.lang.Exception


class AddWordsActivity : AppCompatActivity() {
    // 保存原始图片的Bitmap
    private var originalBitmap: Bitmap? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_words)

        val model: AddWordsViewModel by viewModels()
        val addWordsImage = findViewById<ImageView>(R.id.addWordsImage)
        val addWordsTvWords = findViewById<TextView>(R.id.addWordsTvWords)
        val addWordsEdt = findViewById<EditText>(R.id.addWordsEdt)
        
        findViewById<View>(R.id.addWordsLayout).setOnLongClickListener { v ->
            // 获取当前输入的文字
            val text = addWordsTvWords.text.toString()
            
            if (originalBitmap != null && text.isNotEmpty()) {
                model.saveBitmap(applicationContext, originalBitmap, text) {
                    Toast.makeText(this, "saved successful", Toast.LENGTH_SHORT).show()
                }
            } else {
                if (originalBitmap == null) {
                    Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Please input some text", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
        val pickMedia = selectImage(addWordsImage)
        findViewById<View>(R.id.addWordsImage).setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        // addWordsEdt.setText("这个五彩斑斓的世界，只需要RGB三原色就可以混合出来，三原色可以表示为向量组，所有颜色构成的向量空间，即色彩空间。——线性代数")
        addWordsEdt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val text = s.toString();

                try {
                    val lists = text.split("——")

                    if (lists.isEmpty()) {
                        Toast.makeText(baseContext, "format error", Toast.LENGTH_SHORT).show()
                    } else if (lists.size == 1) {

                        addWordsTvWords.text = lists[0]
                        //hide the view if there is no author.
                        //  addWordsTvAuthor.text="———"+lists[1]
                    } else {
                        addWordsTvWords.text = lists[0]
                    }


                } catch (e: Exception) {

                }


            }

        })

    }

    override fun onDestroy() {
        super.onDestroy()
        // 释放Bitmap内存
        originalBitmap?.recycle()
        originalBitmap = null
    }


    private fun selectImage(addWordsImage: ImageView): ActivityResultLauncher<PickVisualMediaRequest> {
        // Registers a photo picker activity launcher in single-select mode.
        return registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                // 显示图片到ImageView
                Glide.with(this@AddWordsActivity).load(uri).into(addWordsImage)

                // 加载原始Bitmap用于后续绘制文字
                try {
                    val inputStream = contentResolver.openInputStream(uri)
                    originalBitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
                    inputStream?.close()
                    Log.d("PhotoPicker", "Selected URI: $uri, Bitmap loaded: ${originalBitmap?.width}x${originalBitmap?.height}")
                } catch (e: Exception) {
                    Log.e("PhotoPicker", "Failed to load bitmap", e)
                    Toast.makeText(this@AddWordsActivity, "Failed to load image", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
    }

}
