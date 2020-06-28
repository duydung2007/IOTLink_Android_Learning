package android.bignerdranch.photogallery

import android.bignerdranch.nerdlauncher.SingleFragmentActivity
import androidx.fragment.app.Fragment

class PhotoGalleryActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        return PhotoGalleryFragment.newInstance()
    }
}
