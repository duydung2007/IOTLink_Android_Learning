package android.bignerdranch.com

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import android.text.format.DateFormat
import java.util.*


class CrimeFragment : Fragment() {

    companion object {
        private const val ARG_CRIME_ID = "crime_id"
        private const val DIALOG_DATE = "DialogDate"
        private const val REQUEST_DATE = 0
        private const val REQUEST_CONTACT = 1

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
    private var mReportButton: Button? = null
    private var mSuspectButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val crimeId = arguments?.getSerializable(ARG_CRIME_ID) as UUID
        mCrime = CrimeLab.get(activity as Context)?.getCrime(crimeId)
    }

    override fun onPause() {
        super.onPause()
        CrimeLab.get(activity as Context)?.updateCrime(mCrime!!)
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
            dialog.setTargetFragment(this, REQUEST_DATE)
            dialog.show(manager, DIALOG_DATE)
        }

        mSolvedCheckBox = v.findViewById(R.id.crime_solved) as CheckBox
        mSolvedCheckBox?.isChecked = mCrime?.isSolved()!!
        mSolvedCheckBox?.setOnCheckedChangeListener { _, p1 -> mCrime?.setSolved(p1) }

        mReportButton = v.findViewById(R.id.crime_report) as Button
        mReportButton?.setOnClickListener {
            var i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_TEXT, getCrimeReport())
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_suspect))
            i = Intent.createChooser(i, getString(R.string.send_report))
            startActivity(i)
        }

        val pickContact = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        mSuspectButton = v.findViewById(R.id.crime_suspect) as Button
        mSuspectButton?.setOnClickListener {
            startActivityForResult(pickContact, REQUEST_CONTACT)
        }
        if (mCrime?.getSuspect() != null) {
            mSuspectButton?.text = mCrime?.getSuspect()
        }

        val packageManager = activity?.packageManager
        if (packageManager?.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton?.isEnabled = false
        }

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
        else if (requestCode == REQUEST_CONTACT && data != null) {
            val contactUri = data.data
            // Specify which fields you want your query to return
            // values for
            val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
            // Perform your query - the contactUri is like a "where"
            // clause here
            val c = activity?.contentResolver?.query(contactUri, queryFields, null, null, null)
            c.use { c ->
                // Double-check that you actually got results
                if (c?.count == 0) {
                    return
                }
                // Pull out the first column of the first row of data -
                // that is your suspect's name
                c?.moveToFirst()
                val suspect = c?.getString(0)
                mCrime?.setSuspect(suspect)
                mSuspectButton?.setText(suspect)
            }
            c?.close()
        }
    }

    private fun updateDate() {
        mDateButton?.text = mCrime?.getDate().toString()
    }

    fun getCrimeReport(): String? {
        var solvedString: String? = if (mCrime!!.isSolved()) {
            getString(R.string.crime_report_solved)
        } else {
            getString(R.string.crime_report_unsolved)
        }

        val dateFormat = "EEE, MMM, dd"
        val dateString = DateFormat.format(
            dateFormat,
            mCrime?.getDate()
        ).toString()

        var suspect: String? = mCrime?.getSuspect()
        suspect = if (suspect == null) {
            getString(R.string.crime_report_no_suspect)
        } else {
            getString(R.string.crime_report_suspect, suspect)
        }

        return getString(R.string.crime_report, mCrime?.getTitle(), dateString, solvedString, suspect)
    }
}