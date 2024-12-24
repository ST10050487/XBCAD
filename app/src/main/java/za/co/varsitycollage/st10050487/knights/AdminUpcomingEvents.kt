package za.co.varsitycollage.st10050487.knights

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout // Import LinearLayout
import androidx.appcompat.app.AppCompatActivity

class AdminUpcomingEvents : AppCompatActivity() {
    private lateinit var searchBar: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_upcoming_events)

        // Find your Fragment Container
        val fragmentContainer = findViewById<FrameLayout>(R.id.fragment_container)

        // Create an instance of your fragment
        val eventListFragment = AdminEventListFragment()

        // Add the fragment to the container
        supportFragmentManager.beginTransaction()
            .add(fragmentContainer.id, eventListFragment)
            .commit()

        // Find the Create Event button
        val createEventButton = findViewById<Button>(R.id.create_event_button)

        // Set an OnClickListener on the button
        createEventButton.setOnClickListener {
            // Start the Create Event Activity
            val intent = Intent(this, CreateEvent::class.java)
            startActivity(intent)
        }

        // Initialize the Search Bar
        searchBar = findViewById(R.id.search_bar)

        // Set up the search functionality
        setupSearchFunctionality(eventListFragment)

        // Set up the back button functionality
        val backButton = findViewById<LinearLayout>(R.id.back_btn) // Find the back button layout
        backButton.setOnClickListener {
            // Start the AdminEvents activity
            val intent = Intent(this, AdminHome::class.java)
            startActivity(intent)
            finish() // Optional: Call finish() if you want to close this activity
        }
    }

    private fun setupSearchFunctionality(eventListFragment: AdminEventListFragment) {
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Pass the search query to the fragment
                eventListFragment.filterEvents(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}