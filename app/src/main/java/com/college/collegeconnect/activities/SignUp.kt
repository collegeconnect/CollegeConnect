package com.college.collegeconnect.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.college.collegeconnect.databinding.ActivitySignUpBinding
import com.college.collegeconnect.datamodels.SaveSharedPreference.setUser
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.stepOneButton.setOnClickListener {

            val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            if (currentFocus != null) inputManager.hideSoftInputFromWindow(
                currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
            val strPassword = binding.textpass.editText!!.text.toString()
            val strName = binding.textname.editText!!.text.toString()
            val strEmail = binding.textemail.editText!!.text.toString().trim { it <= ' ' }
            if (strEmail.isEmpty() && strPassword.isEmpty() && strName.isEmpty()) {
                binding.textpass.error = "Enter a valid password"
                binding.textname.error = "Enter your Full name"
                binding.textemail.error = "Enter your email address"
            } else if (strEmail.isEmpty()) {
                binding.textemail.error = "Enter your Email address"
            } else if (strPassword.isEmpty()) {
                binding.textpass.error = "Enter a valid password"
            } else if (strName.isEmpty()) {
                binding.textname.error = "Enter your name"
            } else {
                mAuth = FirebaseAuth.getInstance()
                if (isEmailValid(strEmail)) {
                    mAuth!!.fetchSignInMethodsForEmail(strEmail).addOnCompleteListener { task ->
                        val b = !task.result.signInMethods!!.isEmpty()
                        if (b) {
                            Toast.makeText(applicationContext, "Email already Exist! or Verify your email if already registered!", Toast.LENGTH_SHORT).show()
                        } else {
                            setUser(this@SignUp, strName)
                            mAuth!!.createUserWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(this@SignUp) { task ->
                                if (task.isSuccessful) {
                                    mAuth!!.currentUser!!.sendEmailVerification().addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(this@SignUp, "Registered! Email Verification sent", Toast.LENGTH_LONG).show()
                                            Toast.makeText(this@SignUp, strName, Toast.LENGTH_SHORT).show()
                                        } else Toast.makeText(
                                            this@SignUp, task.exception!!.message,
                                            Toast.LENGTH_SHORT
                                        )
                                    }
                                    Log.d(TAG, "createUserWithEmail:success")
                                    val intent = Intent(this@SignUp, MainActivity::class.java)
                                    //                                                intent.putExtra(StepTwoSignUp.EXTRA_NAME, Strname);
//                                                intent.putExtra(StepTwoSignUp.EXTRA_EMAIL, Stremail);
//                                                intent.putExtra(StepTwoSignUp.EXTRA_PASSWORD, Strpassword);
                                    // To indicate the user is signing up using email
                                    startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                                    finish()
                                }
                            }
                        }
                    }
                } else Toast.makeText(applicationContext, "Enter a Valid Email Id", Toast.LENGTH_LONG).show()
            }
            binding.textemail.editText!!.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun afterTextChanged(editable: Editable) {
                    binding.textemail.error = null
                }
            })
            binding.textpass.editText!!.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun afterTextChanged(editable: Editable) {
                    binding.textpass.error = null
                }
            })
            binding.textname.editText!!.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun afterTextChanged(editable: Editable) {
                    binding.textname.error = null
                }
            })
        }
    }

    companion object {
        private const val TAG = "Step 1/2"
        fun isEmailValid(email: String?): Boolean {
            val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
            val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(email)
            return matcher.matches()
        }
    }
}
