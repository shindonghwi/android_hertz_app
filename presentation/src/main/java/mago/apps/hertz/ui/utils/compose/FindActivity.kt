package mago.apps.hertz.ui.utils.compose

import android.content.Context
import android.content.ContextWrapper
import mago.apps.hertz.MainActivity

fun Context.findMainActivity(): MainActivity {
    var context = this
    while (context is ContextWrapper) {
        if (context is MainActivity) return context
        context = context.baseContext
    }
    throw IllegalStateException("no activity")
}