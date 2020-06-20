package android.bignerdranch.nerdlauncher

import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import java.util.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NerdLauncherFragment : Fragment() {
    companion object {
        fun newInstance(): NerdLauncherFragment {
            return NerdLauncherFragment()
        }
    }

    private val TAG = "NerdLauncherFragment"
    private lateinit var mRecyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_nerd_launcher, container, false)
        mRecyclerView = v.findViewById(R.id.app_recycler_view)
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        setupAdapter()
        return v
    }

    private fun setupAdapter() {
        val startupIntent = Intent(Intent.ACTION_MAIN)
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        val pm = activity?.packageManager
        val activities = pm?.queryIntentActivities(startupIntent, 0) ?: return

        activities?.sortWith(Comparator { a, b ->
            val pm = activity!!.packageManager
            String.CASE_INSENSITIVE_ORDER.compare(
                a.loadLabel(pm).toString(),
                b.loadLabel(pm).toString()
            )
        })
        Log.i(TAG, "Found ${activities?.size} activities.")
        mRecyclerView.adapter = ActivityAdapter(activities)
    }

    private inner class ActivityHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private var mResolveInfo: ResolveInfo? = null
        private val mNameTextView: TextView = itemView as TextView

        fun bindActivity(resolveInfo: ResolveInfo) {
            mResolveInfo = resolveInfo
            val pm = activity!!.packageManager
            val appName = mResolveInfo!!.loadLabel(pm).toString()
            mNameTextView.text = appName
            mNameTextView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val activityInfo = mResolveInfo?.activityInfo
            var i = Intent(Intent.ACTION_MAIN)
                .setClassName(activityInfo?.applicationInfo?.packageName.toString(), activityInfo?.name.toString())
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
        }
    }

    private inner class ActivityAdapter(private val mActivities: List<ResolveInfo>) : RecyclerView.Adapter<ActivityHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityHolder {
            val layoutInflater = LayoutInflater.from(activity)
            val view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false)
            return ActivityHolder(view)
        }

        override fun onBindViewHolder(holder: ActivityHolder, position: Int) {
            val resolveInfo = mActivities[position]
            holder.bindActivity(resolveInfo)
        }

        override fun getItemCount(): Int {
            return mActivities.size
        }
    }

}