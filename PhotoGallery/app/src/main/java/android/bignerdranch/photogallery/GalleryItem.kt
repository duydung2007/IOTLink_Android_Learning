package android.bignerdranch.photogallery

import android.net.Uri

class GalleryItem {
    private var mCaption: String? = null
    private var mId: String? = null
    private var mUrl: String? = null
    private var mOwner: String? = null

    override fun toString(): String {
        return mCaption ?: ""
    }

    fun getCaption(): String? {
        return mCaption
    }

    fun setCaption(caption: String?) {
        mCaption = caption
    }

    fun getId(): String? {
        return mId
    }

    fun setId(id: String?) {
        mId = id
    }

    fun getUrl(): String? {
        return mUrl
    }

    fun setUrl(url: String?) {
        mUrl = url
    }

    fun getOwner(): String? {
        return mOwner
    }

    fun setOwner(owner: String?) {
        mOwner = owner
    }

    fun getPhotoPageUri(): Uri? {
        return Uri.parse("https://www.flickr.com/photos/")
            .buildUpon()
            .appendPath(mOwner)
            .appendPath(mId)
            .build()
    }
}