package za.co.varsitycollage.st10050487.knights

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView


class UpcomingEvent(private val eventList: List<EventModel>) :
    RecyclerView.Adapter<UpcomingEvent.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventImage: ImageView = itemView.findViewById(R.id.event_image)
        val eventName: TextView = itemView.findViewById(R.id.event_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_upcoming_event, parent, false)
        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentItem = eventList[position]
        if (currentItem.eventPicture.isNotEmpty()) {
            val bitmap = BitmapFactory.decodeByteArray(currentItem.eventPicture, 0, currentItem.eventPicture.size)
            holder.eventImage.setImageBitmap(bitmap)
        } else {
            holder.eventImage.setImageResource(R.drawable.ic_no_image_icon)
        }
        holder.eventName.text = currentItem.eventName
    }

    override fun getItemCount() = eventList.size
}