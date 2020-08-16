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
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModelProvider
import com.amulyakhare.textdrawable.TextDrawable
import com.college.collegeconnect.BuildConfig
import com.college.collegeconnect.R
import com.college.collegeconnect.database.AttendanceDatabase
import com.college.collegeconnect.datamodels.DatabaseHelper
import com.college.collegeconnect.datamodels.SaveSharedPreference
import com.college.collegeconnect.models.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import de.hdodenhof.circleimageview.CircleImageView
import java.io.*
import java.lang.Float.NaN

class HomeFragment : Fragment() {
    var bottomNavigationView: BottomNavigationView? = null
    var drawable: TextDrawable? = null
    lateinit var tv: TextView
    var totalAttendance: TextView? = null
    var nameField: EditText? = null
    var enrollNo: EditText? = null
    var branch: EditText? = null
    var prfileImage: CircleImageView? = null
    private val storage = FirebaseStorage.getInstance()
    var uri: Uri? = null
    private var storageRef: StorageReference? = null
    private var mcontext: Context? = null

    //    private FirebaseFirestore firebaseFirestore;
    var documentReference: DocumentReference? = null
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity != null) bottomNavigationView = requireActivity().findViewById(R.id.bottomNav)
        tv = requireActivity().findViewById(R.id.navTitle)
        tv.text = "HOME"
        storageRef = storage.reference

        //Get user id
//        auth = FirebaseAuth.getInstance();
//        FirebaseUser firebaseUser = auth.getCurrentUser();
//        assert firebaseUser != null;
//        String userId = firebaseUser.getUid();
//        firebaseFirestore = FirebaseFirestore.getInstance();
        totalAttendance!!.isEnabled = false
        nameField!!.isEnabled = false
        enrollNo!!.isEnabled = false
        branch!!.isEnabled = false

//        loadData();

//        documentReference = firebaseFirestore.collection("users").document(userId);
//        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
//                .setPersistenceEnabled(true)
//                .build();
//        firebaseFirestore.setFirestoreSettings(settings);
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        //        homeViewModel.loadData();
        homeViewModel.returnName().observe(requireActivity(), Observer { s -> nameField!!.setText(s) })
        homeViewModel.returnRoll().observe(requireActivity(), Observer { s -> enrollNo!!.setText(s) })
        homeViewModel.returnBranch().observe(requireActivity(), Observer { s -> branch!!.setText(s) })
        //        loadDataFirestore();
        val file = File("/data/user/0/com.college.collegeconnect/files/dp.jpeg")
        if (file.exists()) {
            uri = Uri.fromFile(file)
            Picasso.get().load(uri).into(prfileImage)
            Log.d("HomeFrag", "onClick: already exists")
        } else {
            storageRef!!.child("User/" + SaveSharedPreference.getUserName(activity) + "/DP.jpeg").downloadUrl.addOnSuccessListener { uri -> // Got the download URL for 'users/me/profile.png'
                this@HomeFragment.uri = uri
                download_dp()
            }.addOnFailureListener { }
        }
        if (uri != null) Picasso.get().load(uri).into(prfileImage)

        homeViewModel.returnTot().observe(requireActivity(), Observer { list ->
                var atten = 0
                var miss = 0
//                Toast.makeText(context, "observation running", Toast.LENGTH_SHORT).show()
                if (list != null) {
                    atten = list[0]
                    miss = list[1]
                }
                Toast.makeText(context, "$miss, $atten", Toast.LENGTH_SHORT).show()
                val percentage = atten.toFloat() / (atten.toFloat() + miss.toFloat())
                if (percentage.isNaN())
                    totalAttendance!!.text = "Aggregate\nAttendance: 0.00%"
                else
                    totalAttendance!!.text = "Aggregate\nAttendance: ${percentage * 100}%"
            })

//            sub?.observe(requireActivity(), Observer {
//                Toast.makeText(context, "observation running", Toast.LENGTH_SHORT).show()
//                var atten = 0
//                var miss = 0
//                homeViewModel.returnTot().observe(requireActivity(), Observer {
//                    if (it != null) {
//                        atten = it[0]
//                        miss = it[1]
//                    }
//                    Toast.makeText(context, "$miss, $atten", Toast.LENGTH_SHORT).show()
//                    val percentage = atten.toFloat() / (atten.toFloat() + miss.toFloat())
//                    if (percentage.isNaN())
//                        totalAttendance!!.text = "Aggregate\nAttendance: 0.00%"
//                    else
//                        totalAttendance!!.text = "Aggregate\nAttendance: ${percentage * 100}%"
//                })
//            })

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
            Log.e("tag", fnfe1.message)
        } catch (e: Exception) {
            Log.e("tag", e.message)
        }
    }

    //    private void loadDataFirestore() {
    //        registered = documentReference.addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<DocumentSnapshot>() {
    //            @Override
    //            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
    //                assert documentSnapshot != null;
    //                try {
    //                    String name = documentSnapshot.getString("name");
    //                    String rollNo = documentSnapshot.getString("rollno");
    //                    String strbranch = documentSnapshot.getString("branch");
    //                    SaveSharedPreference.setUser(mcontext, name);
    //                    nameField.setText(SaveSharedPreference.getUser(mcontext));
    //                    enrollNo.setText(rollNo);
    //                    branch.setText(strbranch);
    //
    //                    assert name != null;
    //                    int space = name.indexOf(" ");
    //                    int color = Navigation.generatecolor();
    //                    drawable = TextDrawable.builder().beginConfig()
    //                            .width(150)
    //                            .height(150)
    //                            .bold()
    //                            .endConfig()
    //                            .buildRound(name.substring(0, 1) + name.substring(space + 1, space + 2), color);
    //                    prfileImage.setImageDrawable(drawable);
    //                } catch (Exception e) {
    //                    Log.d("Home", "onEvent: " + e.getMessage());
    //                }
    //                if (uri != null)
    //                    Picasso.get().load(uri).into(prfileImage);
    //            }
    //        });
    //    }
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