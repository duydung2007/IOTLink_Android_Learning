package android.bignerdranch.com.database

import android.bignerdranch.com.Crime
import android.database.Cursor
import android.database.CursorWrapper
import java.util.*

class CrimeCursorWrapper(cursor: Cursor?) : CursorWrapper(cursor) {
    fun getCrime(): Crime? {
        val uuidString = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.UUID))
        val title = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.TITLE))
        val date = getLong(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.DATE))
        val isSolved = getInt(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.SOLVED))
        val suspect = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.SUSPECT))

        val crime = Crime(UUID.fromString(uuidString))
        crime.setTitle(title)
        crime.setDate(Date(date))
        crime.setSolved(isSolved != 0)
        crime.setSuspect(suspect)
        return crime
    }
}