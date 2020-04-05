package android.bignerdranch.com.beatbox

import android.bignerdranch.com.Sound
import android.content.Context
import android.content.res.AssetManager
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.util.Log
import java.io.IOException

class BeatBox(context: Context?) {
    companion object {
        private const val TAG = "BeatBox"
        private const val SOUNDS_FOLDER = "sample_sounds"
        private const val MAX_SOUNDS = 5
    }

    private var mAssets: AssetManager? = context?.assets
    private val mSounds: MutableList<Sound> = ArrayList()
    private var mSoundPool: SoundPool

    init {
        mSoundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            SoundPool.Builder().setMaxStreams(MAX_SOUNDS).setAudioAttributes(audioAttributes).build()
        } else {
            SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0)
        }
        loadSounds()
    }

    fun play(sound: Sound) {
        val soundId = sound.getSoundId() ?: return
        mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f)
    }

    fun release() {
        mSoundPool.release()
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
            load(sound)
            mSounds.add(sound)
        }
    }

    @Throws(IOException::class)
    private fun load(sound: Sound) {
        val afd = mAssets?.openFd(sound.getAssetPath())
        val soundId = mSoundPool.load(afd, 1)
        sound.setSoundId(soundId)
    }

    fun getSounds(): MutableList<Sound> {
        return mSounds
    }
}