package za.co.varsitycollage.st10050487.knights

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import java.text.SimpleDateFormat
import java.util.*

class UpcomingRecentEvents : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private var userId: Int = -1
    private var roleId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upcoming_recent_events)

        // Getting the userId from the Intent
        userId = intent.getIntExtra("USER_ID", -1)
        roleId = intent.getIntExtra("ROLE_ID", -1)

        dbHelper = DBHelper(this)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView = findViewById<NavigationView>(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, HomeScreen::class.java)
                    intent.putExtra("USER_ID", userId)
                    intent.putExtra("ROLE_ID", roleId)
                    startActivity(intent)
                }
                R.id.nav_sport_fixtures -> {
                    val intent = Intent(this, DisplaySportsFixturesActivity::class.java)
                    intent.putExtra("USER_ID", userId)
                    intent.putExtra("ROLE_ID", roleId)
                    startActivity(intent)
                }
                R.id.nav_events -> {
                    val intent = Intent(this, UpcomingRecentEvents::class.java)
                    intent.putExtra("USER_ID", userId)
                    intent.putExtra("ROLE_ID", roleId)
                    startActivity(intent)
                }
                R.id.nav_shop -> {
                    val intent = Intent(this, ViewProduct::class.java)
                    intent.putExtra("USER_ID", userId)
                    intent.putExtra("ROLE_ID", roleId)
                    startActivity(intent)
                }
                R.id.nav_profile -> {
                    val intent = Intent(this, User::class.java)
                    intent.putExtra("USER_ID", userId)
                    intent.putExtra("ROLE_ID", roleId)
                    startActivity(intent)
                }
                R.id.nav_logout -> {
                    val intent = Intent(this, Login::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
            }
            true
        }

        NavigatingTohomeScreen()
        SettingTheCurrentDate()

        // Retrieve events from the database
        val events = dbHelper.getAllEvents()

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
            event.eventPicture?.let {
                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                eventImage.setImageBitmap(bitmap)
            } ?: eventImage.setImageResource(R.drawable.event_image) // Set a default image if none exists

            eventView.setOnClickListener {
                val intent = Intent(this, EventDetailActivity::class.java).apply {
                    putExtra("EVENT_NAME", event.eventName)
                    putExtra("EVENT_DATE", event.eventDate)
                    putExtra("EVENT_LOCATION", event.eventLocation)
                    putExtra("EVENT_IMAGE", event.eventPicture) // Pass the picture byte array
                }
                startActivity(intent)
            }

            eventsContainer.addView(eventView)
        }
    }

    override fun onResume() {
        super.onResume()
        displayUserDetails()
    }

    private fun displayUserDetails() {
        val user = dbHelper.getUser(userId)
        if (user != null) {
            val navView = findViewById<NavigationView>(R.id.nav_view)
            val headerView = navView.getHeaderView(0)
            val userNameTextView = headerView.findViewById<TextView>(R.id.user_name)
            val userEmailTextView = headerView.findViewById<TextView>(R.id.user_email)
            val userImageView = headerView.findViewById<ImageView>(R.id.user_image)

            userNameTextView.text = user.name + " " + user.surname
            userEmailTextView.text = user.email

            val profilePicture = user.profilePicture
            if (profilePicture != null && profilePicture.isNotEmpty()) {
                val bitmap = BitmapFactory.decodeByteArray(profilePicture, 0, profilePicture.size)
                userImageView.setImageBitmap(bitmap)
            } else {
                userImageView.setImageResource(R.drawable.user_icon) // Default image
            }
        }
    }
    private fun NavigatingTohomeScreen() {
        val backButton = findViewById<LinearLayout>(R.id.back_btn)
        backButton.setOnClickListener {
            val intent = Intent(this, HomeScreen::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun SettingTheCurrentDate() {
        val currentDateTextView = findViewById<TextView>(R.id.CurrentDate)
        val currentDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date())
        currentDateTextView.text = currentDate
    }
}