package android.bignerdranch.com

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.support.annotation.LayoutRes

abstract class SingleFragmentActivity: AppCompatActivity() {

    protected abstract fun createFragment(): Fragment

    @LayoutRes
    protected open fun getLayoutResId(): Int {
        return R.layout.activity_fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())

        val fm = supportFragmentManager as FragmentManager
        var fragment = fm.findFragmentById(R.id.fragment_container)
        if (fragment == null) {
            fragment = createFragment()
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit()
        }
    }
}