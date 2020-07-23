package android.bignerdranch.photogallery

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
    private lateinit var mThumbnailDownloader: ThumbnailDownloader<PhotoHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        FetchItemsTask().execute()

        val responseHandler = Handler()
        mThumbnailDownloader = ThumbnailDownloader(responseHandler)
        mThumbnailDownloader.setThumbnailDownloadListener(object: ThumbnailDownloader.ThumbnailDownloadListener<PhotoHolder> {
            override fun onThumbnailDownloaded(photoHolder: PhotoHolder, bimap: Bitmap) {
                val drawable = BitmapDrawable(resources, bimap)
                photoHolder.bindDrawable(drawable)
            }
        })

        mThumbnailDownloader.start()
        mThumbnailDownloader.looper
        Log.i(TAG, "Background thread started")
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

    override fun onDestroyView() {
        super.onDestroyView()
        mThumbnailDownloader.clearQueue()
    }

    override fun onDestroy() {
        super.onDestroy()
        mThumbnailDownloader.quit()
        Log.i(TAG, "Background thread destroyed")
    }

    private fun setupAdapter() {
        if (isAdded) {
            mPhotoRecyclerView.adapter = PhotoAdapter(mItems)
        }
    }

    private inner class PhotoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var mItemImageView: ImageView? = null

        init {
            mItemImageView = itemView.findViewById(R.id.item_image_view) as ImageView
        }

        fun bindDrawable(drawable: Drawable) {
            mItemImageView?.setImageDrawable(drawable)
        }
    }

    private inner class PhotoAdapter(galleryItems: MutableList<GalleryItem>): RecyclerView.Adapter<PhotoHolder>() {
        private var mGalleryItems: MutableList<GalleryItem> = mutableListOf()

        init {
            mGalleryItems = galleryItems
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
            val inflater = LayoutInflater.from(activity)
            val view = inflater.inflate(R.layout.list_item_gallery, parent, false)
            return PhotoHolder(view)
        }

        override fun getItemCount(): Int {
            return mGalleryItems.size
        }

        override fun onBindViewHolder(photoHolder: PhotoHolder, position: Int) {
            val galleryItem: GalleryItem = mGalleryItems[position]
            val placeholder = resources.getDrawable(R.drawable.bill_up_close)
            photoHolder.bindDrawable(placeholder)
            mThumbnailDownloader.queueThumbnail(photoHolder, galleryItem.getUrl())

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