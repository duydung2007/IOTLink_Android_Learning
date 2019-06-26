package android.bignerdranch.com

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import java.util.*


class CrimeFragment : Fragment() {
    private var mCrime: Crime? = null
    private var mTitleField: EditText? = null
    private var mDateButton: Button? = null
    private var mSolvedCheckBox: CheckBox? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val crimeId = activity?.intent?.getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID) as UUID
        mCrime = CrimeLab.get(activity as Context)?.getCrime(crimeId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_crime, container, false)
        mTitleField = v.findViewById(R.id.crime_title) as EditText
        mTitleField?.setText(mCrime?.getTitle())
        mTitleField?.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mCrime?.setTitle(p0.toString())
            }
        })

        mDateButton = v.findViewById(R.id.crime_date) as Button
        mDateButton?.text = mCrime?.getDate().toString()
        mDateButton?.isEnabled = false

        mSolvedCheckBox = v.findViewById(R.id.crime_solved) as CheckBox
        mSolvedCheckBox?.isChecked = mCrime?.isSolved()!!
        mSolvedCheckBox?.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                mCrime?.setSolved(p1)
            }
        })

        return v
    }
}