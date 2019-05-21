package android.bignerdranch.com

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_quiz.*

class QuizActivity : AppCompatActivity() {
	private var mTrueButton: Button? = null
	private var mFalseButton: Button? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_quiz)
		mTrueButton = true_button
		mTrueButton?.setOnClickListener {
			Toast.makeText(this, R.string.correct_toast, Toast.LENGTH_SHORT).show()
		}
		mFalseButton = false_button
		mFalseButton?.setOnClickListener {
			Toast.makeText(this, R.string.incorrect_toast, Toast.LENGTH_SHORT).show()
		}
	}
}
