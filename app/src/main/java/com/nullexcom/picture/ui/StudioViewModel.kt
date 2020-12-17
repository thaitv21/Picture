package com.nullexcom.picture.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toFile
import com.nullexcom.editor.data.Photo
import com.nullexcom.picture.data.Firebase
import com.nullexcom.picture.data.PhotoRepository
import com.nullexcom.picture.data.Template
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.*
import java.io.File
import javax.inject.Inject

class StudioViewModel @Inject constructor(private val photoRepository: PhotoRepository) {

    enum class State {
        NONE,
        LOADING
    }

    var photos: BehaviorSubject<List<Photo>> = BehaviorSubject.create<List<Photo>>()
    val state: BehaviorSubject<State> = BehaviorSubject.createDefault(State.NONE)

    fun photos(): Observable<List<Photo>> {
        val list = photoRepository.photos()
        photos.onNext(list)
        return photos
    }

    fun handleAddImage(intent: Intent?) {
        val data = intent ?: return
        val uri = data.data ?: return
        photoRepository.addPhoto(uri)
        photos()
    }

    fun removePhoto(photo: Photo) {
        val file = File(photo.uri.encodedPath!!)
        file.delete()
        photos()
    }

    fun publish(context: Context, photo: Photo) {
        state.onNext(State.LOADING)
        val originalFile = photo.uri.toFile()
        val file = File(context.getExternalFilesDir("out"), originalFile.name)
        val uri = if (file.exists()) Uri.fromFile(file) else Uri.fromFile(originalFile)
        val name = uri.toFile().name
        val template = getTemplate(context, photo.uri)
        CoroutineScope(Dispatchers.IO).launch {
            val uploadedUri = Firebase.uploadImage(name, uri)
            Firebase.addImage(uploadedUri, template)
        }.invokeOnCompletion {
            state.onNext(State.NONE)
        }
    }

    fun delete(context: Context, photo: Photo) {
        val uri = photo.uri
        val originalFile = photo.uri.toFile()
        val file = File(context.getExternalFilesDir("out"), originalFile.name)
        val templateFile = File(context.getExternalFilesDir("presets"), "${uri.toFile().nameWithoutExtension}.preset")
        if (originalFile.exists()) originalFile.delete()
        if (file.exists()) file.delete()
        if (templateFile.exists()) templateFile.delete()
        val list = photoRepository.photos()
        photos.onNext(list)
    }

    private fun getTemplate(context: Context, uri: Uri): Template {
        val dir = context.getExternalFilesDir("presets")
        val file = File(dir, "${uri.toFile().nameWithoutExtension}.preset")
        return if (file.exists()) {
            Template.parse(file.readText())
        } else {
            Template()
        }
    }
}