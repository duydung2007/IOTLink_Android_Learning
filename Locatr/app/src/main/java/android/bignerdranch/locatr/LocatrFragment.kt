package android.bignerdranch.locatr

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_locatr.*
import java.io.IOException

class LocatrFragment: Fragment() {
    companion object {
        private const val TAG = "LocatrFragment"

        private val LOCATION_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        private const val REQUEST_LOCATION_PERMISSIONS = 0

        fun newInstance(): LocatrFragment {
            return LocatrFragment()
        }
    }

    private var mClient: GoogleApiClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        mClient = GoogleApiClient.Builder(activity!!)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(object: GoogleApiClient.ConnectionCallbacks {
                override fun onConnected(p0: Bundle?) {
                    activity?.invalidateOptionsMenu()
                }

                override fun onConnectionSuspended(p0: Int) {

                }

            })
            .build()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_locatr, container, false)
    }

    override fun onStart() {
        super.onStart()
        activity?.invalidateOptionsMenu()
        mClient?.connect()
    }

    override fun onStop() {
        super.onStop()
        mClient?.disconnect()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_locatr, menu)
        val searchItem = menu.findItem(R.id.action_locate)
        searchItem.isEnabled = mClient?.isConnected ?: false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_locate -> {
                if (hasLocationPermission()) {
                    findImage()
                }
                else {
                    requestPermissions(LOCATION_PERMISSIONS, REQUEST_LOCATION_PERMISSIONS)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_LOCATION_PERMISSIONS -> {
                if (hasLocationPermission()) {
                    findImage()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun findImage() {
        val request = LocationRequest.create()
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        request.numUpdates = 1
        request.interval = 0
        LocationServices.FusedLocationApi.requestLocationUpdates(mClient, request, object: LocationListener {
            override fun onLocationChanged(location: Location?) {
                Log.i(TAG, "Got a fix: $location")
                SearchTask().execute(location)
            }
        })
    }

    private fun hasLocationPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(requireContext(), LOCATION_PERMISSIONS[0])
        return result == PackageManager.PERMISSION_GRANTED
    }

    private inner class SearchTask: AsyncTask<Location?, Void?, Void?>() {
        private var mGalleryItem: GalleryItem? = null
        private var mBitmap: Bitmap? = null

        override fun doInBackground(vararg params: Location?): Void? {
            val fetchr = FlickrFetchr()
            val items = params[0]?.let { fetchr.searchPhotos(it) }
            if (items?.size == 0) {
                return null
            }
            mGalleryItem = items?.get(0)
            try {
                val bytes = fetchr.getUrlBytes(mGalleryItem?.getUrl() ?: "")
                mBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            }
            catch (ioe: IOException) {
                Log.i(TAG, "Unable to download bitmap", ioe)
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            image.setImageBitmap(mBitmap)
        }
    }
}