package android.bignerdranch.com

import android.content.Context
import java.util.*
import kotlin.collections.ArrayList

class CrimeLab {
    companion object {
        private var sCrimeLab: CrimeLab? = null

        fun get(context: Context): CrimeLab? {
            if (sCrimeLab == null) {
                sCrimeLab = CrimeLab(context)
            }
            return sCrimeLab
        }
    }

    private var mCrimes: List<Crime>? = null

    private constructor(context: Context) {
        mCrimes = ArrayList()
        for (i in 0..100) {
            var crime = Crime()
            crime.setTitle("Crime #$i")
            crime.setSolved(i % 2 == 0)
            (mCrimes as ArrayList<Crime>).add(crime)
        }
    }

    fun getCrimes(): List<Crime>? {
        return mCrimes
    }

    fun getCrime(id: UUID): Crime? {
        mCrimes?.forEach {
            if (it.getId()?.equals(id)!!) {
                return it
            }
        }
        return null
    }

}