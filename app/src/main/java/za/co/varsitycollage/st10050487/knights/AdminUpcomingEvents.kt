package za.co.varsitycollage.st10050487.knights

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class AdminUpcomingEvents : AppCompatActivity() {
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
    }
}