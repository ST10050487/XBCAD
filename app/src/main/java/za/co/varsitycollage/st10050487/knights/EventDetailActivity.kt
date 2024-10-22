package za.co.varsitycollage.st10050487.knights

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EventDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        val eventName = findViewById<TextView>(R.id.event_name)
        val eventDate = findViewById<TextView>(R.id.event_date)
        val eventLocation = findViewById<TextView>(R.id.event_location)
        val eventImage = findViewById<ImageView>(R.id.event_image)
        val eventPrice = findViewById<TextView>(R.id.event_price_amount)
        val eventAboutDescription = findViewById<TextView>(R.id.event_about_description)
        val eventDescription = findViewById<TextView>(R.id.event_description)

        // Hardcoded event details
        eventName.text = "Nineties vs Noughties"
        eventDate.text = "Fri, 2 Jun 18:00 PM - 20:30 PM"
        eventLocation.text = "Barnyard Theatre"
        eventImage.setImageResource(R.drawable.event_image)
        eventPrice.text = "R155.00"
        eventAboutDescription.text = "Nineties vs. Noughties is a popular song roller coaster with a skilled ten-piece cast that resurrects pop and rock icons from both decades."
        eventDescription.text = "This show is an exhilarating ride through two decades of non-stop hits. Whether you're a fan of the 1990s or 2000s - you are bound to be rocked, dance till you drop, sing out loud and be proud of the decade you love most!"
    }
}