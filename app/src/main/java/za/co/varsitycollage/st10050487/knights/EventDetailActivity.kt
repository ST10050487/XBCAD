package za.co.varsitycollage.st10050487.knights

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EventDetailActivity : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper // Declare the DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        dbHelper = DBHelper(this) // Initialize the DBHelper

        // Get the event name from the intent
        val eventName = intent.getStringExtra("EVENT_NAME")

        // Retrieve the event from the database using the event name
        val event = dbHelper.getAllEvents().find { it.eventName == eventName }

        // Check if the event is found
        if (event != null) {
            val eventNameTextView = findViewById<TextView>(R.id.event_name)
            val eventDateTextView = findViewById<TextView>(R.id.event_date)
            val eventLocationTextView = findViewById<TextView>(R.id.event_location)
            val eventPriceTextView = findViewById<TextView>(R.id.event_price_amount)
            val eventAboutDescription = findViewById<TextView>(R.id.event_about_description)
            val eventDescriptionTextView = findViewById<TextView>(R.id.event_description)
            val eventImage = findViewById<ImageView>(R.id.event_image)

            // Set the event details
            eventNameTextView.text = event.eventName
            eventDateTextView.text = event.eventDate
            eventLocationTextView.text = event.eventLocation
            eventPriceTextView.text = event.eventPrice.toString()
            eventAboutDescription.text = event.eventDescription
            eventDescriptionTextView.text = event.eventDescription // Assuming you want to show the same description

            // Set the image from the byte array if available
            event.eventPicture?.let {
                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                eventImage.setImageBitmap(bitmap)
            } ?: eventImage.setImageResource(R.drawable.event_image) // Set a default image if none exists
        } else {
            // Handle the case where the event is not found (optional)
            // You might want to show a message or finish the activity
        }
    }
}