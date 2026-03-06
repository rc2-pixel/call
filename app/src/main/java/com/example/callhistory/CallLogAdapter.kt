package com.example.callhistory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class CallLogAdapter(private var items: List<CallLogEntry>) :
    RecyclerView.Adapter<CallLogAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvNumber: TextView = view.findViewById(R.id.tvNumber)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvDuration: TextView = view.findViewById(R.id.tvDuration)
        val tvCallType: TextView = view.findViewById(R.id.tvCallType)
        val ivIcon: ImageView = view.findViewById(R.id.ivCallTypeIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_call_log, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val context = holder.itemView.context

        holder.tvName.text = if (item.name.isNotEmpty()) item.name else item.number
        holder.tvNumber.text = if (item.name.isNotEmpty()) item.number else ""
        holder.tvDate.text = item.date
        holder.tvDuration.text = item.duration

        when (item.type) {
            CallType.INCOMING -> {
                holder.tvCallType.text = "着信"
                holder.tvCallType.setTextColor(ContextCompat.getColor(context, R.color.incoming_color))
                holder.ivIcon.setImageResource(R.drawable.ic_call_incoming)
                holder.ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.incoming_color))
            }
            CallType.OUTGOING -> {
                holder.tvCallType.text = "発信"
                holder.tvCallType.setTextColor(ContextCompat.getColor(context, R.color.outgoing_color))
                holder.ivIcon.setImageResource(R.drawable.ic_call_outgoing)
                holder.ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.outgoing_color))
            }
            CallType.MISSED -> {
                holder.tvCallType.text = "不在着信"
                holder.tvCallType.setTextColor(ContextCompat.getColor(context, R.color.missed_color))
                holder.ivIcon.setImageResource(R.drawable.ic_call_missed)
                holder.ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.missed_color))
            }
            CallType.REJECTED -> {
                holder.tvCallType.text = "拒否"
                holder.tvCallType.setTextColor(ContextCompat.getColor(context, R.color.rejected_color))
                holder.ivIcon.setImageResource(R.drawable.ic_call_missed)
                holder.ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.rejected_color))
            }
            CallType.UNKNOWN -> {
                holder.tvCallType.text = "不明"
                holder.tvCallType.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
                holder.ivIcon.setImageResource(R.drawable.ic_call_incoming)
            }
        }
    }

    override fun getItemCount() = items.size

    fun updateData(newItems: List<CallLogEntry>) {
        items = newItems
        notifyDataSetChanged()
    }
}
