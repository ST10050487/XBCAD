package za.co.varsitycollage.st10050487.knights

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UpcomingEvent(private val eventList: List<EventModel>) :
    RecyclerView.Adapter<UpcomingEvent.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventName: TextView = itemView.findViewById(R.id.event_name)
        // Add other views for event details if needed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_upcoming_event, parent, false)
        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentItem = eventList[position]
        holder.eventName.text = currentItem.eventName
        // Bind other event details if needed
    }

    override fun getItemCount() = eventList.size
}