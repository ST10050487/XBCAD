package za.co.varsitycollage.st10050487.knights

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DisplaySportsFixturesActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: FixturesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_sports_fixtures)

        tabLayout = findViewById(R.id.tabs)
        viewPager = findViewById(R.id.viewPager)

        // Set up the adapter
        adapter = FixturesAdapter(this)

        // Set up the adapter
        adapter = FixturesAdapter(this)

        // Fetch fixtures from the database
        val dbHelper = DBHelper(this)
        val upcomingFixtures = dbHelper.getUpcomingFixtures()
        val pastFixtures = dbHelper.getPastFixtures()

        // Add upcoming fixtures to the adapter
        for (fixture in upcomingFixtures) {
            adapter.addFragment(
                fixtureName = "${fixture.team1} vs ${fixture.team2}",
                fixtureTime = fixture.startTime,
                fixtureDate = fixture.date,
                sportName = fixture.team1,
                sportCategory = "Category",
                title = "Upcoming"
            )
        }

        // Add past fixtures to the adapter
        for (fixture in pastFixtures) {
            adapter.addFragment(
                fixtureName = "${fixture.team1} vs ${fixture.team2}",
                fixtureTime = fixture.startTime,
                fixtureDate = fixture.date,
                sportName = fixture.team1,
                sportCategory = "Category",
                title = "Past Matches"
            )
        }

        viewPager.adapter = adapter

        // Link the TabLayout and ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = adapter.getPageTitle(position)
        }.attach()
    }
    private fun moveMatchToPast() {
        // Here you would typically check if the match is finished
        // For demonstration, we're just switching the tab
        viewPager.currentItem = 1 // Move to Past Matches tab
    }

}