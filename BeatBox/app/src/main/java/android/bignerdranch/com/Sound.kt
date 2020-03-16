package android.bignerdranch.com

class Sound(private val mAssetPath: String) {
    private var mName: String? = null

    init {
        val components = mAssetPath.split("/")
        val filename = components[components.size - 1]
        mName = filename.replace(".wav", "")
    }

    fun getAssetPath(): String {
        return mAssetPath
    }

    fun getName(): String? {
        return mName
    }
}