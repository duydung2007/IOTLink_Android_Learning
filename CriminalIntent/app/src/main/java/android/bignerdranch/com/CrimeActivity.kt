package android.bignerdranch.com

import android.support.v4.app.Fragment

class CrimeActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        return CrimeFragment()
    }
}
