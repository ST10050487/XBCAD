package za.co.varsitycollage.st10050487.knights

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.text.SimpleDateFormat
import java.util.*

class DisplaySportsFixturesActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: FixturesAdapter
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval = 60000L // 1 minute
    private var userId: Int = -1
    private var roleId: Int = -1
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_sports_fixtures)

        // Initialize dbHelper
        dbHelper = DBHelper(this)

        // Getting the userId from the Intent
        userId = intent.getIntExtra("USER_ID", -1)
        roleId = intent.getIntExtra("ROLE_ID", -1)

        tabLayout = findViewById(R.id.tabs)
        viewPager = findViewById(R.id.viewPager)
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

        if (roleId == -1) {
            Log.e("DisplaySportsFixturesActivity", "ROLE_ID not found in intent")
        } else {
            Log.d("DisplaySportsFixturesActivity", "ROLE_ID: $roleId")
        }

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

        // Displaying the current date
        val dateTextView: TextView = findViewById(R.id.date)
        val currentDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date())
        dateTextView.text = currentDate
    }

    private val updateFixturesRunnable = object : Runnable {
        override fun run() {
            updateFixtures()
            handler.postDelayed(this, updateInterval)
        }
    }

    private fun updateFixtures() {
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

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateFixturesRunnable)
    }
}