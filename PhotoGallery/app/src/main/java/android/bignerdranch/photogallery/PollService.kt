package android.bignerdranch.photogallery

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.concurrent.TimeUnit

class PollService: IntentService(TAG) {

    companion object {
        private const val TAG = "PollService"

        private val POLL_INTERVAL_MS = TimeUnit.MINUTES.toMillis(15L)

        private const val CHANNEL_DEFAULT = "CHANNEL_DEFAULT"

        fun newIntent(context: Context): Intent {
            return Intent(context, PollService::class.java)
        }

        fun setServiceAlarm(context: Context, isOn: Boolean) {
            val i = newIntent(context)
            val pi = PendingIntent.getService(context, 0, i, 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (isOn) {
                alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), POLL_INTERVAL_MS, pi)
            }
            else {
                alarmManager.cancel(pi)
                pi.cancel()
            }
        }

        fun isServiceAlarmOn(context: Context): Boolean {
            val i = newIntent(context)
            val pi = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_NO_CREATE)
            return pi != null
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        if (!isNetworkAvailableAndConnected()) {
            return
        }
        val query = QueryPreferences.getStoredQuery(this)
        val lastResultId = QueryPreferences.getLastResultId(this)
        var items = if (query == null) {
            FlickrFetchr().fetchRecentPhotos()
        } else {
            FlickrFetchr().searchPhotos(query)
        }
        if (items.size == 0) {
            return
        }
        val resultId = items[0].getId()
        if (resultId == lastResultId) {
            Log.i(TAG, "Got an old result $resultId")
        }
        else {
            Log.i(TAG, "Got a new result $resultId")
            val i = PhotoGalleryActivity.newIntent(this)
            val pi = PendingIntent.getActivity(this, 0, i, 0)
            val notification = NotificationCompat.Builder(this, CHANNEL_DEFAULT)
                .setTicker(resources.getString(R.string.new_pictures_title))
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle(resources.getString(R.string.new_pictures_title))
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build()

            val notificationManager = NotificationManagerCompat.from(this)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Create the NotificationChannel
                val mChannel = NotificationChannel(CHANNEL_DEFAULT, "Main Notification", NotificationManager.IMPORTANCE_HIGH)
                mChannel.description = "Notification all important messenger"
                mChannel.enableLights(true)
                mChannel.lightColor = Color.RED
                mChannel.enableVibration(true)
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                notificationManager.createNotificationChannel(mChannel)
            }
            notificationManager.notify(0, notification)
        }
        QueryPreferences.setLastResultId(this, resultId)
    }

    private fun isNetworkAvailableAndConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val isNetworkAvailable = cm?.activeNetworkInfo != null
        return isNetworkAvailable && cm?.activeNetworkInfo?.isConnected()!!
    }
}