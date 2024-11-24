package za.co.varsitycollage.st10050487.knights

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import za.co.varsitycollage.st10050487.knights.databinding.ActivityEventDetailBinding
import java.text.SimpleDateFormat

class EventDetailActivity : AppCompatActivity() {

    private lateinit var btnBack: androidx.appcompat.widget.Toolbar
    private lateinit var dbHelper: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        dbHelper = DBHelper(this)



        btnBack = findViewById(R.id.toolbar)
        setSupportActionBar(btnBack)
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //supportActionBar?.setDisplayShowHomeEnabled(true)

        btnBack.setNavigationOnClickListener {
            onBackPressed()
        }

        // Get the event name from the intent
        val eventName = intent.getStringExtra("EVENT_NAME")

        // Retrieve the event from the database using the event name
        val eventId = intent.getIntExtra("EVENT_ID", -1)
        val event = dbHelper.getEventById(eventId)
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
            eventPriceTextView.text = "R${event.eventPrice}"
            eventAboutDescription.text = event.eventDescription
            eventDescriptionTextView.text = event.eventDescription

            val inputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val outputDateFormat = SimpleDateFormat("EEE, d MMM HH:mm a")
            val eventDateTime = "${event.eventDate} ${event.eventTime}"
            val date = inputDateFormat.parse(eventDateTime)
            val formattedDate = outputDateFormat.format(date)

            eventDateTextView.text = formattedDate

            event.eventPicture?.let {
                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                if (bitmap != null) {
                    eventImage.setImageBitmap(bitmap)
                } else {
                    eventImage.setImageResource(R.drawable.event_image)
                }
            }
        } else {
            Toast.makeText(this, "Event not found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}