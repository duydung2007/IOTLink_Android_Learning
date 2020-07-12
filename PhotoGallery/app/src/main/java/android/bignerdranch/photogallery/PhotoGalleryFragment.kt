package android.bignerdranch.photogallery

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PhotoGalleryFragment: Fragment() {

    companion object {
        private const val TAG = "PhotoGalleryFragment"

        fun newInstance(): PhotoGalleryFragment {
            return PhotoGalleryFragment()
        }
    }

    private lateinit var mPhotoRecyclerView: RecyclerView
    private var mItems: MutableList<GalleryItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        FetchItemsTask().execute()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_photo_gallery, container, false)
        mPhotoRecyclerView = view.findViewById(R.id.photo_recycler_view)
        mPhotoRecyclerView.layoutManager = GridLayoutManager(context, 3)
        setupAdapter()
        return view
    }

    private fun setupAdapter() {
        if (isAdded) {
            mPhotoRecyclerView.adapter = PhotoAdapter(mItems)
        }
    }

    private inner class PhotoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var mTitleTextView: TextView? = null

        init {
            mTitleTextView = itemView as TextView
        }

        fun bindGalleryItem(item: GalleryItem) {
            mTitleTextView?.text = item.toString()
        }
    }

    private inner class PhotoAdapter(galleryItems: MutableList<GalleryItem>): RecyclerView.Adapter<PhotoHolder>() {
        private var mGalleryItems: MutableList<GalleryItem> = mutableListOf()

        init {
            mGalleryItems = galleryItems
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
            val textView = TextView(activity)
            return PhotoHolder(textView)
        }

        override fun getItemCount(): Int {
            return mGalleryItems.size
        }

        override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
            val galleryItem: GalleryItem = mGalleryItems[position]
            holder.bindGalleryItem(galleryItem)
        }
    }

    private inner class FetchItemsTask: AsyncTask<Void, Void, MutableList<GalleryItem>>() {
        override fun doInBackground(vararg p0: Void?): MutableList<GalleryItem> {
            return FlickrFetchr().fetchItems()
        }

        override fun onPostExecute(items: MutableList<GalleryItem>) {
            mItems = items
            setupAdapter()
        }
    }
}