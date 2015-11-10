package com.mvcoding.financius.extension

import android.graphics.Outline
import android.support.design.widget.Snackbar
import android.view.View
import android.view.ViewOutlineProvider
import com.memoizrlabs.Shank
import kotlin.reflect.KClass

fun <T : Any> View.provideActivityScopedSingleton(cls: KClass<T>): T? {
    if (isInEditMode) {
        return null
    }

    val activity = context.toActivity()
    return Shank.withBoundScope(activity.javaClass, activity.observeFinish().map { Any() }).provide(cls.java)
}

fun View.makeOutlineProviderOval() {
    if (supportsLollipop()) {
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setOval(0, 0, view.width, view.height)
            }
        }
    }
}

fun View.showSnackbar(resId: Int, duration: Int) {
    Snackbar.make(this, resId, duration).show()
}