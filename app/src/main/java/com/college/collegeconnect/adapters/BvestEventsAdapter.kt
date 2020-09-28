package com.college.collegeconnect.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.college.collegeconnect.R
import com.college.collegeconnect.datamodels.Events
import com.college.collegeconnect.ui.event.bvest.BvestActivity
import com.college.collegeconnect.ui.event.bvest.BvestEventActivity
import com.college.collegeconnect.utils.ImageHandler
import com.college.collegeconnect.utils.toast
import com.facebook.shimmer.ShimmerFrameLayout
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class BvestEventsAdapter(val context: Context, val arrayList: ArrayList<Events>) : RecyclerView.Adapter<BvestEventsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_grid_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = arrayList[position]

        holder.textView.text = event.eventName

        val target = object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                holder.imageView.setImageBitmap(bitmap)
                holder.shimmer.stopShimmer()
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                Toast.makeText(context, e?.message, Toast.LENGTH_SHORT).show()
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                holder.shimmer.startShimmer()
            }
        }

        ImageHandler().getSharedInstance(context)?.load(event.imageUrl[0])?.into(target)
        holder.imageView.tag = target

        holder.date.text = date(event.date)

        //card click
        holder.itemView.setOnClickListener {
            val intent = Intent(context, BvestEventActivity::class.java)
            intent.putExtra("list", arrayList[position])
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    context as Activity,
                    Pair.create(holder.textView, "eventTitleTransition"),
                    Pair.create(holder.date, "eventDateTransition"),
                    Pair.create(holder.imageView, "eventBannerTransition")
            )
            context.startActivity(intent,options.toBundle())

        }
    }


    override fun getItemCount() = arrayList.size

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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.ivEventBannerCardImage)
        val textView = itemView.findViewById<TextView>(R.id.tvEventTitle)
        val date = itemView.findViewById<TextView>(R.id.tvEventDate)
        val shimmer: ShimmerFrameLayout = itemView.findViewById(R.id.ivEventBannerCard)
    }
}