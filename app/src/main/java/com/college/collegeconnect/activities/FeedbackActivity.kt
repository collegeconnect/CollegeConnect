package com.college.collegeconnect.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doAfterTextChanged
import com.college.collegeconnect.R
import com.college.collegeconnect.databinding.ActivityFeedbackBinding
import com.college.collegeconnect.datamodels.Feedback
import com.college.collegeconnect.datamodels.SaveSharedPreference
import com.college.collegeconnect.utils.FirebaseUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.hsalf.smileyrating.SmileyRating
import com.sample.viewbinding.activity.viewBinding


class FeedbackActivity : AppCompatActivity() {

    private lateinit var mood: String
    lateinit var databaseReference: DatabaseReference
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var alertDialog: AlertDialog.Builder
    var email: String = ""
    private lateinit var manager:ReviewManager
    private val binding: ActivityFeedbackBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        firebaseAuth = FirebaseAuth.getInstance()
        val uid = firebaseAuth.currentUser?.uid.toString()
        databaseReference = FirebaseUtil.getDatabase().getReference("Feedback").child(uid)
        email = firebaseAuth.currentUser?.email.toString()
        val toolbar = findViewById<Toolbar>(R.id.toolbarcom)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeButtonEnabled(true)
        binding.toolbarfeedback.tvtitle.text = "Feedback"
        manager = ReviewManagerFactory.create(this)
        alertDialog = MaterialAlertDialogBuilder(this)
        alertDialog.setTitle("Thank you")
                .setMessage("Rate us on Playstore?")

//        alertDialog.setPositiveButton("Sure") { _, _ ->
//            val uri = Uri.parse("market://details?id=" + this.packageName)
//            val goToMarket = Intent(Intent.ACTION_VIEW, uri).apply {
//                addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
//                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
//                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
//            }
//            try {
//                startActivity(goToMarket)
//            } catch (e: ActivityNotFoundException) {
//                startActivity(Intent(Intent.ACTION_VIEW,
//                        Uri.parse("http://play.google.com/store/apps/details?id=" + this.packageName)))
//            }
//        }.setNegativeButton("Later") { dialog, _ ->
//            dialog.dismiss()
//            finish()
//            Navigation.act.finish()
//        }
        alertDialog.setPositiveButton("Sure") { dialog, _ ->
            val request = manager.requestReviewFlow()
            request.addOnCompleteListener { request ->
                if (request.isSuccessful) {
                    // We got the ReviewInfo object
                    val reviewInfo = request.result
                    val flow = manager.launchReviewFlow(this, reviewInfo)
                    flow.addOnCompleteListener {
                        SaveSharedPreference.setRev(this,true)
                    }
                } else {
                    // There was some problem, continue regardless of the result.
                    dialog.dismiss()
                    finish()
                    Navigation.act.finish()
                }
            }
        }.setNegativeButton("Later") { dialog, _ ->
            dialog.dismiss()
            finish()
            Navigation.act.finish()
        }

        binding.ansProblem.editText?.doAfterTextChanged { binding.ansProblem.error = null }
        binding.ansFeature.editText?.doAfterTextChanged { binding.ansFeature.error = null }
        binding.ansNoFeature.editText?.doAfterTextChanged { binding.ansNoFeature.error = null }


        binding.smileyRating.setRating(SmileyRating.Type.GREAT, true)
        binding.solveAns.setIndicatorTextFormat("\${TICK_TEXT}")
        binding.indicatorSeekBar.setIndicatorTextFormat("\${TICK_TEXT}")
        binding.smileyRating.setSmileySelectedListener { type ->
            when (type.rating) {
                1 -> mood = "Tired"
                2 -> mood = "Sleepy"
                3 -> mood = "Neutral"
                4 -> mood = "Good"
                5 -> mood = "Energetic"
            }
        }

        binding.submitFeedback.setOnClickListener {
            if (binding.ansProblem.editText?.text == null) {
                binding.ansProblem.error = "Field cannot be empty"
                return@setOnClickListener
            }
            if (binding.ansNoFeature.editText?.text == null) {
                binding.ansNoFeature.error = "Field cannot be empty"
                return@setOnClickListener
            }
            if (binding.ansConfused.editText?.text == null) {
                binding.ansConfused.error = "Field cannot be empty"
                return@setOnClickListener
            }
            if (binding.ansFeature.editText?.text == null) {
                binding.ansFeature.error = "Field cannot be empty"
                return@setOnClickListener
            }
            submit()
        }
    }

    private fun submit() {
        val feedback = Feedback(email,
                mood,
            binding.ansProblem.editText?.text.toString(),
                binding.solveAns.progress,
            binding.ansNoFeature.editText?.text.toString(),
                binding.indicatorSeekBar.progress,
                binding.ansConfused.editText?.text.toString(),
            binding.ansFeature.editText?.text.toString())
        databaseReference.setValue(feedback)
        if(!SaveSharedPreference.getReview(this)) {
            val dialog = alertDialog.create()
            dialog.show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish(); return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}