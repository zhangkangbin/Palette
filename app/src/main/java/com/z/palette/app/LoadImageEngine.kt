package com.z.palette.app

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.luck.picture.lib.engine.ImageEngine
import com.luck.picture.lib.listener.OnImageCompleteCallback
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView

class LoadImageEngine : ImageEngine{
    override fun loadImage(context: Context, url: String, imageView: ImageView) {

        Glide.with(context).load(url).into(imageView);
    }

    override fun loadImage(context: Context, url: String, imageView: ImageView, longImageView: SubsamplingScaleImageView?, callback: OnImageCompleteCallback?) {

    }

    override fun loadImage(context: Context, url: String, imageView: ImageView, longImageView: SubsamplingScaleImageView?) {

    }

    override fun loadAsGifImage(context: Context, url: String, imageView: ImageView) {
        Glide.with(context).load(url).into(imageView);
    }

    override fun loadGridImage(context: Context, url: String, imageView: ImageView) {
        Glide.with(context).load(url).into(imageView);
    }

    override fun loadFolderImage(context: Context, url: String, imageView: ImageView) {
        Glide.with(context).load(url).into(imageView);
    }
}