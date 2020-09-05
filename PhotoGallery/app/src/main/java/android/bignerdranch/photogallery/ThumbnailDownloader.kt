package android.bignerdranch.photogallery

import android.graphics.Bitmap
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.ConcurrentHashMap
import android.os.Message
import android.graphics.BitmapFactory
import java.io.IOException


class ThumbnailDownloader<T> : HandlerThread {
    companion object {
        private const val TAG = "ThumbnailDownloader"
        private const val MESSAGE_DOWNLOAD = 0
    }

    private var mHasQuit: Boolean = false
    private lateinit var mRequestHandler: Handler
    private var mRequestMap: ConcurrentMap<T, String> = ConcurrentHashMap<T, String>()
    private var mResponseHandler: Handler
    private lateinit var mThumbnailDownloadListener: ThumbnailDownloadListener<T>

    interface ThumbnailDownloadListener<T> {
        fun onThumbnailDownloaded(target: T, thumbnail: Bitmap)
    }

    fun setThumbnailDownloadListener(listener: ThumbnailDownloadListener<T>) {
        mThumbnailDownloadListener = listener
    }

    constructor(responseHandler: Handler): super(TAG) {
        mResponseHandler = responseHandler
    }

    override fun onLooperPrepared() {
        mRequestHandler = object : Handler() {
            override fun handleMessage(msg: Message) {
                if (msg.what === MESSAGE_DOWNLOAD) {
                    val target = msg.obj as T
                    Log.i(TAG, "Got a request for URL: " + mRequestMap[target])
                    handleRequest(target)
                }
            }
        }
    }

    override fun quit(): Boolean {
        mHasQuit = true
        return super.quit()
    }

    fun queueThumbnail(target: T, url: String?) {
        Log.i(TAG, "Got a URL: $url")
        if (url == null) {
            mRequestMap.remove(target)
        }
        else {
            mRequestMap[target] = url
            mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD, target).sendToTarget()
        }
    }

    fun clearQueue() {
        mResponseHandler.removeMessages(MESSAGE_DOWNLOAD)
        mRequestMap.clear()
    }

    private fun handleRequest(target: T) {
        try {
            val url = mRequestMap[target] ?: return
            val bitmapBytes = FlickrFetchr().getUrlBytes(url)
            val bitmap = BitmapFactory
                .decodeByteArray(bitmapBytes, 0, bitmapBytes.size)
            Log.i(TAG, "Bitmap created")
            mResponseHandler.post(object: Runnable {
                override fun run() {
                    if (mRequestMap[target] != url || mHasQuit) {
                        return
                    }
                    mRequestMap.remove(target)
                    mThumbnailDownloadListener.onThumbnailDownloaded(target, bitmap)
                }

            })
        } catch (ioe: IOException) {
            Log.e(TAG, "Error downloading image", ioe)
        }
    }
}