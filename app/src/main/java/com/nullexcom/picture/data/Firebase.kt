package com.nullexcom.picture.data

import android.net.Uri
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.nullexcom.editor.ext.logD
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject


object Firebase {

    fun uploadImage(filename: String, uri: Uri): Uri? {
        val storage = FirebaseStorage.getInstance()
        val storageRef: StorageReference = storage.reference
        val imagesRef = storageRef.child("images")
        val imageRef = imagesRef.child(filename)
        val uploadTask = imageRef.putFile(uri)
        val result = Tasks.await(uploadTask)
        return Tasks.await(result.storage.downloadUrl)
    }

    fun addImage(uploadedUri: Uri?, template: Template) {
        if (uploadedUri == null) return
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser ?: return
        val database = FirebaseDatabase.getInstance()
        val pictures = database.getReference("pictures")
        val url = uploadedUri.toString()
        val userId = user.uid
        val username = user.displayName ?: ""
        val userPhoto = user.photoUrl.toString() ?: ""
        val picture = Picture(url, userId, username, userPhoto, 0, false, template.toString())
        pictures.push().setValue(picture)
    }

    fun observeImages(): Observable<Picture> {
        val observable = BehaviorSubject.create<Picture>()
        val database = FirebaseDatabase.getInstance()
        val pictures = database.reference.child("pictures")
        pictures.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {
                logD(error.message)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                logD(snapshot.key!!)
                val picture = snapshot.getValue(Picture::class.java) ?: return
                observable.onNext(picture)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
        })
        pictures.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                logD("haha")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                logD("haha")
            }

        })
        pictures.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                logD("hih")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                logD("hih")
            }

        })
        return observable
    }

}