package android.bignerdranch.com

import android.bignerdranch.com.beatbox.BeatBoxFragment
import androidx.fragment.app.Fragment

class BeatBoxActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        return BeatBoxFragment.newInstance()
    }
}
