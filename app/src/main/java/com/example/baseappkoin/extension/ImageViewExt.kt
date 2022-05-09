package com.example.baseappkoin.extension

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.example.baseappkoin.BuildConfig
import com.example.baseappkoin.GlideApp
import com.example.baseappkoin.R
import jp.wasabeef.glide.transformations.BlurTransformation

fun ImageView.loadPhotoFullUrl(
    url: String,
    colorInt: Int? = null,
    colorString: String? = null,
    requestListener: RequestListener<Drawable>? = null
) {
    colorInt?.let { background = ColorDrawable(it) }
    colorString?.let { background = ColorDrawable(Color.parseColor(it)) }
    GlideApp.with(context)
        .load(url)
        .addListener(requestListener)
        .into(this)
        .clearOnDetach()
}

