package com.college.collegeconnect.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.college.collegeconnect.R
import com.college.collegeconnect.datamodels.Society
import com.college.collegeconnect.utils.toast
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class BvestSocietiesAdapter(val context: Context, val arrayList: ArrayList<Society>) : RecyclerView.Adapter<BvestSocietiesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_society_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val society = arrayList[position]

        holder.textView.text = society.name
        Picasso.get().load(society.image).into(holder.imageView)
    }

    override fun getItemCount() = arrayList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<CircleImageView>(R.id.societyImage)
        val textView = itemView.findViewById<TextView>(R.id.society_name)
    }
}