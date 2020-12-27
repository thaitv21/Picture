package com.nullexcom.picture

import android.graphics.Bitmap
import com.nullexcom.editor.ext.logD
import com.nullexcom.picture.data.DataStorePreferences
import com.nullexcom.picture.data.Photo
import com.nullexcom.picture.data.PhotoRepository
import com.nullexcom.picture.ext.emptyMatrix
import com.nullexcom.picture.ext.stringify
import com.nullexcom.picture.ext.toPreferBitmap
import junit.framework.Assert.assertTrue
import org.junit.Test

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        val repo = PhotoRepository()
        val photos = repo.photos()
        val photo = photos[0]

        val s = System.currentTimeMillis()
        val bitmap = photo.uri.toPreferBitmap()
        var e = System.currentTimeMillis()
        logD("decode: ${e - s}")

        val copy = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        e = System.currentTimeMillis()
        logD("copy: ${e - s}")
    }
}