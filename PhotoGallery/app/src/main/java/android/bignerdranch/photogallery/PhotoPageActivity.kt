package android.bignerdranch.photogallery

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment

class PhotoPageActivity: SingleFragmentActivity() {
    companion object {
        fun newIntent(context: Context?, photoPageUri: Uri?): Intent {
            var i = Intent(context, PhotoPageActivity::class.java)
            i.data = photoPageUri
            return i
        }
    }

    override fun createFragment(): Fragment {
        return PhotoPageFragment.newInstance(intent.data)
    }

}