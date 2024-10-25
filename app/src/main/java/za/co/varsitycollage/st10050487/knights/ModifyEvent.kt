package za.co.varsitycollage.st10050487.knights

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText

class ModifyEvent : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_modify_event)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DBHelper(this)

        // Get the passed event ID
        val eventId = intent.getIntExtra("EVENT_ID", -1)
        if (eventId != -1) {
            displayEventDetails(eventId)
        }
    }

    private fun displayEventDetails(eventId: Int) {
        val event = dbHelper.getEventDetails(eventId)
        event?.let {
            findViewById<ImageView>(R.id.eventImage).setImageBitmap(BitmapFactory.decodeByteArray(it.pictures, 0, it.pictures.size))
            findViewById<TextInputEditText>(R.id.eventName).setText(it.eventName)
            findViewById<TextInputEditText>(R.id.eventLocation).setText(it.eventLocation)
            findViewById<TextInputEditText>(R.id.time).setText(it.eventTime)
            findViewById<TextInputEditText>(R.id.date).setText(it.eventDate)
            findViewById<TextInputEditText>(R.id.description).setText(it.eventDescription)
        }
    }
    // A method to update the event details
    fun updateEventDetails() {
    val eventId = intent.getIntExtra("EVENT_ID", -1)
    if (eventId != -1) {
        val eventName = findViewById<TextInputEditText>(R.id.eventName).text.toString()
        val eventLocation = findViewById<TextInputEditText>(R.id.eventLocation).text.toString()
        val eventTime = findViewById<TextInputEditText>(R.id.time).text.toString()
        val eventDate = findViewById<TextInputEditText>(R.id.date).text.toString()
        val eventDescription = findViewById<TextInputEditText>(R.id.description).text.toString()
        val eventPictures = dbHelper.getEventDetails(eventId)?.pictures ?: byteArrayOf() // Fetch existing pictures

        val updatedEvent = EventModel(
            eventId = eventId,
            eventName = eventName,
            eventDate = eventDate,
            eventTime = eventTime,
            eventLocation = eventLocation,
            eventPrice = 0.0, // Set appropriate price
            pictures = eventPictures,
            eventDescription = eventDescription,
            selected = false
        )

        dbHelper.updateEventDetails(updatedEvent)
        finish()
    }
}
}