package com.college.collegeconnect.ui.event.bvest

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.college.collegeconnect.R
import com.college.collegeconnect.adapters.ImageAdapter
import com.college.collegeconnect.databinding.ActivityBvestEventBinding
import com.college.collegeconnect.datamodels.Events
import com.college.collegeconnect.ui.event.bvest.viewModels.BvestViewModel
import com.college.collegeconnect.utils.ImageHandler
import com.college.collegeconnect.utils.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.sample.viewbinding.activity.viewBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class BvestEventActivity : AppCompatActivity() {

    lateinit var bvestViewModel: BvestViewModel
    var code = ""
    private val binding: ActivityBvestEventBinding by viewBinding()
    private lateinit var event: Events
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bvest_event)

        bvestViewModel = ViewModelProvider(this).get(BvestViewModel::class.java)

        if (intent != null) event = intent.getSerializableExtra("list") as Events
        bvestViewModel.eventName = event.eventName

        //Set values
        ImageHandler().getSharedInstance(this)?.load(event.imageUrl[0])
        binding.tvEventPageTitle.text = event.eventName
        binding.organizerName.text = event.organizer
        binding.tvEventPageDate.text = date(event.date)
        binding.tvEventDescription.text = event.eventDescription
        val imageAdapter = ImageAdapter(event.imageUrl)
        binding.ivEventBanner.adapter = imageAdapter

        binding.registerEventButton.setOnClickListener {
            dialogRegistration()
        }

        bvestViewModel.returnTeam(event.eventName, code).observe(this, { team ->
            if (team.code == this.code) {
                Intent(this, TeamDetails::class.java).putExtra("team", team).also {
                    startActivity(it)
                }
            } else
                toast("called")
        })
    }

    private fun date(date: String): String {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEE, dd MMM yy", Locale.getDefault())
        val datetext: Date
        var str: String? = null
        try {
            datetext = inputFormat.parse(date) as Date
            str = outputFormat.format(datetext)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str.toString()
    }

    private fun dialogTeam() {
        val builder: AlertDialog.Builder = MaterialAlertDialogBuilder(this)
        val inflater = this.layoutInflater
        val view = inflater.inflate(R.layout.join_event_dialog_box, null)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val next = view.findViewById<ImageButton>(R.id.register_next)
        val teamCode = view.findViewById<TextInputLayout>(R.id.team_code)
        teamCode.editText?.doAfterTextChanged { teamCode.error = null }
        next.setOnClickListener {
            if (!teamCode.editText?.text.isNullOrBlank()) {
                code = teamCode.editText?.text.toString()
                bvestViewModel.loadTeamDetails(event.eventName, code)
                dialog.dismiss()
            } else {
                teamCode.error = "Enter Team Code"
                return@setOnClickListener
            }
        }
    }

    private fun dialogRegistration() {
        val builder: AlertDialog.Builder = MaterialAlertDialogBuilder(this)
        val inflater = this.layoutInflater
        val view = inflater.inflate(R.layout.register_event_dialog, null)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        view.findViewById<Button>(R.id.createTeam).setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this, RegisterEventActivity::class.java))
        }
        view.findViewById<Button>(R.id.joinButton).setOnClickListener {
            dialog.dismiss()
            dialogTeam()
        }

    }


}