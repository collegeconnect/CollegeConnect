package com.college.collegeconnect.ui.home

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.amulyakhare.textdrawable.TextDrawable
import com.college.collegeconnect.BuildConfig
import com.college.collegeconnect.R
import com.college.collegeconnect.adapters.HomeRecyclerAdapter
import com.college.collegeconnect.datamodels.SaveSharedPreference
import com.college.collegeconnect.viewmodels.HomeViewModel
import com.college.collegeconnect.settingsActivity.SettingsActivity
import com.college.collegeconnect.utils.ImageHandler
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {
    var bottomNavigationView: BottomNavigationView? = null
    var drawable: TextDrawable? = null
    lateinit var tv: TextView
    var totalAttendance: TextView? = null
    var nameField: TextView? = null
    var enrollNo: TextView? = null
    var branch: TextView? = null
    var prfileImage: CircleImageView? = null
    private val storage = FirebaseStorage.getInstance()
    var uri: Uri? = null
    private var storageRef: StorageReference? = null
    private var mcontext: Context? = null
    lateinit var homeViewModel: HomeViewModel
    var registered: ListenerRegistration? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        prfileImage = view.findViewById(R.id.imageView3)
        nameField = view.findViewById(R.id.nameField)
        enrollNo = view.findViewById(R.id.textView3)
        branch = view.findViewById(R.id.textView4)
        totalAttendance = view.findViewById(R.id.aggregateAttendance)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity != null) bottomNavigationView = requireActivity().findViewById(R.id.bottomNav)
        tv = requireActivity().findViewById(R.id.navTitle)
        tv.text = "HOME"
        storageRef = storage.reference

        totalAttendance!!.isEnabled = false
        nameField!!.isEnabled = false
        enrollNo!!.isEnabled = false
        branch!!.isEnabled = false

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.returnName().observe(requireActivity(), Observer { s -> nameField!!.text = s })
        homeViewModel.returnRoll().observe(requireActivity(), Observer { s -> enrollNo!!.text = s })
        homeViewModel.returnBranch().observe(requireActivity(), Observer { s -> branch!!.text = s })
        val file = File("/data/user/0/com.college.collegeconnect/files/dp.jpeg")
        if (file.exists()) {
            uri = Uri.fromFile(file)
            context?.applicationContext?.let { ImageHandler().getSharedInstance(it)?.load(uri)?.into(prfileImage) }
            Log.d("HomeFrag", "onClick: already exists")
        } else {
            storageRef!!.child("User/" + SaveSharedPreference.getUserName(activity) + "/DP.jpeg").downloadUrl.addOnSuccessListener { uri -> // Got the download URL for 'users/me/profile.png'
                this@HomeFragment.uri = uri
                download_dp()
            }.addOnFailureListener { }
        }
        if (uri != null) Picasso.get().load(uri).into(prfileImage)

        homeViewModel.getAttended().observe(requireActivity(), Observer { atten ->
           homeViewModel.getMissed().observe(requireActivity(), Observer { miss ->
                if (atten != null && miss != null) {
                    val percentage = atten.toFloat() / (atten.toFloat() + miss.toFloat())
                    if (percentage.isNaN())
                        totalAttendance!!.text = "0%"
                    else
                        totalAttendance!!.text = "%.0f".format(percentage*100)+"%"
                } else
                    totalAttendance!!.text = "0%"
            })

        })
        homeViewModel.getMissed().observe(requireActivity(), Observer { miss ->
            homeViewModel.getAttended().observe(requireActivity(), Observer { atten ->
                if (atten != null && miss != null) {
                    val percentage = atten.toFloat().div((atten.toFloat() + miss.toFloat()))
                    if (percentage.isNaN())
                        totalAttendance!!.text = "0%"
                    else
                        totalAttendance!!.text = "%.0f".format(percentage*100)+"%"
                } else
                    totalAttendance!!.text = "0%"
            })
        })

        settings_btn.setOnClickListener {
            context?.startActivity(Intent(context,SettingsActivity::class.java))
        }

        recyclerviewHome.layoutManager =  LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val adapter = context?.let { HomeRecyclerAdapter(it) }
        recyclerviewHome.adapter = adapter

        //Set happening now
        var now = false
        homeViewModel.getHappeningNow().observe(requireActivity(), {
            it.forEach { it1 ->
                val pattern = "dd-MM-yyyy"
                val dateInString: String = SimpleDateFormat(pattern, Locale.getDefault()).format(Date())
                val startingTime = getMilli("$dateInString ${it1.startTime}")
                val endingTime = getMilli("$dateInString ${it1.endTime}")
                if (System.currentTimeMillis() in startingTime until endingTime) {
                    txt_now_subject_title.text = it1.subjectName
                    txt_now_room_num.text = it1.roomNumber
                    txt_now_time.text = "${it1.startTimeShow} - ${it1.endTimeShow}"
                    now = true
                }
            }
            if (!now) {
                try {
                    txt_now_state.text = "No class happening currently"
                    txt_now_time.visibility = View.GONE
                    card_now_class.visibility = View.GONE
                } catch (e:Exception) {
                    Log.d("HomeFragment", "onActivityCreated: ${e.message}")
                }
            }
        })
    }

    // get time in milliseconds
    private fun getMilli(myDate: String): Long {
        val format = SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault())
        val d = format.parse(myDate)
        return d.time
    }

    private fun download_dp() {
        val downloadManager = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(uri)
        request.setDestinationInExternalFilesDir(context, "", "dp.jpeg")
        val id = downloadManager.enqueue(request)
        val onComplete: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val c = downloadManager.query(DownloadManager.Query().setFilterById(id))
                if (c != null) {
                    c.moveToFirst()
                    try {
                        val fileUri = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
                        uri = Uri.parse(fileUri)
                        Picasso.get().load(uri).into(prfileImage)
                        copyFile("/storage/emulated/0/Android/data/" + BuildConfig.APPLICATION_ID + "/files", "/dp.jpeg", requireContext().filesDir.absolutePath)
                        File("/storage/emulated/0/Android/data/com.college.collegeconnect/files/dp.jpeg").delete()
                    } catch (e: Exception) {
                        Log.e("error", "Could not open the downloaded file")
                    }
                }
            }
        }
        requireContext().registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    private fun copyFile(inputPath: String, inputFile: String, outputPath: String) {
        var `in`: InputStream? = null
        var out: OutputStream? = null
        try {

            //create output directory if it doesn't exist
            val dir = File(outputPath)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            `in` = FileInputStream(inputPath + inputFile)
            out = FileOutputStream(outputPath + inputFile)
            val buffer = ByteArray(1024)
            var read: Int
            while (`in`.read(buffer).also { read = it } != -1) {
                out.write(buffer, 0, read)
            }
            `in`.close()
            `in` = null

            // write the output file (You have now copied the file)
            out.flush()
            out.close()
            out = null
        } catch (fnfe1: FileNotFoundException) {
            Log.e("tag", fnfe1.message.toString())
        } catch (e: Exception) {
            Log.e("tag", e.message.toString())
        }
    }

    override fun onStart() {
        super.onStart()
        bottomNavigationView!!.menu.findItem(R.id.nav_home).isChecked = true
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mcontext = context
    }

    override fun onResume() {
        super.onResume()
        bottomNavigationView!!.menu.findItem(R.id.nav_home).isChecked = true
        if (SaveSharedPreference.getClearall1(mcontext)) {
            val file = File("/data/user/0/com.college.collegeconnect/files/dp.jpeg")
            if (file.exists()) {
                uri = Uri.fromFile(file)
                Picasso.get().invalidate(uri)
                SaveSharedPreference.setClearall1(context, false)
                Picasso.get().load(uri).into(prfileImage)
            }
        }
    }

    override fun onDestroyView() {
        if (registered != null) registered!!.remove()
        super.onDestroyView()
    }
}