package android.bignerdranch.nerdlauncher

import androidx.fragment.app.Fragment

class NerdLauncherActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        return NerdLauncherFragment.newInstance()
    }
}
