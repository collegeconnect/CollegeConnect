package com.college.collegeconnect.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.college.collegeconnect.R
import com.college.collegeconnect.databinding.ActivityMainBinding
import com.college.collegeconnect.datamodels.SaveSharedPreference.getUpload
import com.college.collegeconnect.datamodels.SaveSharedPreference.setUserName
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private val RC_SIGN_IN = 1

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(R.layout.activity_main)
        firebaseAuth = FirebaseAuth.getInstance()
        val i = Intent(this, SignUp::class.java)

        binding.btnRegister.setOnClickListener(View.OnClickListener { startActivity(i) })
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        findViewById<View>(R.id.googleSign).setOnClickListener {
            val signinintent = mGoogleSignInClient.getSignInIntent()
            startActivityForResult(signinintent, RC_SIGN_IN)
        }

        binding.btnLogin.setOnClickListener(View.OnClickListener { logIn() })

        binding.editTextPassword.editText!!.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) logIn()
            false
        }

        binding.editTextEmail.getEditText()!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                binding.editTextEmail.setError(null)
            }
        })
        binding.editTextPassword.editText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                binding.editTextPassword.error = null
            }
        })
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                Toast.makeText(this, "Google sign in failed" + e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id)
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val newuser = task.result.additionalUserInfo!!.isNewUser
                    if (newuser) {
                        startActivity(Intent(applicationContext, StepTwoSignUp::class.java))
                    } else {
                        Log.d(TAG, "signInWithCredential:success")
                        val user = firebaseAuth.currentUser
                        Toast.makeText(applicationContext, user!!.displayName, Toast.LENGTH_SHORT).show()
                        setUserName(applicationContext, user.email)
                        startActivity(Intent(applicationContext, Navigation::class.java))
                    }
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    //                            Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                    Snackbar.make(findViewById(R.id.googleSign), "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                    //                            updateUI(null);
                }
            }
    }

    private fun logIn() {
        try {
            val inputManager = (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
            inputManager.hideSoftInputFromWindow(
                Objects.requireNonNull(currentFocus)?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        } catch (ignored: Exception) {
        }
        val progressBar = findViewById<ProgressBar>(R.id.MainProgressBar)
        binding.btnLogin.isEnabled = false
        binding.btnRegister.isEnabled = false
        progressBar.visibility = View.VISIBLE
        val strEmail = binding.editTextEmail.editText!!.text.toString()
        val strPass = binding.editTextPassword.editText!!.text.toString()
        if (strEmail.isEmpty() && strPass.isEmpty()) {
            binding.editTextEmail.error = "Enter your Email address"
            binding.editTextPassword.error = "Enter a Valid password"
            progressBar.visibility = View.GONE
            binding.btnLogin.isEnabled = true
            binding.btnRegister.isEnabled = true
        } else if (strEmail.isEmpty()) {
            binding.editTextEmail.error = "Enter your Email address"
            progressBar.visibility = View.GONE
            binding.btnLogin.isEnabled = true
            binding.btnRegister.isEnabled = true
        } else if (strPass.isEmpty()) {
            binding.editTextPassword.error = "Enter a valid password"
            progressBar.visibility = View.GONE
            binding.btnLogin.isEnabled = true
            binding.btnRegister.isEnabled = true
        } else {
            firebaseAuth.signInWithEmailAndPassword(strEmail, strPass).addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    firebaseAuth!!.fetchSignInMethodsForEmail(strEmail).addOnCompleteListener { task ->
                        val b = !task.result.signInMethods!!.isEmpty()
                        if (b) {
                            Toast.makeText(applicationContext, "Incorrect Password!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext, "User not Registered!", Toast.LENGTH_SHORT).show()
                        }
                        progressBar.visibility = View.GONE
                        binding.btnLogin.isEnabled = true
                        binding.btnRegister.isEnabled = true
                    }
                } else {
                    if (firebaseAuth.currentUser!!.isEmailVerified) {
                        if (getUpload(this@MainActivity)) {
                            startActivity(Intent(this@MainActivity, Navigation::class.java))
                            finish()
                        } else {
                            // Set email
                            setUserName(this@MainActivity, strEmail)
                            val intent = Intent(this@MainActivity, StepTwoSignUp::class.java)
                            intent.putExtra(StepTwoSignUp.EXTRA_PREV, "MainActivity")
                            startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
                            finish()
                        }
                    } else Toast.makeText(this@MainActivity, "Email Not Verified! Please verify before continuing", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                    binding.btnLogin.isEnabled = true
                    binding.btnRegister.isEnabled = true
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        //        if(GoogleSignIn.getLastSignedInAccount(this)!=null) {
//            startActivity(new Intent(this, navigation.class));
//            finish();
//        }
//        else if(currentUser!=null){
//            startActivity(new Intent(this, navigation.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//            finish();
//        }
//        else if (SaveSharedPreference.getUserName(MainActivity.this).length() != 0) {
//            startActivity(new Intent(this, navigation.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//            finish();
//        }
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null && firebaseUser.isEmailVerified) {
            startActivity(Intent(this, Navigation::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            finish()
        }
    }

    fun forgotpassword(view: View?) {
        val Stremail = binding.editTextEmail.editText!!.text.toString()
        if (Stremail.isEmpty()) binding.editTextEmail.error = "Enter your Email address" else FirebaseAuth.getInstance().sendPasswordResetEmail(Stremail).addOnCompleteListener { task -> if (task.isSuccessful) Toast.makeText(applicationContext, "Password Reset Email sent. Check Inbox", Toast.LENGTH_LONG).show() }.addOnFailureListener { Toast.makeText(applicationContext, "Email not registered!", Toast.LENGTH_LONG).show() }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
