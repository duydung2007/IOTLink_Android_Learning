package android.bignerdranch.com

class Question {
    private var mTextResId: Int? = null
    private var mAnswerTrue: Boolean? = null

    constructor(textRestId: Int, answerTrue: Boolean) {
        mTextResId = textRestId
        mAnswerTrue = answerTrue
    }

    fun getTextResId(): Int? {
        return mTextResId
    }

    fun isAnswerTrue(): Boolean? {
        return mAnswerTrue
    }

}