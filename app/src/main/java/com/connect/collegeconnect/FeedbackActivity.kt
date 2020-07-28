package com.connect.collegeconnect

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doAfterTextChanged
import com.connect.collegeconnect.datamodels.Feedback
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hsalf.smileyrating.SmileyRating
import kotlinx.android.synthetic.main.activity_feedback.*
import kotlinx.android.synthetic.main.toolbar_main.*


class FeedbackActivity : AppCompatActivity(){

    var smile = 0
    private lateinit var mood:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        val toolbar = findViewById<Toolbar>(R.id.toolbarcom)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeButtonEnabled(true)
        tvtitle.text = "Feedback"
        val alertDialog = MaterialAlertDialogBuilder(this)
        alertDialog.setTitle("Thank you")
                .setMessage("Rate us on Playstore?")

        alertDialog.setPositiveButton("Sure") { dialog, which ->
//            startActivity(Intent())
        }.setNegativeButton("Later") { dialog, which ->
            finish()
            dialog.dismiss()
        }
        ansProblem.editText?.doAfterTextChanged { ansProblem.error = null }
        ansFeature.editText?.doAfterTextChanged { ansFeature.error = null }
        ansNoFeature.editText?.doAfterTextChanged { ansNoFeature.error = null }
        ansFeature.editText?.doAfterTextChanged { ansFeature.error = null }


        smileyRating.setRating(SmileyRating.Type.GREAT,true)
        solveAns.setIndicatorTextFormat("\${TICK_TEXT}")
        indicatorSeekBar.setIndicatorTextFormat("\${TICK_TEXT}")
        smileyRating.setSmileySelectedListener { type ->
            when(type.rating){
                1 -> mood = "Tired"
                2 -> mood = "Sleepy"
                3 -> mood = "Neutral"
                4 -> mood = "Good"
                5 -> mood = "Energetic"
            }
        }

        submitFeedback.setOnClickListener{
            if(ansProblem.editText?.text == null) {
                ansProblem.error = "Field cannot be empty"
                return@setOnClickListener
            }
            if(ansNoFeature.editText?.text == null) {
                ansNoFeature.error = "Field cannot be empty"
                return@setOnClickListener
            }
            if(ansConfused.editText?.text == null) {
                ansConfused.error = "Field cannot be empty"
                return@setOnClickListener
            }
            if(ansFeature.editText?.text == null) {
                ansFeature.error = "Field cannot be empty"
                return@setOnClickListener
            }
            submit()
            val dialog = alertDialog.create()
            dialog.show()
        }
    }
    private fun submit(){
    val feedback =  Feedback(mood, ansProblem.editText?.text.toString(), solveAns.progress, ansNoFeature.editText?.text.toString(), indicatorSeekBar.progress, ansConfused.editText?.text.toString(), ansFeature.editText?.text.toString() )

    }
}