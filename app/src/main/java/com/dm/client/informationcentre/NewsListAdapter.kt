package com.dm.client.informationcentre

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dm.client.R

class NewsListAdapter(val list: ArrayList<NewsItem>) : RecyclerView.Adapter<NewsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_newsitem, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.title.text = list[position].heading
        holder.body.text = list[position].body
        holder.date.text = list[position].creationTime
        val priority = list[position].priority
        when (priority) {
            1 -> holder.prior.setBackgroundResource(R.drawable.bg_circle_priority_green)
            2 -> holder.prior.setBackgroundResource(R.drawable.bg_circle_priority_red)
        }

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val title = view.findViewById<TextView>(R.id.News_Headline)
        val body = view.findViewById<TextView>(R.id.News_Text)
        val date = view.findViewById<TextView>(R.id.Date_Time)
        val prior = view.findViewById<View>(R.id.Priority_Button)

    }


}