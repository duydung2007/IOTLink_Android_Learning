package android.bignerdranch.photogallery

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.SearchView
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
        setHasOptionsMenu(true)
        updateItems()

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.fragment_photo_gallery, menu)
        val searchItem = menu?.findItem(R.id.menu_item_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(TAG, "QueryTextSubmit: $query")
                QueryPreferences.setStoredQuery(activity as Context, query)
                updateItems()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(TAG, "onQueryTextChange: $newText")
                return false
            }
        })

        searchView.setOnSearchClickListener {
            val query = QueryPreferences.getStoredQuery(activity as Context)
            searchView.setQuery(query,false)
        }

        val toggleItem = menu.findItem(R.id.menu_item_toggle_polling)
        if (PollService.isServiceAlarmOn(requireContext())) {
            toggleItem.setTitle(R.string.stop_polling)
        } else {
            toggleItem.setTitle(R.string.start_polling)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_clear -> {
                QueryPreferences.setStoredQuery(activity as Context, null)
                updateItems()
                true
            }
            R.id.menu_item_toggle_polling -> {
                val shouldStartAlarm = !PollService.isServiceAlarmOn(requireContext())
                PollService.setServiceAlarm(requireContext(), shouldStartAlarm)
                activity?.invalidateOptionsMenu()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateItems() {
        val query = QueryPreferences.getStoredQuery(activity as Context)
        FetchItemsTask(query).execute()
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

    private inner class PhotoAdapter(var mGalleryItems: MutableList<GalleryItem>): RecyclerView.Adapter<PhotoHolder>() {

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

    private inner class FetchItemsTask(var mQuery: String?): AsyncTask<Void, Void, MutableList<GalleryItem>>() {

        override fun doInBackground(vararg p0: Void?): MutableList<GalleryItem> {
            return if (mQuery == null) {
                FlickrFetchr().fetchRecentPhotos()
            } else {
                FlickrFetchr().searchPhotos(mQuery)
            }
        }

        override fun onPostExecute(items: MutableList<GalleryItem>) {
            mItems = items
            setupAdapter()
        }
    }
}