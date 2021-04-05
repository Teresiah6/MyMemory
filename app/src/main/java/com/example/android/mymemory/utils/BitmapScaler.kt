package com.example.android.mymemory.utils

import android.graphics.Bitmap

object BitmapScaler {
    //scale and maintain aspect ratio given a desired width
    //bitmapscaler.scaleToFitWith(bitmpa, 100)
    fun scaleToFitWidth(b:Bitmap, width:Int):Bitmap{
        val factor = width /b.width.toFloat()
        return Bitmap.createScaledBitmap(b, width, (b.height *factor).toInt(), true)

    }
    //do same for height
    fun scaleToFitHeight (b:Bitmap, height:Int):Bitmap {
        val factor = height / b.height.toFloat()
        return Bitmap.createScaledBitmap(b, (b.width * factor).toInt(), height, true)
    }


    }
