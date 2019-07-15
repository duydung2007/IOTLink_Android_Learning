package android.bignerdranch.com

import java.util.*

class Crime {
    private var mId: UUID? = null
    private var mTitle: String? = null
    private var mDate: Date? = null
    private var mSolved: Boolean? = null

    init {
        mId = UUID.randomUUID()
        mDate = Date()
    }

    fun getId(): UUID? {
        return mId
    }

    fun getTitle(): String? {
        return mTitle
    }

    fun setTitle(title: String) {
        mTitle = title
    }

    fun getDate(): Date? {
        return mDate
    }

    fun setDate(date: Date) {
        mDate = date
    }

    fun isSolved(): Boolean {
        return mSolved ?: false
    }

    fun setSolved(solved: Boolean) {
        mSolved = solved
    }
}