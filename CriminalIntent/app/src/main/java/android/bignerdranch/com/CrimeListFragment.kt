package android.bignerdranch.com

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class CrimeListFragment : Fragment() {
    private var mCrimeRecyclerView: RecyclerView? = null
    private var mAdapter: CrimeAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)
        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView
        mCrimeRecyclerView?.layoutManager = LinearLayoutManager(activity)
        updateUI()
        return view
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {
        val crimeLab = CrimeLab.get(activity as Context)
        val crimes = crimeLab?.getCrimes()
        if (mAdapter == null) {
            mAdapter = CrimeAdapter(crimes)
            mCrimeRecyclerView?.adapter = mAdapter
        }
        else {
            mAdapter?.notifyDataSetChanged()
        }
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
            val intent = CrimeActivity.newIntent(activity as Context, mCrime?.getId()!!)
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

    }
}