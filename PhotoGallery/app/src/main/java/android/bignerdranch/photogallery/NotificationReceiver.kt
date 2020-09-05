package android.bignerdranch.photogallery

import android.app.Activity
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat

class NotificationReceiver: BroadcastReceiver() {

    companion object {
        private const val TAG = "NotificationReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(TAG, "received result: $resultCode")
        if (resultCode != Activity.RESULT_OK) {
            // A foreground activity cancelled the broadcast
            return
        }
        val requestCode = intent?.getIntExtra(PollService.REQUEST_CODE, 0)
        val notification = intent?.getParcelableExtra<Notification>(PollService.NOTIFICATION)
        val notificationManager = NotificationManagerCompat.from(context!!)
        notificationManager.notify(requestCode!!, notification!!)
    }
}