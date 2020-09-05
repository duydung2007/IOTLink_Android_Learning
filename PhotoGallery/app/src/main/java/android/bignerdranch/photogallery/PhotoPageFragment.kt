package android.bignerdranch.photogallery

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity



class PhotoPageFragment: VisibleFragment() {
    companion object {
        private const val ARG_URI = "photo_page_url"

        fun newInstance(uri: Uri?): PhotoPageFragment {
            var args = Bundle()
            args.putParcelable(ARG_URI, uri)
            var fragment = PhotoPageFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var mUri: Uri? = null
    private var mWebView: WebView? = null
    private var mProgressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mUri = arguments?.getParcelable(ARG_URI)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_photo_page, container, false)

        mProgressBar = v.findViewById(R.id.progress_bar)
        mProgressBar?.max = 100

        mWebView = v.findViewById(R.id.web_view)
        mWebView?.settings?.javaScriptEnabled = true
        mWebView?.webChromeClient = object: WebChromeClient() {
            override fun onProgressChanged(webView: WebView, newProgress: Int) {
                if (newProgress == 100) {
                    mProgressBar?.visibility = View.GONE
                } else {
                    mProgressBar?.visibility = View.VISIBLE
                    mProgressBar?.progress = newProgress
                }
            }

            override fun onReceivedTitle(webView: WebView, title: String) {
                val activity = activity as AppCompatActivity?
                activity?.supportActionBar?.subtitle = title
            }
        }
        mWebView?.loadUrl(mUri.toString())
        return v
    }

}