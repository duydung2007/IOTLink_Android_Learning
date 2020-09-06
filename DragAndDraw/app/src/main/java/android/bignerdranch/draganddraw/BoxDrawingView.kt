package android.bignerdranch.draganddraw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.max
import kotlin.math.min

class BoxDrawingView: View {
    companion object {
        private const val TAG = "BoxDrawingView"
    }

    private var mCurrentBox: Box? = null
    private var mBoxen: MutableList<Box> = arrayListOf()
    private var mBoxPaint: Paint = Paint()
    private var mBackGroundPaint: Paint = Paint()

    // Used when inflating the view from XML
    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        // Paint the boxes a nice semitransparent red (ARGB)
        mBoxPaint.color = 0x22ff0000

        // Paint the background off-white
        mBackGroundPaint.color = 0xfff8efe
    }

    // Used when creating the view in code
    constructor(context: Context): this(context, null)

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val current = PointF(event?.x ?: -1.0f, event?.y ?: -1.0f)
        var action = ""

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                action = "ACTION_DOWN"
                // Reset drawing state
                mCurrentBox = Box(current)
                mBoxen.add(mCurrentBox!!)
            }
            MotionEvent.ACTION_MOVE -> {
                action = "ACTION_MOVE"
                if (mCurrentBox != null) {
                    mCurrentBox?.setCurrent(current)
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                action = "ACTION_UP"
                mCurrentBox = null
            }
            MotionEvent.ACTION_CANCEL -> {
                action = "ACTION_CANCEL"
                mCurrentBox = null
            }
        }
        Log.i(TAG, "$action at x = ${current.x} y = ${current.y}")
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        // Fill the background
        canvas?.drawPaint(mBackGroundPaint)

        for (box in mBoxen) {
            val left = min(box.getOrigin().x, box.getCurrent().x)
            val right = max(box.getOrigin().x, box.getCurrent().x)
            val top = min(box.getOrigin().y, box.getCurrent().y)
            val bottom = max(box.getOrigin().y, box.getCurrent().y)
            canvas?.drawRect(left, top, right, bottom, mBoxPaint)
        }
    }
}