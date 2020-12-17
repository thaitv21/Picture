package com.nullexcom.picture.ext

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle

fun <T> Activity.navigate(clazz: Class<T>, data: Bundle? = null) {
    val intent = Intent(this, clazz)
    data?.let { intent.putExtra("data", it) }
    startActivity(intent)
}

fun <T> Activity.navigateAndClearBackStack(clazz: Class<T>, data: Bundle? = null) {
    val intent = Intent(this, clazz)
    data?.let { intent.putExtra("data", it) }
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}

fun Activity.isPermissionGranted(permission: String): Boolean {
    if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
        requestPermissions(arrayOf(permission), 1)
        return false
    }
    return true
}

fun Activity.alert(title: String, message: String) {
    AlertDialog.Builder(this).setTitle(title).setMessage(message).setNegativeButton("OK") { dialog, _ -> dialog.cancel() }.show()
}

fun Activity.alert(title: String, message: String, onClick: () -> Unit) {
    AlertDialog.Builder(this).apply {
        setTitle(title)
        setMessage(message)
        setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        setPositiveButton("OK") { dialog, _ ->
            kotlin.run {
                dialog.cancel()
                onClick.invoke()
            }
        }
    }.show()
}
