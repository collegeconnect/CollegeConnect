package com.college.collegeconnect.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import com.college.collegeconnect.R
import com.college.collegeconnect.databinding.ActivityStepTwoSignUpBinding
import com.college.collegeconnect.datamodels.SaveSharedPreference.getUser
import com.college.collegeconnect.datamodels.SaveSharedPreference.setCollege
import com.college.collegeconnect.datamodels.SaveSharedPreference.setUploaded
import com.college.collegeconnect.datamodels.SaveSharedPreference.setUser
import com.college.collegeconnect.datamodels.SaveSharedPreference.setUserName
import com.college.collegeconnect.datamodels.User
import com.college.collegeconnect.utils.FirebaseUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.*
import java.util.*

class StepTwoSignUp : AppCompatActivity() {
    private lateinit var binding: ActivityStepTwoSignUpBinding

    private var databaseReference: DatabaseReference? = null
    private val firebaseDatabase = FirebaseUtil.getDatabase()
    private lateinit var mAuth: FirebaseAuth
    private lateinit var receivedPRev: String
    private lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var documentReference: DocumentReference
    lateinit var listener: ListenerRegistration
    lateinit var valueListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStepTwoSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()

        firebaseFirestore = FirebaseFirestore.getInstance()
        documentReference = firebaseFirestore.collection("users").document(mAuth.currentUser!!.uid)
        databaseReference = firebaseDatabase.getReference("Colleges")

        val arrayList = ArrayList<String>()
        arrayList.add("Other")
        val spinnerArrayAdapter = ArrayAdapter(this@StepTwoSignUp, android.R.layout.simple_spinner_item, arrayList)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.collegeSpinner.adapter = spinnerArrayAdapter
        databaseReference!!.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val arrayList2 = dataSnapshot.value as ArrayList<String>?
                    arrayList.addAll(arrayList2!!)
                    spinnerArrayAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            }.also { valueListener = it }
        )
        val intent = intent
        val receivedName = intent.getStringExtra(EXTRA_NAME)
        val receivedEmail = intent.getStringExtra(EXTRA_EMAIL)
        val receivedPassword = intent.getStringExtra(EXTRA_PASSWORD)
        receivedPRev = intent.getStringExtra(EXTRA_PREV).toString()
        binding.tvContribute.setOnClickListener(
            View.OnClickListener {
                val intent = Intent(this@StepTwoSignUp, ContributeActivity::class.java)
                intent.putExtra("Name", receivedName)
                startActivity(intent)
            }
        )
        binding.collegeSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                if (position == 0) binding.otherCollege.visibility = View.VISIBLE else binding.otherCollege.visibility = View.GONE
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        binding.stepTwoButton.setOnClickListener {
            val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            if (currentFocus != null) inputManager.hideSoftInputFromWindow(
                    currentFocus!!.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
            )
            val roll = binding.textuser.editText!!.text.toString()
            val branch = binding.textbranch.editText!!.text.toString()
            val year = binding.year.editText!!.text.toString()
            if (roll.isEmpty() && branch.isEmpty() && year.isEmpty()) {
                binding.textuser.error = "Enter Roll Number"
                binding.textbranch.error = "Enter Branch Name"
                binding.year.error = "Enter Academic Year"
            } else if (branch.isEmpty()) {
                binding.textbranch.error = "Enter Branch Name"
            } else if (roll.isEmpty()) {
                binding.textuser.error = "Enter Roll Number"
            } else if (year.isEmpty()) {
                binding.year.error = "Enter Academic Year"
            } else {
                if (binding.collegeSpinner.selectedItem.toString() == "Other" && binding.otherCollege.getEditText()!!.text.toString().isEmpty()) binding.otherCollege.error = "Enter College Name" else {
                    val college: String
                    if (binding.collegeSpinner.selectedItem.toString() == "Other") {
                        college = binding.otherCollege.editText!!.text.toString()
                        try {
                            databaseReference = firebaseDatabase.getReference("CollegesInputFromUsers")
                            databaseReference!!.child(System.currentTimeMillis().toString()).setValue(binding.otherCollege.editText!!.text.toString())
                        } catch (e: Exception) {
                            Log.d(TAG, "onClick: " + e.message)
                        }
                    } else {
                        college = binding.collegeSpinner.getSelectedItem().toString()
                    }
                    setCollege(this@StepTwoSignUp, college)
                    if (receivedPRev == null) { // google
                        User.addUser(roll, mAuth.currentUser!!.email, mAuth.currentUser!!.displayName, branch, college, year)
                        setUserName(applicationContext, mAuth.currentUser!!.email)
                        startActivity(Intent(applicationContext, Navigation::class.java))
                        finish()
                    } else { // email
                        setUploaded(this@StepTwoSignUp, true)
                        if (getUser(this@StepTwoSignUp) == "") {
                            listener = documentReference!!.addSnapshotListener { documentSnapshot: DocumentSnapshot?, error: FirebaseFirestoreException? ->
                                try {
                                    assert(documentSnapshot != null)
                                    val name = documentSnapshot!!.getString("name")
                                    User.addUser(roll, mAuth!!.currentUser!!.email, name, branch, college, year)
                                    Log.d(TAG, "Details Uploaded!")
                                    setUser(this@StepTwoSignUp, name)
                                    val intent = Intent(this@StepTwoSignUp, MainActivity::class.java)
                                    startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                                    finish()
                                } catch (ignored: Exception) {
                                }
                            }
                        } else {
                            User.addUser(roll, mAuth.currentUser!!.email, getUser(this@StepTwoSignUp), branch, college, year)
                            Log.d(TAG, "Details Uploaded!")
                            val intent = Intent(this@StepTwoSignUp, MainActivity::class.java)
                            startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                            finish()
                        }
                    }
                }
            }
            binding.otherCollege.editText!!.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun afterTextChanged(editable: Editable) {
                    binding.otherCollege.setError(null)
                }
            })
            binding.textuser.editText!!.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun afterTextChanged(editable: Editable) {
                    binding.textuser.setError(null)
                }
            })
            binding.textbranch.editText!!.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun afterTextChanged(editable: Editable) {
                    binding.textbranch.setError(null)
                }
            })
        }
    }

    override fun onBackPressed() {
        if (receivedPRev != null) super.onBackPressed() else Toast.makeText(this, "Please fill in the details and click submit!", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        if (valueListener != null) databaseReference!!.removeEventListener(valueListener!!)
        if (listener != null) listener!!.remove()
        super.onDestroy()
    }

    companion object {
        const val EXTRA_NAME = "name"
        const val EXTRA_EMAIL = "email"
        const val EXTRA_PASSWORD = "password"
        const val EXTRA_PREV = "previous"
        private const val TAG = "Step Two Sign Up"
    }
}
