package za.co.varsitycollage.st10050487.knights

import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class EventAdapter(
    private val events: MutableList<EventModel>,
    private val showDeleteMenu: (Boolean) -> Unit,
    private val updateDeleteButton: (Int) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
    private val itemSelectedList = mutableListOf<EventModel>()
    private var isSelectionMode = false
    private var isLongPressHandled = false

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventName: TextView = itemView.findViewById(R.id.txtTitle)
        val eventDate: TextView = itemView.findViewById(R.id.txtDate)
        val eventLocation: TextView = itemView.findViewById(R.id.txtLocation)
        var checkBox: CheckBox = itemView.findViewById(R.id.checkDel)
        val eventImage: ImageView = itemView.findViewById(R.id.event_image) // Add this line
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_event_card, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.eventName.text = event.eventName
        holder.eventDate.text = formatEventDate(event.eventDate ?: "", event.eventTime ?: "")
        holder.eventLocation.text = event.eventLocation

        // Decode and set the image
        event.eventPicture?.let { imageBytes ->
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            holder.eventImage.setImageBitmap(bitmap)
        } // Set a default image if none is provided

        // Set up the edit button click listener
        holder.itemView.findViewById<Button>(R.id.btnEdit).setOnClickListener {
            val intent = Intent(holder.itemView.context, ModifyEvent::class.java)
            intent.putExtra("EVENT_ID", event.eventId)
            holder.itemView.context.startActivity(intent)
        }

        // Set up the item click listener
        holder.itemView.setOnClickListener {
            if (isSelectionMode) {
                selectEvent(holder, event)
            } else {
                val intent = Intent(holder.itemView.context, EventDetailActivity::class.java)
                intent.putExtra("EVENT_ID", event.eventId)
                holder.itemView.context.startActivity(intent)
            }
        }

        // Handle long click for selection mode
        holder.itemView.setOnLongClickListener {
            if (!isLongPressHandled) {
                isLongPressHandled = true
                if (!isSelectionMode) {
                    isSelectionMode = true
                    showAllCheckBoxes()
                }
                selectEvent(holder, event)
            }
            true
        }

        // Show or hide the checkbox based on selection mode
        holder.checkBox.visibility = if (isSelectionMode) View.VISIBLE else View.GONE

        // Set the checkbox state
        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = event.selected
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            selectEvent(holder, event)
        }
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
        updateDeleteButton(itemSelectedList.size)
        notifyItemChanged(holder.adapterPosition)
        Toast.makeText(
            holder.itemView.context,
            "Selected event: ${event.eventName}",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showAllCheckBoxes() {
        for (event in events) {
            event.selected = false
        }
        notifyDataSetChanged()
    }

    fun getSelectedEvents(): List<EventModel> {
        return itemSelectedList
    }

    fun removeSelectedEvents() {
        events.removeAll(itemSelectedList)
        itemSelectedList.clear()
        isSelectionMode = false
        isLongPressHandled = false
        updateDeleteButton(0)
        notifyDataSetChanged()
    }

    fun clearSelection() {
        for (event in events) {
            event.selected = false
        }
        itemSelectedList.clear()
        isSelectionMode = false
        isLongPressHandled = false
        updateDeleteButton(0)
        notifyDataSetChanged()
    }
}