package com.example.e_messengerapplication.ui.widget

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout

class SafeAreaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        applyInsets()
    }

    private fun applyInsets() {
        val safeAre = getSafeArea(context as Activity)

        setPadding(safeAre.left, safeAre.top, safeAre.right, safeAre.bottom)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        applyInsets()
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    private fun getSafeArea(activity: Activity): Rect {
        val safeInsetRect = Rect()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            return safeInsetRect
        }
        val windowInsets = activity.window.decorView.rootWindowInsets ?: return safeInsetRect
        val displayCutout = windowInsets.displayCutout
        if (displayCutout != null) {
            safeInsetRect[displayCutout.safeInsetLeft, displayCutout.safeInsetTop, displayCutout.safeInsetRight] =
                displayCutout.safeInsetBottom
        }

        val statusBarHeight = windowInsets.systemWindowInsetTop
        if (safeInsetRect.top == 0) {
            safeInsetRect.top = statusBarHeight
        }
        return safeInsetRect
    }
}