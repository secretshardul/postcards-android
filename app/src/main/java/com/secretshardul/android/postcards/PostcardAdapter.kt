package com.secretshardul.android.postcards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class PostcardAdapter: RecyclerView.Adapter<PostcardAdapter.ViewHolder>() {
    var data =  listOf<PostcardModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val postcardTitle: TextView = itemView.findViewById(R.id.postcard_title)
        private val postcardBody: TextView = itemView.findViewById(R.id.postcard_body)
        private val postcardDate: TextView = itemView.findViewById(R.id.postcard_date)

        fun bind(item: PostcardModel) {
            postcardTitle.text = item.title
            postcardBody.text = item.body
            val simpleDateFormat = SimpleDateFormat("hh:mm a, dd MMM yyyy", Locale.getDefault())
            val timeText = simpleDateFormat.format(item.time)

            postcardDate.text = timeText
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.postcard_item_layout, parent, false)

                return ViewHolder(view)
            }
        }
    }
}
