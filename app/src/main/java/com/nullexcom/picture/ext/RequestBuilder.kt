package com.nullexcom.editor.ext

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition

fun RequestBuilder<Drawable>.addListener(listener: (Bitmap) -> Unit) : RequestBuilder<Drawable> {
    return this.addListener(object : RequestListener<Drawable> {
        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean) = true

        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
            resource?.apply { listener.invoke(resource.toBitmap()) }
            return true
        }
    })
}

fun RequestBuilder<Bitmap>.doOnCompleted(f: (Bitmap) -> Unit) {
    this.into(object : CustomTarget<Bitmap>() {
        override fun onLoadCleared(placeholder: Drawable?) {

        }

        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            f.invoke(resource)
        }
    })
//
//    this.into(object : Target<T> {
//        override fun onLoadStarted(placeholder: Drawable?) {
//        }
//
//        override fun onLoadFailed(errorDrawable: Drawable?) {
//        }
//
//        override fun getSize(cb: SizeReadyCallback) {
//        }
//
//        override fun getRequest(): Request? = null
//
//        override fun onStop() {
//        }
//
//        override fun setRequest(request: Request?) {
//        }
//
//        override fun removeCallback(cb: SizeReadyCallback) {
//        }
//
//        override fun onLoadCleared(placeholder: Drawable?) {
//        }
//
//        override fun onStart() {
//        }
//
//        override fun onDestroy() {
//        }
//
//        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
//            f.invoke(resource.toBitmap())
//        }

//    })
}