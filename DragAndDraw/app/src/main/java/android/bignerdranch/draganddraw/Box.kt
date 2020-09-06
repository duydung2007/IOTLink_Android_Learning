package android.bignerdranch.draganddraw

import android.graphics.PointF

class Box(var mOrigin: PointF) {
    private var mCurrent: PointF

    init {
        mCurrent = mOrigin
    }

    fun getCurrent(): PointF {
        return mCurrent
    }

    fun setCurrent(current: PointF) {
        mCurrent = current
    }

    fun getOrigin(): PointF {
        return mOrigin
    }
}