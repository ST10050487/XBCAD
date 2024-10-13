package za.co.varsitycollage.st10050487.knights

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale


class EventAdapter(
    private val events: MutableList<EventModel>,
    private val showDeleteMenu: (Boolean) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
    private val itemSelectedList = mutableListOf<EventModel>()


    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventName: TextView = itemView.findViewById(R.id.txtTitle)
        val eventDate: TextView = itemView.findViewById(R.id.txtDate)
        val eventLocation: TextView = itemView.findViewById(R.id.txtLocation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_event_card, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.eventName.text = event.eventName
        holder.eventDate.text = formatEventDate(event.eventDate, event.eventTime)
        holder.eventLocation.text = event.eventLocation

        holder.itemView.findViewById<Button>(R.id.btnEdit).setOnClickListener {
            Toast.makeText(holder.itemView.context, "Edit button clicked for ${event.eventName}", Toast.LENGTH_SHORT).show()
        }

        holder.itemView.setOnLongClickListener {
            selectEvent(holder, event)
            Toast.makeText(holder.itemView.context, "Long pressed on ${event.eventName}", Toast.LENGTH_SHORT).show()
            true
        }

        holder.itemView.isSelected = event.selected
        holder.itemView.setBackgroundResource(if (event.selected) R.color.lightBlue else R.color.white )
        holder.itemView.focusable = View.FOCUSABLE
    }

    override fun getItemCount(): Int {
        return events.size
    }

    private fun formatEventDate(date: String, time: String): String {
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("EEE, MMM dd · hh.mm a", Locale.getDefault())
        val dateTimeString = "$date $time"
        val parsedDate = inputDateFormat.parse(dateTimeString)
        return if (parsedDate != null) {
            outputDateFormat.format(parsedDate)
        } else {
            "$date $time"
        }
    }

    private fun selectEvent(holder: EventViewHolder, event: EventModel) {
        if (event.selected) {
            itemSelectedList.remove(event)
            event.selected = false
        } else {
            itemSelectedList.add(event)
            event.selected = true
        }
        showDeleteMenu(itemSelectedList.isNotEmpty())
        notifyItemChanged(holder.adapterPosition)
        Toast.makeText(holder.itemView.context, "Selected event: ${event.eventName}", Toast.LENGTH_SHORT).show()
    }

    fun getSelectedEvents(): List<EventModel> {
        return itemSelectedList
    }
    fun removeSelectedEvents() {
        events.removeAll(itemSelectedList)
        itemSelectedList.clear()
        notifyDataSetChanged()
    }
}