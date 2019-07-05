package android.bignerdranch.com

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import java.util.*

class CrimeActivity : SingleFragmentActivity() {

    companion object {
        private const val EXTRA_CRIME_ID: String = "com.bignerdranch.android.criminalintent.crime_id"

        public fun newIntent(packageContext: Context, crimeId: UUID): Intent? {
            val intent = Intent(packageContext, CrimeActivity::class.java)
            intent.putExtra(EXTRA_CRIME_ID, crimeId)
            return intent
        }
    }

    override fun createFragment(): Fragment {
        val crimeId = intent.getSerializableExtra(EXTRA_CRIME_ID) as UUID
        return CrimeFragment.newInstance(crimeId)
    }
}
