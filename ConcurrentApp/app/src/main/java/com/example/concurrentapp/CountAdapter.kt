package com.example.concurrentapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CountAdapter : RecyclerView.Adapter<CountAdapter.CountViewHolder>() {

    private val counts = mutableListOf<Int>()

    fun addCount(count: Int) {
        counts.add(count)
        notifyItemInserted(counts.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return CountViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountViewHolder, position: Int) {
        holder.bind(counts[position])
    }

    override fun getItemCount(): Int = counts.size

    class CountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(count: Int) {
            (itemView as TextView).text = count.toString()
        }
    }
}
