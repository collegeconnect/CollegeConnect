package com.college.collegeconnect.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.college.collegeconnect.R
import com.college.collegeconnect.datamodels.Upload

class MyUploadedFilesAdapter(val context: Context, private val uploadList:ArrayList<Upload>):RecyclerView.Adapter<MyUploadedFilesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_files_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = uploadList[position].name
    }

    override fun getItemCount() = uploadList.size

    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        val textView:TextView = itemView.findViewById(R.id.subject_title)
    }
}