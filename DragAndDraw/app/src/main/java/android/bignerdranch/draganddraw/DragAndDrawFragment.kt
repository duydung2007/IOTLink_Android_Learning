package android.bignerdranch.draganddraw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class DragAndDrawFragment: Fragment() {
    companion object {
        fun newInstance(): DragAndDrawFragment {
            return DragAndDrawFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_drag_and_draw, container, false)
    }
}