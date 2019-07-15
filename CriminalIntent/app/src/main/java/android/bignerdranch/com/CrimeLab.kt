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

    private var mCrimes: ArrayList<Crime>? = null

    private constructor(context: Context) {
        mCrimes = ArrayList()
    }

    fun getCrimes(): List<Crime>? {
        return mCrimes
    }

    fun addCrime(crime: Crime) {
        mCrimes?.add(crime)
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