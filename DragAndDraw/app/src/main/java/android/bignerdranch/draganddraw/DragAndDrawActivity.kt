package android.bignerdranch.draganddraw

import androidx.fragment.app.Fragment

class DragAndDrawActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        return DragAndDrawFragment.newInstance()
    }

}
