package android.bignerdranch.com

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_quiz.*

class QuizActivity : AppCompatActivity() {
	private var mTrueButton: Button? = null
	private var mFalseButton: Button? = null
	private var mPreviousButton: ImageButton? = null
	private var mNextButton: ImageButton? = null
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

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_quiz)
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
			updateQuestion()
		}
		mNextButton = findViewById(R.id.next_button)
		mNextButton?.setOnClickListener {
			mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.size
			updateQuestion()
		}
		updateQuestion()
	}

	private fun updateQuestion() {
		val question = mQuestionBank[mCurrentIndex].getTextResId()
		mQuestionTextView?.setText(question ?: 0)
	}

	private fun checkAnswer(userPressedTrue: Boolean) {
		val answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue()
		var messageResId: Int
		if (userPressedTrue == answerIsTrue) {
			messageResId = R.string.correct_toast
		}
		else {
			messageResId = R.string.incorrect_toast
		}
		Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
	}
}
