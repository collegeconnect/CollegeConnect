package com.college.collegeconnect.ui.event.bvest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.college.collegeconnect.R
import com.college.collegeconnect.datamodels.SaveSharedPreference
import com.college.collegeconnect.datamodels.TeamMate
import com.college.collegeconnect.ui.event.bvest.viewModels.BvestViewModel
import com.college.collegeconnect.utils.RandomGenerator
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_register_event.*

class RegisterEventActivity : AppCompatActivity(), TaskListener {
    lateinit var code: String
    private lateinit var bvestViewModel: BvestViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_event)
        bvestViewModel = ViewModelProvider(this).get(BvestViewModel::class.java)
        bvestViewModel.taskListener = this
        code = RandomGenerator().randomString(6)
        val list = bvestViewModel.checkTeamCode()
        val con = createCode(list, code)
        teamCode.text = con
        nameText.editText?.apply {
            setText(SaveSharedPreference.getUser(this@RegisterEventActivity))
            isEnabled = false
        }
        emailText.editText?.apply {
            setText(SaveSharedPreference.getUserName(this@RegisterEventActivity))
            isEnabled = false
        }
        collegetext.editText?.apply {
            setText(SaveSharedPreference.getCollege(this@RegisterEventActivity))
            isEnabled = false
        }

        doneButton.setOnClickListener {
            if (phoneText.editText?.text.isNullOrEmpty()) {
                phoneText.error = "Enter a Phone Number"
                phoneText.editText?.requestFocus()
                return@setOnClickListener
            } else if (phoneText.editText?.text?.length!! > 10) {
                phoneText.error = "Enter a Valid Phone Number"
                phoneText.editText?.requestFocus()
                return@setOnClickListener
            }
            if (teamName.editText?.text.isNullOrEmpty()) {
                teamName.error = "Enter a Team Name"
                teamName.editText?.requestFocus()
                return@setOnClickListener
            }
            register(list, con)
        }
    }

    private fun register(list: ArrayList<String>, code: String) {
        val teamMate1 = TeamMate(SaveSharedPreference.getUser(this), SaveSharedPreference.getUserName(this))
        bvestViewModel.createTeam(teamName.editText?.text.toString(), code, list, phoneText.editText?.text.toString(), teamMate1)

    }

    private fun dialogSuccess(message: String, title: String) {
        val builder: AlertDialog.Builder = MaterialAlertDialogBuilder(this)
        builder.setMessage(message)
        builder.setTitle(title)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            finish()
        }
    }

    private fun createCode(list: ArrayList<String>, code: String): String {
        return if (!list.contains(code)) {
            code
        } else {
            val c = RandomGenerator().randomString(6)
            createCode(list, c)
        }
    }

    override fun complete() {
        dialogSuccess("Team has been created Successfully","Success")
    }

    override fun onError(message: String) {
        dialogSuccess("Unable to create team\nError Code: $message","Failed")
    }
}