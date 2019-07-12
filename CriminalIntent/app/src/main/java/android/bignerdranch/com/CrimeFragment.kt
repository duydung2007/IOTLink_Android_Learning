package android.bignerdranch.com

import android.app.Activity
import android.content.Context
import android.content.Intent
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

    companion object {
        private const val ARG_CRIME_ID = "crime_id"
        private const val DIALOG_DATE = "DialogDate"
        private const val REQUEST_DATE = 0

        fun newInstance(crimeId: UUID): CrimeFragment {
            var args = Bundle()
            args.putSerializable(ARG_CRIME_ID, crimeId)

            var fragment = CrimeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var mCrime: Crime? = null
    private var mTitleField: EditText? = null
    private var mDateButton: Button? = null
    private var mSolvedCheckBox: CheckBox? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val crimeId = arguments?.getSerializable(ARG_CRIME_ID) as UUID
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
        updateDate()
        mDateButton?.setOnClickListener {
            val manager = fragmentManager
            val dialog = DatePickerFragment.newInstance(mCrime?.getDate()!!)
            dialog.setTargetFragment(CrimeFragment@this, REQUEST_DATE)
            dialog.show(manager, DIALOG_DATE)
        }

        mSolvedCheckBox = v.findViewById(R.id.crime_solved) as CheckBox
        mSolvedCheckBox?.isChecked = mCrime?.isSolved()!!
        mSolvedCheckBox?.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                mCrime?.setSolved(p1)
            }
        })

        return v
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_DATE) {
            val date = data?.getSerializableExtra(DatePickerFragment.EXTRA_DATE) as Date
            mCrime?.setDate(date)
            updateDate()
        }
    }

    private fun updateDate() {
        mDateButton?.text = mCrime?.getDate().toString()
    }
}