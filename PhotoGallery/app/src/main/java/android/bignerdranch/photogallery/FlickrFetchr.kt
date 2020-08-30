package android.bignerdranch.photogallery

import android.net.Uri
import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class FlickrFetchr {
    companion object {
        private const val TAG = "FlickrFetchr"
        private const val API_KEY = "d423c1b709763a452aa58da2bec3cb68"
        private const val FETCH_RECENTS_METHOD = "flickr.photos.getRecent"
        private const val SEARCH_METHOD = "flickr.photos.search"
        private val ENDPOINT = Uri
            .parse("https://api.flickr.com/services/rest/")
            .buildUpon()
            .appendQueryParameter("api_key", API_KEY)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .appendQueryParameter("extras", "url_s")
            .build()
    }

    @Throws(IOException::class)
    fun getUrlBytes(urlSpec: String): ByteArray {
        val url = URL(urlSpec)
        val connection = url.openConnection() as HttpURLConnection
        try {
            val out = ByteArrayOutputStream()
            val inputStream = connection.inputStream
            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                throw IOException(connection.responseMessage + ": with " + urlSpec)
            }
            var buffer = ByteArray(1024)
            var bytesRead = inputStream.read(buffer)
            while (bytesRead > 0) {
                out.write(buffer, 0, bytesRead)
                bytesRead = inputStream.read(buffer)
            }
            out.close()
            return out.toByteArray()
        } finally {
            connection.disconnect()
        }
    }

    @Throws(IOException::class)
    fun getUrlString(urlSpec: String): String {
        return String(getUrlBytes(urlSpec))
    }

    fun fetchRecentPhotos(): MutableList<GalleryItem> {
        val url = buildUrl(FETCH_RECENTS_METHOD, null)
        return downloadGalleryItems(url)
    }

    fun searchPhotos(query: String?): MutableList<GalleryItem> {
        val url = buildUrl(SEARCH_METHOD, query)
        return downloadGalleryItems(url)
    }

    private fun downloadGalleryItems(url: String): MutableList<GalleryItem> {
        var items: MutableList<GalleryItem> = mutableListOf()
        try {
            val jsonString = getUrlString(url)
            Log.i(TAG, "Received JSON: $jsonString")
            val jsonBody = JSONObject(jsonString)
            parseItems(items, jsonBody)
        } catch (ioe: IOException) {
            Log.e(TAG, "Failed to fetch items", ioe)
        } catch (je: JSONException) {
            Log.e(TAG, "Failed to parse JSON", je)
        }

        return items
    }

    private fun buildUrl(method: String, query: String?): String {
        var uriBuilder = ENDPOINT.buildUpon()
            .appendQueryParameter("method", method)
        if (method == SEARCH_METHOD) {
            uriBuilder.appendQueryParameter("text", query)
        }
        return uriBuilder.build().toString()
    }

    @Throws(IOException::class, JSONException::class)
    private fun parseItems(items: MutableList<GalleryItem>, jsonBody: JSONObject) {
        val photosJsonObject: JSONObject = jsonBody.getJSONObject("photos")
        val photoJsonArray: JSONArray = photosJsonObject.getJSONArray("photo")

        for (i in 0 until photoJsonArray.length()) {
            val photoJsonObject: JSONObject = photoJsonArray.getJSONObject(i)

            val item = GalleryItem()
            item.setId(photoJsonObject.getString("id"))
            item.setCaption(photoJsonObject.getString("title"))

            if (!photoJsonObject.has("url_s")) {
                continue
            }

            item.setUrl(photoJsonObject.getString("url_s"))
            item.setOwner(photoJsonObject.getString("owner"))
            items.add(item)
        }
    }
}