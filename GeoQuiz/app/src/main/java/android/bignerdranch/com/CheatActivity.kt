package android.bignerdranch.com

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.Button
import android.widget.TextView

class CheatActivity : AppCompatActivity() {
    private var mAnswerIsTrue: Boolean? = null
    private var mAnswerTextView: TextView? = null
    private var mShowAnswerButton: Button? = null
    companion object {
        private const val EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true"

        private const val EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown"

        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            val intent = Intent(packageContext, CheatActivity::class.java)
            intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            return intent
        }

        fun wasAnswerShown(result: Intent): Boolean {
            return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        mAnswerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        mAnswerTextView = findViewById(R.id.answer_text_view)

        mShowAnswerButton = findViewById(R.id.show_answer_button)
        mShowAnswerButton?.setOnClickListener {
            if (mAnswerIsTrue!!) {
                mAnswerTextView?.setText(R.string.true_button)
            }
            else {
                mAnswerTextView?.setText(R.string.false_button)
            }
            setAnswerShownResult(true)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val cx = mShowAnswerButton?.width?.div(2) ?: 0
                val cy = mShowAnswerButton?.height?.div(2) ?: 0
                val radius = mShowAnswerButton?.width
                val anim = ViewAnimationUtils.createCircularReveal(mShowAnswerButton, cx, cy, radius!! * 1.0f, 0f)
                anim.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        mShowAnswerButton?.visibility = View.INVISIBLE
                    }
                })
                anim.start()
            }
            else {
                mShowAnswerButton?.visibility = View.INVISIBLE
            }
        }
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent()
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        setResult(Activity.RESULT_OK, data)
    }
}
