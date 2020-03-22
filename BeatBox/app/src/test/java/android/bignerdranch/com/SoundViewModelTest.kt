package android.bignerdranch.com

import android.bignerdranch.com.beatbox.BeatBox
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is
import org.junit.Before
import org.junit.Test

import org.mockito.Mockito.*

class SoundViewModelTest {

    private lateinit var mBeatBox: BeatBox
    private lateinit var mSound: Sound
    private lateinit var mSubject: SoundViewModel

    @Before
    fun setUp() {
        mBeatBox = mock(BeatBox::class.java)
        mSound = Sound("assetPath")
        mSubject = SoundViewModel(mBeatBox)
        mSubject.setSound(mSound)
    }

    @Test
    fun exposesSoundNameAsTitle() {
        assertThat(mSubject.getTitle(), Is.`is`(mSound.getName()))
    }

    @Test
    fun callsBeatBoxPlayOnButtonClicked() {
        mSubject.onButtonClicked()
        verify(mBeatBox).play(mSound)
    }
}