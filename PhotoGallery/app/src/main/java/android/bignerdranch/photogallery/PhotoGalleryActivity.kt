package android.bignerdranch.photogallery

import androidx.fragment.app.Fragment

class PhotoGalleryActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        return PhotoGalleryFragment.newInstance()
    }
}
