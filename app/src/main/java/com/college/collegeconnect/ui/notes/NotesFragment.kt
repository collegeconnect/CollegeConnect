package com.college.collegeconnect.ui.notes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.college.collegeconnect.R
import com.college.collegeconnect.activities.DownloadNotes
import com.college.collegeconnect.activities.UploadNotes
import com.college.collegeconnect.databinding.FragmentNotesBinding
import com.college.collegeconnect.datamodels.SaveSharedPreference
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sample.viewbinding.fragment.viewBinding

class NotesFragment : Fragment(R.layout.fragment_notes) {
    lateinit var bottomNavigationView:BottomNavigationView
    private val binding: FragmentNotesBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().findViewById<TextView>(R.id.navTitle).text = "NOTES"

        bottomNavigationView = requireActivity().findViewById(R.id.bottomNav)
        binding.CourseN.setSelection(SaveSharedPreference.getCourse(context))
        binding.BranchN.setSelection(SaveSharedPreference.getBranch(context))
        binding.SemesterN.setSelection(SaveSharedPreference.getSemester(context))
        binding.UnitN.setSelection(SaveSharedPreference.getUnit(context))

        binding.fabUpload.setOnClickListener{
            startActivity(Intent(context, UploadNotes::class.java))
        }

        binding.SemesterN.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (binding.SemesterN.selectedItem.toString() == "Syllabus") {
                    binding.UnitN.visibility = View.INVISIBLE
                    binding.textView8.visibility = View.INVISIBLE
                    binding.imageView10.visibility = View.INVISIBLE
                    binding.UnitN.setSelection(0)
                } else {
                    binding.UnitN.visibility = View.VISIBLE
                    binding.textView8.visibility = View.VISIBLE
                    binding.imageView10.visibility = View.VISIBLE
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

        binding.viewnotes.setOnClickListener {
            val selected_course: String = binding.CourseN.selectedItem.toString()
            val selected_branch: String = binding.BranchN.selectedItem.toString()
            val selected_semester: String = binding.SemesterN.selectedItem.toString()
            val selected_unit: String = binding.UnitN.selectedItem.toString()

            SaveSharedPreference.setCourse(context, binding.CourseN.selectedItemPosition)
            SaveSharedPreference.setBranch(context, binding.BranchN.selectedItemPosition)
            SaveSharedPreference.setSemester(context, binding.SemesterN.selectedItemPosition)
            SaveSharedPreference.setUnit(context, binding.UnitN.selectedItemPosition)
            val intent = Intent(activity, DownloadNotes::class.java)
            val bundle = Bundle()
            bundle.putString(DownloadNotes.EXTRA_COURSE, selected_course)
            bundle.putString(DownloadNotes.EXTRA_BRANCH, selected_branch)
            bundle.putString(DownloadNotes.EXTRA_SEMESTER, selected_semester)
            bundle.putString(DownloadNotes.EXTRA_UNIT, selected_unit)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }


    override fun onStart() {
        super.onStart()
        bottomNavigationView.menu.findItem(R.id.nav_notes).isChecked = true
    }

    override fun onResume() {
        super.onResume()
        bottomNavigationView.menu.findItem(R.id.nav_notes).isChecked = true
    }


}