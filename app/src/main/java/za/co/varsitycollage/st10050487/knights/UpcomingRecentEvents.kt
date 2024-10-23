package za.co.varsitycollage.st10050487.knights

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UpcomingRecentEvents : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper // Declare the DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upcoming_recent_events)

        dbHelper = DBHelper(this) // Initialize the DBHelper


        SettingTheCurrentDate()

        // Retrieve events from the database
        val events = dbHelper.getAllEvents() // Get the list of events

        val eventsContainer = findViewById<LinearLayout>(R.id.events_container)
        for (event in events) {
            val eventView = layoutInflater.inflate(R.layout.event_item, eventsContainer, false)

            val eventName = eventView.findViewById<TextView>(R.id.event_name)
            val eventDate = eventView.findViewById<TextView>(R.id.event_date)
            val eventLocation = eventView.findViewById<TextView>(R.id.event_location)
            val eventImage = eventView.findViewById<ImageView>(R.id.event_image)

            eventName.text = event.eventName
            eventDate.text = event.eventDate
            eventLocation.text = event.eventLocation

            // Set the image from the byte array if available
            event.picture?.let {
                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                eventImage.setImageBitmap(bitmap)
            } ?: eventImage.setImageResource(R.drawable.event_image) // Set a default image if none exists

            eventView.setOnClickListener {
                val intent = Intent(this, EventDetailActivity::class.java).apply {
                    putExtra("EVENT_NAME", event.eventName)
                    putExtra("EVENT_DATE", event.eventDate)
                    putExtra("EVENT_LOCATION", event.eventLocation)
                    putExtra("EVENT_IMAGE", event.picture) // Pass the picture byte array
                }
                startActivity(intent)
            }

            eventsContainer.addView(eventView)
        }
    }

    private fun SettingTheCurrentDate() {
        // Set the current date in the specified format
        val currentDateTextView = findViewById<TextView>(R.id.CurrentDate)
        val currentDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date())
        currentDateTextView.text = currentDate
    }
}