package com.college.collegeconnect.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.college.collegeconnect.BuildConfig
import com.college.collegeconnect.R
import com.college.collegeconnect.activities.PdfViewerActivity
import com.college.collegeconnect.database.entity.DownloadEntity
import com.college.collegeconnect.settingsActivity.models.MyFilesViewModel
import java.io.File

class MyDownloadedFilesAdapter(val context: Context, private val arrayList: List<DownloadEntity>, private val myFilesViewModel: MyFilesViewModel):RecyclerView.Adapter<MyDownloadedFilesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_notes, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.fileName.text = arrayList[position].docName
        holder.authName.text = arrayList[position].authName
        holder.unit.text = arrayList[position].unit
        val file = File("/storage/emulated/0/Android/data/" + BuildConfig.APPLICATION_ID + "/files/Notes/Download Notes" + File.separator + arrayList[position].docName + ".pdf")
        holder.itemView.setOnClickListener {
            if(file.exists()){
                openfile(file.absolutePath)
            }
        }

        holder.itemView.setOnLongClickListener{
            if(file.exists()){
                file.delete()
                return@setOnLongClickListener true
            }
            return@setOnLongClickListener false
        }

        holder.report.setOnClickListener {
            val popup = PopupMenu(context, it)
            val inflater = popup.menuInflater
            inflater.inflate(R.menu.notes_overflow, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.delete -> {
                        file.delete()
                        myFilesViewModel.deleteDownload(arrayList[position].id)
                    }
                }
                return@setOnMenuItemClickListener true
            }
            popup.menu.findItem(R.id.tagover).isVisible = false
            popup.menu.findItem(R.id.details).isVisible = false
            popup.menu.findItem(R.id.report).isVisible = false
        }
        holder.report.visibility = View.GONE
        holder.download.visibility = View.GONE
    }
    fun openFile(path: String) {
        val uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", File(path))
        //        Log.d("Upload", "openfile:uri being sent in intent "+uri+"\n Actual path: "+uri);
        context.applicationContext.grantUriPermission(context.packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        Log.d("Upload", "openfile: $path")
        intent.setDataAndType(uri, "application/pdf")
        context.startActivity(intent)
    }
    fun openfile(path: String) {
//        val uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", File(path))
        //        Log.d("Upload", "openfile:uri being sent in intent "+uri+"\n Actual path: "+uri);
//        context.applicationContext.grantUriPermission(context.packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val bundle = Bundle()
        bundle.putString("file", path)
        val intent = Intent(context, PdfViewerActivity::class.java).apply {
            putExtras(bundle)
        }

        //        intent.setAction(Intent.ACTION_VIEW);
//        intent.putExtra(Intent.EXTRA_STREAM, uri);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Log.d("Upload", "openfile: $path")
//        intent.setDataAndType(uri, "application/pdf")
        (context as Activity).startActivity(intent)
    }

    override fun getItemCount() = arrayList.size

    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {

        val fileName:TextView = itemView.findViewById(R.id.title)
        val authName:TextView = itemView.findViewById(R.id.authorname)
        val unit:TextView = itemView.findViewById(R.id.unitText)
        val report:ImageView = itemView.findViewById(R.id.reportButton)
        val download:TextView = itemView.findViewById(R.id.download)
    }
}