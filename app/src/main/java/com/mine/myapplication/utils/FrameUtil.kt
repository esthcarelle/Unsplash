package com.mine.myapplication.utils

import android.content.Context
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.mine.myapplication.R
import com.mine.myapplication.components.Constants

class FrameUtil {
    companion object{
        fun painterToFrame(imageState:String): Int {
            var painter: Int = R.drawable.black_frame
            when (imageState) {
                Constants.BLACK_FRAME -> painter = R.drawable.black_frame
                Constants.DARK_FRAME -> painter = R.drawable.dark_wood_frame
                Constants.GOLD_FRAME -> painter = R.drawable.gold_frame
                Constants.LIGHT_FRAME -> painter = R.drawable.light_wood_frame
                else -> painter = R.drawable.ic_launcher_background
            }
            return painter
        }
    }
}