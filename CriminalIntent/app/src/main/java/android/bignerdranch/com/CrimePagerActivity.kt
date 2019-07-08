package android.bignerdranch.com

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_crime_pager.*
import java.util.*


class CrimePagerActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id"

        fun newIntent(packageContext: Context, crimeId: UUID): Intent {
            var intent = Intent(packageContext, CrimePagerActivity::class.java)
            intent.putExtra(EXTRA_CRIME_ID, crimeId)
            return intent
        }
    }

    private var mCrimes: List<Crime>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crime_pager)

        val crimeId = intent.getSerializableExtra(EXTRA_CRIME_ID)

        mCrimes = CrimeLab.get(this)?.getCrimes()
        val fragmentManager = supportFragmentManager
        crime_view_pager.adapter = object : FragmentStatePagerAdapter(fragmentManager) {
            override fun getItem(position: Int): Fragment {
                val crime = mCrimes?.get(position)
                return CrimeFragment.newInstance(crime?.getId()!!)
            }

            override fun getCount(): Int {
                return mCrimes?.size!!
            }
        }

        for (i in 0..mCrimes?.size!!) {
            if (mCrimes?.get(i)?.getId()!! == crimeId) {
                crime_view_pager.currentItem = i
                break
            }
        }
    }

}
