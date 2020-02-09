package android.bignerdranch.com

import android.support.v4.app.Fragment
import android.widget.FrameLayout


class CrimeListActivity : SingleFragmentActivity(), CrimeListFragment.Callbacks, CrimeFragment.Callbacks {

    override fun createFragment(): Fragment {
        return CrimeListFragment()
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_masterdetail
    }

    override fun onCrimeSelected(crime: Crime) {
        if (findViewById<FrameLayout>(R.id.detail_fragment_container) == null) {
            val intent = CrimePagerActivity.newIntent(this, crime.getId()!!)
            startActivity(intent)
        }
        else {
            val newDetail = CrimeFragment.newInstance(crime.getId()!!)
            supportFragmentManager.beginTransaction()
                .replace(R.id.detail_fragment_container, newDetail)
                .commit()
        }
    }

    override fun onCrimeUpdated(crime: Crime) {
        val listFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as CrimeListFragment
        listFragment.updateUI()
    }
}