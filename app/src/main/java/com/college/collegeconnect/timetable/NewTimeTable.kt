package com.college.collegeconnect.timetable

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.RingtoneManager
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import androidx.work.*
import com.college.collegeconnect.R
import com.college.collegeconnect.activities.Navigation
import com.college.collegeconnect.adapters.SectionsPagerAdapter
import com.college.collegeconnect.database.AttendanceDatabase
import com.college.collegeconnect.database.entity.*
import com.college.collegeconnect.databinding.ActivityNewTimeTableBinding
import com.college.collegeconnect.utils.toast
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputLayout
import com.sample.viewbinding.activity.viewBinding
import java.text.SimpleDateFormat
import java.util.*

class NewTimeTable : AppCompatActivity() {

    lateinit var newTimeTableViewModel: NewTimeTableViewModel
    private val binding: ActivityNewTimeTableBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_time_table)

        newTimeTableViewModel = ViewModelProvider(this).get(NewTimeTableViewModel::class.java)
        //Setup viewpager and tablayout
        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager, newTimeTableViewModel)
        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs = findViewById<TabLayout>(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        binding.timeTableFab.setOnClickListener {
            add_class()
        }
    }

    private fun add_class() {

        var startTime: String? = "08:00:00"
        var endTime: String? = "09:00:00"
        var startTimeShow: String? = "08:00 AM"
        var endTimeShow: String? = "09:00 AM"

        val builder = AlertDialog.Builder(this)
        val inflater = (this as AppCompatActivity).layoutInflater
        val view = inflater.inflate(R.layout.add_class_layout, null)

        var spinner = view.findViewById<Spinner>(R.id.spinner2)
        var adapter: ArrayAdapter<String>
        AttendanceDatabase(this).getAttendanceDao().getSubjects().observe(this, androidx.lifecycle.Observer {
            adapter = ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, it)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        })

        val roomNumber = view.findViewById<TextInputLayout>(R.id.roomNumber)
        val start = view.findViewById<TextView>(R.id.start_time)
        val end = view.findViewById<TextView>(R.id.end_time)

        start.setOnClickListener {

            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
            val minute = mcurrentTime[Calendar.MINUTE]
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(this, { timePicker, selectedHour, selectedMinute ->
                val am_pm = if (selectedHour < 12) "AM" else "PM"
                val hour = if (selectedHour > 12) {
                    val temp = selectedHour - 12
                    if (temp < 10)
                        "0$temp"
                    else
                        temp.toString()

                } else {
                    if (selectedHour < 10)
                        "0$selectedHour"
                    else
                        selectedHour.toString()
                }
                val min = if (selectedMinute < 10)
                    "0$selectedMinute"
                else
                    selectedMinute.toString()

                val hourStore = if (selectedHour < 10)
                    "0$selectedHour"
                else
                    selectedHour.toString()

                start.text = "$hour:$min $am_pm"
                startTimeShow = "$hour:$min $am_pm"
                startTime = "$hourStore:$min:00"

            }, hour, minute, false) //Yes 24 hour time
            mTimePicker.show()
        }

        end.setOnClickListener {

            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
            val minute = mcurrentTime[Calendar.MINUTE]
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(this, { timePicker, selectedHour, selectedMinute ->
                val am_pm = if (selectedHour < 12) "AM" else "PM"
                val hour = if (selectedHour > 12) {
                    val temp = selectedHour - 12
                    if (temp < 10)
                        "0$temp"
                    else
                        temp.toString()

                } else {
                    if (selectedHour < 10)
                        "0$selectedHour"
                    else
                        selectedHour.toString()
                }
                val min = if (selectedMinute < 10)
                    "0$selectedMinute"
                else
                    selectedMinute.toString()

                val hourStore = if (selectedHour < 10)
                    "0$selectedHour"
                else
                    selectedHour.toString()

                end.text = "$hour:$min $am_pm"
                endTimeShow = "$hour:$min $am_pm"
                endTime = "$hourStore:$min:00"

            }, hour, minute, false) //Yes 24 hour time
            mTimePicker.show()
        }

        builder.setView(view)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        view.findViewById<Button>(R.id.btn_schedule_class).setOnClickListener {
            if (spinner.adapter.count == 0) {
                toast("No subject added!")
                return@setOnClickListener
            }
            startTimeShow?.let { sts -> endTimeShow?.let { ets -> newTimeTableViewModel.addItem(spinner.selectedItem.toString(), startTime.toString(), sts, endTime.toString(), ets, binding.viewPager.currentItem, roomNumber.editText?.text.toString()) } }
            dialog.dismiss()
        }
        dialog.show()
    }

    //-----------Testing Notification------SJ--
    fun alert(endTime: String){
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlertReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_IMMUTABLE)
//        val alarmClockInfo = AlarmManager.AlarmClockInfo(getMilli("${SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())} $endTime"), pendingIntent)
        val intent2 = Intent()
        intent2.putExtra("Class", "Added")
        setResult(10, intent2)
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, getMilli("${SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())} $endTime"), pendingIntent)
    }

//    fun scheduleNotification(timeDelay: String, tag: String, body: String) {
//
//        val time = getMilli("${SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())} $timeDelay")
//        val data = Data.Builder().putString("body", body)
//
//        val work = OneTimeWorkRequestBuilder<NotificationSchedule>()
//                .setInitialDelay(time, java.util.concurrent.TimeUnit.MILLISECONDS)
//                .setConstraints(Constraints.Builder().setTriggerContentMaxDelay(1000, java.util.concurrent.TimeUnit.MILLISECONDS).build()) // API Level 24
//                .setInputData(data.build())
//                .addTag(tag)
//                .build()
//
//        WorkManager.getInstance(this).enqueue(work)
//    }

    class NotificationSchedule(var context: Context, var params: WorkerParameters) : Worker(context, params) {

        override fun doWork(): Result {
            val data = params.inputData
            val title = "Title"
            val body = data.getString("body")

            TriggerNotification(context, title, body.toString())

            return Result.success()
        }
    }

    fun createNotification() {

        // Create an explicit intent for an Activity in your app
        val intent = Intent(application, Navigation::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(application, 0, intent,
            PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(application, "Notification")
                .setSmallIcon(R.mipmap.ic_stat_call_white)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.mipmap.icon_round))
                .setContentTitle("title")
                .setContentText("body")
                .setColor(Color.parseColor("#138FF7"))
                .setContentIntent(pendingIntent)
                .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setLights(Color.WHITE, 500, 500)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(
                        NotificationCompat.BigTextStyle()
                                .bigText("long notification content")
                )
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        with(NotificationManagerCompat.from(application)) {
            // notificationId is a unique int for each notification that you must define
            if (ActivityCompat.checkSelfPermission(
                    this@NewTimeTable,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(1, builder.build())
        }
    }

    // get time in milliseconds
    private fun getMilli(myDate: String): Long {
        val format = SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault())
        val d = format.parse(myDate)
        return d.time
    }

    //-----------Testing Notification------SJ--
}