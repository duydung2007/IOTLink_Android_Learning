package android.bignerdranch.com

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class PictureUtils {
    companion object {
        fun getScaledBitmap(path: String, destWidth: Int, destHeight: Int): Bitmap {
            // Read in the dimensions of the image on disk
            var options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(path, options)

            val srcWidth = options.outWidth * 1.0f
            val srcHeight = options.outHeight * 1.0f

            // Figure out how much to scale down by
            var inSampleSize: Int = 1
            if (srcHeight > destHeight || srcWidth > destWidth) {
                val heightScale = srcHeight / destHeight
                val widthScale = srcWidth / destWidth
                inSampleSize = if (heightScale > widthScale)
                    heightScale.roundToInt()
                else
                    widthScale.roundToInt()
            }
            options = BitmapFactory.Options()
            options.inSampleSize = inSampleSize

            // Read in and create final bitmap
            return BitmapFactory.decodeFile(path, options)
        }

        fun getScaledBitmap(path: String, activity: Activity): Bitmap {
            val size = Point()
            activity.windowManager.defaultDisplay.getSize(size)
            return getScaledBitmap(path, size.x, size.y)
        }
    }
}