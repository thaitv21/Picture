package com.nullexcom.picture

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
        // Context of the app under test.
//        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        Bitmap bitmap = Glide.with(context).load("")
        val matrix = emptyMatrix(16)
        val str = matrix.stringify()
        assertTrue(str, str.isEmpty())
    }
}