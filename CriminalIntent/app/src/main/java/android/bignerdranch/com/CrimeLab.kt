package android.bignerdranch.com

import android.bignerdranch.com.database.CrimeBaseHelper
import android.bignerdranch.com.database.CrimeCursorWrapper
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import java.util.*
import kotlin.collections.ArrayList
import android.bignerdranch.com.database.CrimeDbSchema.CrimeTable
import java.io.File

class CrimeLab {
    companion object {
        private var sCrimeLab: CrimeLab? = null

        fun get(context: Context): CrimeLab? {
            if (sCrimeLab == null) {
                sCrimeLab = CrimeLab(context)
            }
            return sCrimeLab
        }

        fun getContentValues(crime: Crime): ContentValues {
            val values = ContentValues()
            values.put(CrimeTable.Cols.UUID, crime.getId().toString())
            values.put(CrimeTable.Cols.TITLE, crime.getTitle())
            values.put(CrimeTable.Cols.DATE, crime.getDate()?.time)
            values.put(CrimeTable.Cols.SOLVED, if (crime.isSolved()) 1 else 0)
            values.put(CrimeTable.Cols.SUSPECT, crime.getSuspect())
            return values
        }
    }

    private var mContext: Context? = null

    private var mDatabase: SQLiteDatabase? = null

    private constructor(context: Context) {
        mContext = context.applicationContext
        mDatabase = CrimeBaseHelper(mContext!!).writableDatabase
    }

    fun getCrimes(): List<Crime> {
        val crimes: ArrayList<Crime> = ArrayList()
        val cursor = queryCrimes(null, null)
        cursor.use { cursor ->
            cursor?.moveToFirst()
            while (!cursor?.isAfterLast!!) {
                crimes.add(cursor.getCrime()!!)
                cursor?.moveToNext()
            }
        }
        cursor?.close()
        return crimes
    }

    fun addCrime(crime: Crime) {
        val values = getContentValues(crime)
        mDatabase?.insert(CrimeTable.NAME, null, values)
    }

    fun getCrime(id: UUID): Crime? {
        val cursor = queryCrimes(
            CrimeTable.Cols.UUID + " = ?",
            arrayOf(id.toString())
        )
        cursor.use { cursor ->
            if (cursor?.count == 0) {
                return null
            }
            cursor?.moveToFirst()
            val crime = cursor?.getCrime()
            cursor?.close()
            return crime
        }
    }

    fun getPhotoFile(crime: Crime): File {
        val filesDir = mContext?.filesDir
        return File(filesDir, crime.getPhotoFilename())
    }

    fun updateCrime(crime: Crime) {
        val uuidString = crime.getId().toString()
        val values = getContentValues(crime)
        mDatabase?.update(
            CrimeTable.NAME, values,
            CrimeTable.Cols.UUID + " = ?",
            arrayOf(uuidString)
        )
    }

    private fun queryCrimes(whereClause: String?, whereArgs: Array<String>?): CrimeCursorWrapper? {
        val cursor = mDatabase?.query(
            CrimeTable.NAME, // having
            null, // columns - null selects all columns
            whereClause,
            whereArgs, null, null, null
        )
        return CrimeCursorWrapper(cursor)
    }
}