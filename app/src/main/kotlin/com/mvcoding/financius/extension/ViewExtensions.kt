package com.mvcoding.financius.extension

import android.content.Context
import android.graphics.Outline
import android.support.design.widget.Snackbar
import android.view.View
import android.view.ViewOutlineProvider
import com.memoizrlabs.Shank
import kotlin.reflect.KClass

fun <T : Any> View.shankWithBoundScope(cls: KClass<T>, context: Context): Shank.ScopedCache? {
    if (isInEditMode) {
        return null
    }

    return Shank.withBoundScope(cls.javaClass, context.toActivity().observeFinish().map { Any() })
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