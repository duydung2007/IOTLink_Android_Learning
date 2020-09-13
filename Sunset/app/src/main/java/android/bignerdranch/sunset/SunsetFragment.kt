package android.bignerdranch.sunset

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_sunset.*

class SunsetFragment: Fragment() {

    companion object {
        fun newInstance(): SunsetFragment {
            return SunsetFragment()
        }
    }

    private lateinit var mScreenView: View
    private var mBlueSkyColor: Int = 0
    private var mSunsetSkyColor: Int = 0
    private var mNightSkyColor: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sunset, container, false)
        mScreenView = view
        mBlueSkyColor = resources?.getColor(R.color.blue_sky)
        mSunsetSkyColor = resources?.getColor(R.color.sunset_sky)
        mNightSkyColor = resources?.getColor(R.color.night_sky)
        mScreenView.setOnClickListener { startAnimation() }
        return view
    }

    private fun startAnimation() {
        val sunYStart = sun.top * 1.0f
        val sunYEnd = sky.height * 1.0f
        val heightAnimator = ObjectAnimator.ofFloat(sun, "y", sunYStart, sunYEnd).setDuration(3000L)
        heightAnimator.interpolator = AccelerateInterpolator()

        val sunsetSkyAnimator = ObjectAnimator.ofInt(sky, "backgroundColor", mBlueSkyColor, mSunsetSkyColor).setDuration(3000L)
        sunsetSkyAnimator.setEvaluator(ArgbEvaluator())

        val nightSkyAnimator = ObjectAnimator.ofInt(sky, "backgroundColor", mSunsetSkyColor, mNightSkyColor).setDuration(1500L)
        nightSkyAnimator.setEvaluator(ArgbEvaluator())

        var animatorSet = AnimatorSet()
        animatorSet.play(heightAnimator).with(sunsetSkyAnimator).before(nightSkyAnimator)
        animatorSet.start()
    }
}