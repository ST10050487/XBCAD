package za.co.varsitycollage.st10050487.knights

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class UpcomingRecentEvents : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upcoming_recent_events)

        val events = listOf(
            Events("Knights Golf Day", "Date: 12th August", "Time: 9:00 AM", "Location: Golf Course", 0.0,R.drawable.event_image),
            Events("Science Fair", "Date: 20th August", "Time: 10:00 AM", "Location: School Hall", 0.0,R.drawable.home_image)
        )

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
            eventImage.setImageResource(event.imageResId)

            eventView.setOnClickListener {
                val intent = Intent(this, EventDetailActivity::class.java).apply {
                    putExtra("EVENT_NAME", event.eventName)
                    putExtra("EVENT_DATE", event.eventDate)
                    putExtra("EVENT_LOCATION", event.eventLocation)
                    putExtra("EVENT_IMAGE", event.imageResId) // Update this as needed
                }
                startActivity(intent)
            }

            eventsContainer.addView(eventView)
        }
    }
}