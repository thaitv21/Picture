package com.nullexcom.picture.ext

import android.animation.Animator
import android.view.View
import android.view.ViewPropertyAnimator

fun ViewPropertyAnimator.addEndListener(listener: () -> Unit) : ViewPropertyAnimator {
    setListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
            listener.invoke()
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationStart(animation: Animator?) {
        }
    })
    return this
}