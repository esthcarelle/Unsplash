package com.mine.myapplication.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import com.mine.myapplication.R


class FrameUtil {
    companion object {

//        fun blurImageUtil(context: Context, bitmap: Bitmap, blurRadio: Float): Bitmap {
//            val inputAllocation = Allocation.createFromBitmap(renderScript, bitmap)
//            val outputBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
//            val outputAllocation = Allocation.createFromBitmap(renderScript, outputBitmap)
//
//            scriptBlur.apply {
//                setRadius(blurRadius)
//                setInput(inputAllocation)
//                forEach(outputAllocation)
//            }
//
//            outputAllocation.copyTo(outputBitmap)
//            inputAllocation.destroy()
//            outputAllocation.destroy()
//
//            return outputBitmap
//        }

        fun painterToFrame(imageState: String): Int {
            var painter: Int = when (imageState) {
                Constants.BLACK_FRAME -> R.drawable.black_frame
                Constants.DARK_FRAME -> R.drawable.dark_wood_frame
                Constants.GOLD_FRAME -> R.drawable.gold_frame
                Constants.LIGHT_FRAME -> R.drawable.light_wood_frame
                else -> R.drawable.ic_launcher_background
            }
            return painter
        }

        fun overlayBitmapToCenter(bitmap1: Bitmap, bitmap2: Bitmap): Bitmap? {
            val bitmap1Width = bitmap1.width
            val bitmap1Height = bitmap1.height
            val bitmap2Width = bitmap2.width
            val bitmap2Height = bitmap2.height
            val marginLeft = (bitmap1Width * 0.5 - bitmap2Width * 0.5).toFloat()
            val marginTop = (bitmap1Height * 0.5 - bitmap2Height * 0.5).toFloat()
            val overlayBitmap = Bitmap.createBitmap(bitmap1Width, bitmap1Height, bitmap1.config)
            val canvas = Canvas(overlayBitmap)
            canvas.drawBitmap(bitmap1, Matrix(), null)
            canvas.drawBitmap(bitmap2, marginLeft, marginTop, null)
            return overlayBitmap
        }
    }
}