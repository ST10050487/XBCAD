package za.co.varsitycollage.st10050487.knights

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HomeScreen : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: UpcomingEvent
    private lateinit var dbHelper: DBHelper
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_screen)

        // Retrieve the ROLE_ID from the intent
        val roleId = intent.getIntExtra("ROLE_ID", -1)
        if (roleId == -1) {
            Log.e("HomeScreen", "ROLE_ID not found in intent")
            // Handle the case where the ROLE_ID is not found
        } else {
            Log.d("HomeScreen", "ROLE_ID: $roleId")
        }

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navView = findViewById<NavigationView>(R.id.nav_view)

        ViewCompat.setOnApplyWindowInsetsListener(drawerLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ExtractingFragmentContainer()

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
navView.setNavigationItemSelectedListener {
    when (it.itemId) {
        R.id.nav_home -> {
            val intent = Intent(this, HomeScreen::class.java)
            startActivity(intent)
        }
       R.id.nav_sport_fixtures -> {
            val intent = Intent(this, DisplaySportsFixturesActivity::class.java)
            startActivity(intent)
        }
        R.id.nav_events -> { val intent = Intent(this, UpcomingRecentEvents::class.java)
            startActivity(intent)
        }
        R.id.nav_shop -> { val intent = Intent(this, ViewProduct::class.java)
            startActivity(intent)
        }
        R.id.nav_profile -> { val intent = Intent(this, HomeScreen::class.java)
            startActivity(intent)
        }
        R.id.nav_logout -> { val intent = Intent(this, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
    true
}

        dbHelper = DBHelper(this)
        recyclerView = findViewById(R.id.events_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val eventList = dbHelper.getAllEvents()
        if (eventList.isNotEmpty()) {
            eventAdapter = UpcomingEvent(eventList)
            recyclerView.adapter = eventAdapter
            Log.d("HomeScreen", "RecyclerView adapter set with ${eventList.size} items.")

            // Adjust the RecyclerView height based on the number of events
            val layoutParams = recyclerView.layoutParams
            layoutParams.height = (163 * eventList.size).coerceAtMost(800) // max height limit
            recyclerView.layoutParams = layoutParams
        } else {
            Log.d("HomeScreen", "No events found.")
        }

        // Displaying the current date
        val dateTextView: TextView = findViewById(R.id.date)
        val currentDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date())
        dateTextView.text = currentDate

        // Displaying the current school term
        val termTextView: TextView = findViewById(R.id.term)
        termTextView.text = getCurrentTerm()
    }

    private fun ExtractingFragmentContainer() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(
            R.id.fixtures_fragment_container,
            SportsFixturesHomeScreenFragment()
        )
        fragmentTransaction.commit()
    }

    private fun getCurrentTerm(): String {
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return when {
            (month == Calendar.JANUARY && day >= 15) || (month == Calendar.FEBRUARY) || (month == Calendar.MARCH && day <= 31) -> "Term 1"
            (month == Calendar.APRIL) || (month == Calendar.MAY) || (month == Calendar.JUNE && day <= 15) -> "Term 2"
            (month == Calendar.JULY) || (month == Calendar.AUGUST) || (month == Calendar.SEPTEMBER && day <= 30) -> "Term 3"
            else -> "Term 4"
        }
    }
}
