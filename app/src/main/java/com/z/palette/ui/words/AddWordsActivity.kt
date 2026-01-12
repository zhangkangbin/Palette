package com.z.palette.ui.words

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
                // 播放保存动画
                playSaveAnimation(v) {
                    // 动画完成后在子线程执行保存
                    lifecycleScope.launch {
                        try {
                            // 在IO线程执行保存操作
                            withContext(Dispatchers.IO) {
                                model.saveBitmap(applicationContext, originalBitmap, text) {
                                    // 回调在子线程，需要切换到主线程更新UI
                                    lifecycleScope.launch(Dispatchers.Main) {
                                        playSuccessAnimation(v)
                                        Toast.makeText(this@AddWordsActivity, "✓ Saved successful", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            // 错误处理，切换到主线程显示错误
                            withContext(Dispatchers.Main) {
                                playErrorAnimation(v)
                                Toast.makeText(this@AddWordsActivity, "Save failed: ${e.message}", Toast.LENGTH_SHORT).show()
                                Log.e("AddWordsActivity", "Save failed", e)
                            }
                        }
                    }
                }
            } else {
                // 播放错误抖动动画
                playErrorAnimation(v)
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

    /**
     * 播放保存动画 - 缩小效果
     */
    private fun playSaveAnimation(view: View, onAnimationEnd: () -> Unit) {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.95f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.95f)
        val alpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 0.8f)
        
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY, alpha)
        animatorSet.duration = 200
        animatorSet.interpolator = AccelerateDecelerateInterpolator()
        
        animatorSet.addListener(object : android.animation.Animator.AnimatorListener {
            override fun onAnimationStart(animation: android.animation.Animator) {}
            override fun onAnimationEnd(animation: android.animation.Animator) {
                onAnimationEnd()
            }
            override fun onAnimationCancel(animation: android.animation.Animator) {}
            override fun onAnimationRepeat(animation: android.animation.Animator) {}
        })
        
        animatorSet.start()
    }

    /**
     * 播放成功动画 - 恢复并放大
     */
    private fun playSuccessAnimation(view: View) {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.95f, 1.05f, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.95f, 1.05f, 1f)
        val alpha = ObjectAnimator.ofFloat(view, "alpha", 0.8f, 1f)
        
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY, alpha)
        animatorSet.duration = 400
        animatorSet.interpolator = OvershootInterpolator()
        animatorSet.start()
    }

    /**
     * 播放错误动画 - 抖动效果
     */
    private fun playErrorAnimation(view: View) {
        val shake = ObjectAnimator.ofFloat(view, "translationX", 0f, -25f, 25f, -25f, 25f, -15f, 15f, -6f, 6f, 0f)
        shake.duration = 500
        shake.start()
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
