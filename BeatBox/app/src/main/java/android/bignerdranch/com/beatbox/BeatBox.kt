package android.bignerdranch.com.beatbox

import android.bignerdranch.com.Sound
import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import java.io.IOException

class BeatBox(context: Context?) {
    companion object {
        private const val TAG = "BeatBox"
        private const val SOUNDS_FOLDER = "sample_sounds"
    }

    private var mAssets: AssetManager? = context?.assets
    private val mSounds: MutableList<Sound> = ArrayList()

    init {
        loadSounds()
    }

    private fun loadSounds() {
        val soundNames: Array<String>?
        try {
            soundNames = mAssets?.list(SOUNDS_FOLDER)
            Log.i(TAG, "Found " + soundNames!!.size + " sounds")
        } catch (ioe: IOException) {
            Log.e(TAG, "Could not list assets", ioe)
            return
        }

        for (filename in soundNames) {
            val assetPath = "$SOUNDS_FOLDER/$filename"
            val sound = Sound(assetPath)
            mSounds.add(sound)
        }
    }

    fun getSounds(): MutableList<Sound> {
        return mSounds
    }
}