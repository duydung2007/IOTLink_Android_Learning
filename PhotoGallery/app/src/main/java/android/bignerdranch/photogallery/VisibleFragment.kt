package android.bignerdranch.photogallery

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import androidx.fragment.app.Fragment
import android.content.Intent
import android.util.Log

abstract class VisibleFragment: Fragment() {
    companion object {
        private const val TAG = "VisibleFragment"
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(PollService.ACTION_SHOW_NOTIFICATION)
        activity?.registerReceiver(mOnShowNotification, filter, PollService.PERM_PRIVATE, null)
    }

    override fun onStop() {
        super.onStop()
        activity?.unregisterReceiver(mOnShowNotification)
    }

    private val mOnShowNotification = object: BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // If we receive this, we're visible, so cancel
            // the notification
            Log.i(TAG, "canceling notification")
            resultCode = Activity.RESULT_CANCELED
        }
    }
}