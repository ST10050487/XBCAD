package za.co.varsitycollage.st10050487.knights

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import android.os.Handler
import android.os.Looper
import android.widget.Button
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.*

class DisplaySportsFixturesActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: FixturesAdapter
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval = 60000L // 1 minute

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_sports_fixtures)

        tabLayout = findViewById(R.id.tabs)
        viewPager = findViewById(R.id.viewPager)
        val backButton: Button = findViewById(R.id.backButton)

        // Set up the back button to navigate to the home screen
        backButton.setOnClickListener {
            val intent = Intent(this, HomeScreen::class.java)
            startActivity(intent)
            finish()
        }

        try {
            // Fetch fixtures from the database
            val dbHelper = DBHelper(this)
            val upcomingFixtures = dbHelper.getUpcomingFixtures()
            val pastFixtures = dbHelper.getPastFixtures()

            // Combine fixtures into a single list
            val allFixtures = upcomingFixtures + pastFixtures

            // Set up the adapter
            adapter = FixturesAdapter(allFixtures)
            viewPager.adapter = adapter

            // Link the TabLayout and ViewPager2
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = if (position == 0) "Upcoming" else "Past Matches"
            }.attach()

            // Start periodic update
            handler.post(updateFixturesRunnable)
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the exception (e.g., show a toast or log the error)
        }
    }

    private val updateFixturesRunnable = object : Runnable {
        override fun run() {
            updateFixtures()
            handler.postDelayed(this, updateInterval)
        }
    }

    private fun updateFixtures() {
        try {
            val dbHelper = DBHelper(this)
            val currentTime = Calendar.getInstance().time
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

            // Check upcoming fixtures
            val upcomingFixtures = dbHelper.getUpcomingFixtures()
            for (fixture in upcomingFixtures) {
                val fixtureTime = dateFormat.parse(fixture.startTime)
                if (fixtureTime != null && fixtureTime.before(currentTime)) {
                    // Move to past fixtures
                    dbHelper.moveFixtureToPast(fixture)
                }
            }

            // Refresh the adapter data
            val newUpcomingFixtures = dbHelper.getUpcomingFixtures()
            val newPastFixtures = dbHelper.getPastFixtures()
            val allFixtures = newUpcomingFixtures + newPastFixtures

            adapter = FixturesAdapter(allFixtures)
            viewPager.adapter = adapter
            adapter.notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle the exception (e.g., show a toast or log the error)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateFixturesRunnable)
    }

}