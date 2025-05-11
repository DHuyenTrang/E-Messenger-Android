package com.example.e_messengerapplication.ui

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentActivity

fun View.applyInsetsRespectingBottomNav(activity: FragmentActivity, bottomNavId: Int) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val systemBarInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())

        val hasBottomNav = activity.findViewById<View?>(bottomNavId) != null

        if (!hasBottomNav) {
            v.setPadding(
                systemBarInsets.left,
                systemBarInsets.top,
                systemBarInsets.right,
                maxOf(systemBarInsets.bottom, imeInsets.bottom)
            )
        } else {
            v.setPadding(0, 0, 0, 0)
        }

        insets
    }
}
