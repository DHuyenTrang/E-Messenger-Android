package com.example.e_messengerapplication.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

class SafeAreaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        // Lắng nghe khi hệ thống apply WindowInsets (status bar, nav bar, notch)
        ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Áp dụng padding theo vùng an toàn
            view.updatePadding(
                top = systemBarsInsets.top,
                bottom = systemBarsInsets.bottom,
                left = systemBarsInsets.left,
                right = systemBarsInsets.right
            )

            // Trả lại insets đã xử lý để các view con vẫn có thể sử dụng
            insets
        }

        // Kích hoạt lắng nghe insets ngay lập tức
        ViewCompat.requestApplyInsets(this)
    }
}
