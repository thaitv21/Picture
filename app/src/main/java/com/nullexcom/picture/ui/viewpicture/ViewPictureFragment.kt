package com.nullexcom.picture.ui.viewpicture

import android.content.ContentValues
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nullexcom.picture.R
import com.nullexcom.picture.data.Picture
import com.nullexcom.picture.ui.dialog.LoadingDialog
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_view_picture.*
import java.io.File


class ViewPictureFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_view_picture, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = view.context
        val picture : Picture = arguments?.get("picture")?.let { it as Picture } ?: return
        Glide.with(context).load(picture.url).into(photoView)
        viewDownloadImage.setOnClickListener { downloadImage(picture) }
        viewDownloadPreset.setOnClickListener { downloadPreset(picture) }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog ?: return
        val params = dialog.window?.attributes ?: return
        params.height = resources.displayMetrics.heightPixels
        dialog.window?.attributes = params
    }

    private fun downloadImage(picture: Picture) {
        val context = context ?: return
        val loadingDialog = LoadingDialog(context)
        loadingDialog.show()
        val name = "Picture_${System.currentTimeMillis()}.png"
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, name)
        values.put(MediaStore.Images.Media.DISPLAY_NAME, name)
        values.put(MediaStore.Images.Media.DESCRIPTION, android.R.attr.description)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
        val contentResolver = context.contentResolver
        val insertUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (insertUri == null) {
            loadingDialog.cancel()
            dismiss()
            return
        }
        Completable.defer {
            val bitmap = Glide.with(context).asBitmap().load(picture.url).submit().get()
            contentResolver.openOutputStream(insertUri)?.apply {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, this)
                close()
            }
            Completable.complete()
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnComplete {
            loadingDialog.cancel()
            Toast.makeText(context, "Download completely.", Toast.LENGTH_SHORT).show()
            dismiss()
        }.subscribe()
    }

    private fun downloadPreset(picture: Picture) {
        val ctx = context ?: return
        val template = picture.template
        val dir = ctx.getExternalFilesDir("downloaded_presets")
        val file = File(dir, "${System.currentTimeMillis()}.preset")
        file.writeText(template)
        Toast.makeText(context, "Download completely.", Toast.LENGTH_SHORT).show()
        dismiss()
    }
}