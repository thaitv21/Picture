package com.nullexcom.picture

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.renderscript.Allocation
import androidx.renderscript.Element
import androidx.renderscript.RenderScript

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        // Context of the app under test.
        val rs = RenderScript.create(this)
        val bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.parseColor("#f0380e"))
        val `in` = Allocation.createFromBitmap(rs, bitmap)
        val out = Allocation.createTyped(rs, `in`.type)
        val values = FloatArray(32)
        values[0] = 5f;
        val allocationValues = Allocation.createSized(rs, Element.F32(rs), values.size)
        allocationValues.copyFrom(values)
        val script = ScriptC_hsl(rs)
        script.bind_values(allocationValues)
        script.forEach_process(`in`, out)
        val outBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        out.copyTo(outBitmap)
        Log.d("TAG", "useAppContext: ")
    }
}