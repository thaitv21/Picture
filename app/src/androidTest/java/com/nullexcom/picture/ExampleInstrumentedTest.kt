package com.nullexcom.picture

import com.nullexcom.editor.ext.logD
import com.nullexcom.picture.data.DataStorePreferences
import com.nullexcom.picture.ext.emptyMatrix
import com.nullexcom.picture.ext.stringify
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
        DataStorePreferences.getInstance().isFirstUse().doOnNext {
            logD("hihi")
        }.subscribe()
    }
}