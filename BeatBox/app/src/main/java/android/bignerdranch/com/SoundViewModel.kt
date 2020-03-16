package android.bignerdranch.com

import android.bignerdranch.com.beatbox.BeatBox
import androidx.databinding.BaseObservable

class SoundViewModel(private val mBeatBox: BeatBox): BaseObservable() {
    private var mSound: Sound? = null

    fun getTitle(): String? {
        return mSound?.getName()
    }

    fun getSound(): Sound? {
        return mSound
    }

    fun setSound(sound: Sound) {
        mSound = sound
        notifyChange()
    }
}