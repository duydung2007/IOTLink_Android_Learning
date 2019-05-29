package android.bignerdranch.com

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class QuizActivity : AppCompatActivity() {
	companion object {
		private const val TAG = "QuizActivity"
		private const val KEY_INDEX = "index"
		private const val REQUEST_CODE_CHEAT = 0
	}
	private var mTrueButton: Button? = null
	private var mFalseButton: Button? = null
	private var mPreviousButton: ImageButton? = null
	private var mNextButton: ImageButton? = null
	private var mCheatButton: Button? = null
	private var mQuestionTextView: TextView? = null

	private var mQuestionBank: Array<Question> = arrayOf(
		Question(R.string.question_australia, true),
		Question(R.string.question_oceans, true),
		Question(R.string.question_mideast, false),
		Question(R.string.question_africa, false),
		Question(R.string.question_americas, true),
		Question(R.string.question_asia, true)
	)

	private var mCurrentIndex: Int = 0
	private var mIsCheater: Boolean = false

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		Log.d(TAG, "onCreate(Bundle) called")
		setContentView(R.layout.activity_quiz)
		if (savedInstanceState != null) {
			mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0)
		}
		mQuestionTextView = findViewById(R.id.question_text_view)
		mQuestionTextView?.setOnClickListener {
			mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.size
			updateQuestion()
		}
		mTrueButton = findViewById(R.id.true_button)
		mTrueButton?.setOnClickListener {
			checkAnswer(true)
		}
		mFalseButton = findViewById(R.id.false_button)
		mFalseButton?.setOnClickListener {
			checkAnswer(false)
		}
		mPreviousButton = findViewById(R.id.previous_button)
		mPreviousButton?.setOnClickListener {
			mCurrentIndex = if (mCurrentIndex > 0) mCurrentIndex - 1 else mQuestionBank.size - 1
			mIsCheater = false
			updateQuestion()
		}
		mNextButton = findViewById(R.id.next_button)
		mNextButton?.setOnClickListener {
			mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.size
			mIsCheater = false
			updateQuestion()
		}
		mCheatButton = findViewById(R.id.cheat_button)
		mCheatButton?.setOnClickListener {
			/** Start CheatActivity **/
			val answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue()
			val intent = CheatActivity.newIntent(this, answerIsTrue!!)
			startActivityForResult(intent, REQUEST_CODE_CHEAT)
		}
		updateQuestion()
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (resultCode != Activity.RESULT_OK) {
			return
		}

		if (requestCode == REQUEST_CODE_CHEAT) {
			if (data == null) {
				return
			}
			mIsCheater = CheatActivity.wasAnswerShown(data)
		}
	}

	private fun updateQuestion() {
		val question = mQuestionBank[mCurrentIndex].getTextResId()
		mQuestionTextView?.setText(question ?: 0)
	}

	private fun checkAnswer(userPressedTrue: Boolean) {
		val answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue()
		var messageResId: Int
		if (mIsCheater) {
			messageResId = R.string.judgment_toast
		}
		else {
			if (userPressedTrue == answerIsTrue) {
				messageResId = R.string.correct_toast
			} else {
				messageResId = R.string.incorrect_toast
			}
		}
		Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
	}

	override fun onSaveInstanceState(outState: Bundle?) {
		super.onSaveInstanceState(outState)
		Log.i(TAG, "onSaveInstanceState")
		outState?.putInt(KEY_INDEX, mCurrentIndex)
	}

	override fun onStart() {
		super.onStart()
		Log.d(TAG, "onStart() called")
	}

	override fun onResume() {
		super.onResume()
		Log.d(TAG, "onResume() called")
	}

	override fun onPause() {
		super.onPause()
		Log.d(TAG, "onPause() called")
	}

	override fun onStop() {
		super.onStop()
		Log.d(TAG, "onStop() called")
	}

	override fun onDestroy() {
		super.onDestroy()
		Log.d(TAG, "onDestroy() called")
	}
}
