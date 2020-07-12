package android.bignerdranch.photogallery

class GalleryItem {
    private var mCaption: String? = null
    private var mId: String? = null
    private var mUrl: String? = null

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
}