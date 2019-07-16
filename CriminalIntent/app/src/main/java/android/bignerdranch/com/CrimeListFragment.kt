package android.bignerdranch.com

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ImageView
import android.widget.TextView

class CrimeListFragment : Fragment() {
    companion object {
        private const val SAVED_SUBTITLE_VISIBLE = "subtitle"
    }
    private var mCrimeRecyclerView: RecyclerView? = null
    private var mAdapter: CrimeAdapter? = null
    private var mSubtitleVisible: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)
        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView
        mCrimeRecyclerView?.layoutManager = LinearLayoutManager(activity)
        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE)
        }
        updateUI()
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.fragment_crime_list, menu)

        val subtitleItem = menu?.findItem(R.id.show_subtitle)
        if (mSubtitleVisible) {
            subtitleItem?.setTitle(R.string.hide_subtitle)
        }
        else {
            subtitleItem?.setTitle(R.string.show_subtitle)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.new_crime -> {
                val crime = Crime()
                CrimeLab.get(context!!)?.addCrime(crime)
                val intent = CrimePagerActivity.newIntent(context!!, crime.getId()!!)
                startActivity(intent)
                true
            }
            R.id.show_subtitle -> {
                mSubtitleVisible = !mSubtitleVisible
                println(mSubtitleVisible)
                activity?.invalidateOptionsMenu()
                updateSubtitle()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun updateSubtitle() {
        val crimeLab = CrimeLab.get(activity as Context)
        val crimeCount = crimeLab?.getCrimes()?.size
        var subtitle: String? = getString(R.string.subtitle_format, crimeCount)

        if (!mSubtitleVisible) {
            subtitle = null
        }

        val activity = activity as AppCompatActivity
        activity.supportActionBar?.subtitle = subtitle
    }

    private fun updateUI() {
        val crimeLab = CrimeLab.get(activity as Context)
        val crimes = crimeLab?.getCrimes()
        if (mAdapter == null) {
            mAdapter = CrimeAdapter(crimes)
            mCrimeRecyclerView?.adapter = mAdapter
        }
        else {
            mAdapter?.setCrimes(crimes)
            mAdapter?.notifyDataSetChanged()
        }
        updateSubtitle()
    }

    private inner class CrimeHolder(inflater: LayoutInflater, parent: ViewGroup)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item_crime, parent, false)), View.OnClickListener {
        private var mTitleTextView: TextView? = null
        private var mDateTextView: TextView? = null
        private var mSolvedImageView: ImageView? = null
        private var mCrime: Crime? = null
        init {
            mTitleTextView = itemView.findViewById(R.id.crime_title) as TextView
            mDateTextView = itemView.findViewById(R.id.crime_date) as TextView
            mSolvedImageView = itemView.findViewById(R.id.crime_solved) as ImageView
            itemView.setOnClickListener(this)
        }

        fun bind(crime: Crime) {
            mCrime = crime
            mTitleTextView?.text = mCrime?.getTitle()
            mDateTextView?.text = mCrime?.getDate().toString()
            mSolvedImageView?.visibility = if (crime.isSolved() == true) View.VISIBLE else View.GONE
        }

        override fun onClick(p0: View?) {
            val intent = CrimePagerActivity.newIntent(activity as Context, mCrime?.getId()!!)
            startActivity(intent)
        }
    }

    private inner class CrimeAdapter(crimes: List<Crime>?) : RecyclerView.Adapter<CrimeHolder>() {

        private var mCrimes: List<Crime>? = null

        init {
            mCrimes = crimes
        }

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CrimeHolder {
            val layoutInflater = LayoutInflater.from(activity)
            return CrimeHolder(layoutInflater, p0)
        }

        override fun getItemCount(): Int {
            return mCrimes?.size ?: 0
        }

        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = mCrimes?.get(position)
            crime?.let {
                holder.bind(crime)
            }
        }

        fun setCrimes(crimes: List<Crime>?) {
            mCrimes = crimes
        }
    }
}