package com.nullexcom.picture

import android.graphics.Bitmap
import androidx.renderscript.RenderScript
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

object AppState {

    lateinit var application: MainApplication
    val rs: RenderScript by lazy { RenderScript.create(application.applicationContext) }
    private val originalBitmap = BehaviorSubject.create<Bitmap>()
    private val pageState = BehaviorSubject.createDefault<Int>(Pages.PAGE_ADJUST)

    fun getOriginalBitmap(): BehaviorSubject<Bitmap> {
        return originalBitmap
    }

    fun setOriginalBitmap(bitmap: Bitmap) {
        originalBitmap.onNext(bitmap)
    }

    fun nextPage() {
        val next = when (pageState.value) {
            Pages.PAGE_ADJUST -> Pages.PAGE_FILTER
            Pages.PAGE_FILTER -> Pages.PAGE_HISTOGRAM
            Pages.PAGE_HISTOGRAM -> Pages.PAGE_BLUR
            Pages.PAGE_BLUR -> Pages.PAGE_MORE
            else -> Pages.PAGE_UNKNOWN
        }
        if (next != Pages.PAGE_UNKNOWN) pageState.onNext(next)
    }

    fun nextToPage(page: Int) {
        pageState.onNext(page)
    }

    fun previousPage() {
        val page = when (pageState.value) {
            Pages.PAGE_ADJUST -> Pages.PAGE_DISMISS
            Pages.PAGE_FILTER -> Pages.PAGE_ADJUST
            Pages.PAGE_HISTOGRAM -> Pages.PAGE_FILTER
            Pages.PAGE_BLUR -> Pages.PAGE_HISTOGRAM
            Pages.PAGE_MORE -> Pages.PAGE_BLUR
            else -> Pages.PAGE_DISMISS
        }
        pageState.onNext(page)
    }

    fun getPageState(): Observable<Int> = pageState

}