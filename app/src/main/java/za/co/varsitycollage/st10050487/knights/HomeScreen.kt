package za.co.varsitycollage.st10050487.knights

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
    private lateinit var eventAdapter: UpcomingEventInforAdptor
    private lateinit var dbHelper: DBHelper
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_screen)

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
                    // Handle the Home action
                }
                R.id.nav_events -> {
                    // Handle the Events action
                }
                R.id.nav_profile -> {
                    // Handle the Profile action
                }
//                R.id.nav_logout -> {
//                    // Handle the Logout action
//                }
            }
            true
        }

        dbHelper = DBHelper(this)
        recyclerView = findViewById(R.id.events_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val eventList = dbHelper.getAllEvents()
        if (eventList.isNotEmpty()) {
            eventAdapter = UpcomingEventInforAdptor(eventList)
            recyclerView.adapter = eventAdapter
            Log.d("HomeScreen", "RecyclerView adapter set with ${eventList.size} items.")
        } else {
            Log.d("HomeScreen", "No events found.")
        }

        // Displaying the current date
        val dateTextView: TextView = findViewById(R.id.date)
        // Update the date format to "dd MMMM yyyy" (e.g., "20 November 2024")
        val currentDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date())
        dateTextView.text = currentDate

        // Displaying the current school term
        val termTextView: TextView = findViewById(R.id.term)
        termTextView.text = getCurrentTerm()
    }

    private fun ExtractingFragmentContainer() {
        // Load the SportsFixturesHomeScreen fragment into the container
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(
            R.id.fixtures_fragment_container,
            SportsFixturesHomeScreenFragment()
        )
        fragmentTransaction.commit()
    }

    // A method to determine the current school term
    private fun getCurrentTerm(): String {
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return when {
            (month == Calendar.JANUARY && day >= 15) || (month == Calendar.FEBRUARY) || (month == Calendar.MARCH && day <= 31) -> "Term 1"
            (month == Calendar.APRIL) || (month == Calendar.MAY) || (month == Calendar.JUNE && day <= 15) -> "Term 2"
            (month == Calendar.JULY) || (month == Calendar.AUGUST) || (month == Calendar.SEPTEMBER && day <= 30) -> "Term 3"
            (month == Calendar.OCTOBER) || (month == Calendar.NOVEMBER) || (month == Calendar.DECEMBER && day <= 15) -> "Term 4"
            else -> "Unknown Term"
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}