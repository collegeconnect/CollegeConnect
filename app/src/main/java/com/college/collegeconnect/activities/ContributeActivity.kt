package com.college.collegeconnect.activities

import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.college.collegeconnect.databinding.ActivityContributeBinding

class ContributeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContributeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContributeBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.develoeprCheck.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked -> if (isChecked) binding.nondeveloperCheck.setEnabled(false) else binding.nondeveloperCheck.setEnabled(true) })
        binding.nondeveloperCheck.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked -> if (isChecked) binding.develoeprCheck.setEnabled(false) else binding.develoeprCheck.setEnabled(true) })

        // Send email to developers
        binding.submitContri.setOnClickListener {
            if (binding.collegeContri.getEditText()!!.text.toString() == null || binding.courseContri.getEditText()!!.text.toString() == null || binding.mobileContri.getEditText()!!.text.toString() == null) Toast.makeText(this@ContributeActivity, "Please ensure all the fields are not empty.", Toast.LENGTH_SHORT).show() else {
                val intent = intent
                val name = intent.getStringExtra("Name")
                val dev_status: String
                dev_status = if (binding.develoeprCheck.isChecked) "Yes" else "No"

                // Forming email body
                val mesaage = """
                    My name is $name
                    
                    Am I a developer?: $dev_status
                    
                    College Name: ${binding.collegeContri.getEditText()!!.text}
                    
                    Course: ${binding.courseContri.getEditText()!!.text}
                    
                    Contact
                    Mobile Number: ${binding.mobileContri.getEditText()!!.text}
                    
                    Type below this line for any extra message
                    ---------------------
                    
                """.trimIndent()
                val intent_email = Intent(Intent.ACTION_SEND)
                intent_email.type = "text/plain"
                val recipients = arrayOf("college.connect8@gmail.com")
                intent_email.putExtra(Intent.EXTRA_EMAIL, recipients)
                intent_email.putExtra(Intent.EXTRA_TEXT, mesaage)
                val pm = packageManager
                val matches = pm.queryIntentActivities(intent_email, 0)
                var best: ResolveInfo? = null
                for (info in matches) if (info.activityInfo.packageName.endsWith(".gm") ||
                    info.activityInfo.name.toLowerCase().contains("gmail")
                ) best = info
                if (best != null) intent_email.setClassName(best.activityInfo.packageName, best.activityInfo.name)
                startActivity(intent_email)
            }
        }
    }
}
