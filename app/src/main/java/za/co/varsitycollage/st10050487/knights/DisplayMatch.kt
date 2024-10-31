package za.co.varsitycollage.st10050487.knights

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DisplayMatch : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_match)

        dbHelper = DBHelper(this)

        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)

        // Set up the ViewPager with the sections adapter.
        val viewPagerAdapter = ViewPagerAdapter(this)
        viewPager.adapter = viewPagerAdapter

        // Link the TabLayout with the ViewPager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Player List"
                1 -> "Overview"
                else -> null
            }
        }.attach()

        // Get the fixture ID from the intent
        val fixtureId = intent.getIntExtra("FIXTURE_ID", -1)
        if (fixtureId != -1) {
            getFixtureDetailsById(fixtureId)
        }
    }

    private fun getFixtureDetailsById(fixtureId: Int) {
    val fixture = dbHelper.getFixtureDetails(fixtureId)
    fixture?.let {
        val matchLocation: TextView = findViewById(R.id.matchLocation)
        val sport: TextView = findViewById(R.id.sport)
        val homeTeamName: TextView = findViewById(R.id.home_team_name)
        val awayTeamName: TextView = findViewById(R.id.away_team_name)
        val score: TextView = findViewById(R.id.score)
        val matchStatus: TextView = findViewById(R.id.match_status)
        val date: TextView = findViewById(R.id.date)
        val time: TextView = findViewById(R.id.time)
        val venue: TextView = findViewById(R.id.venue)
        val homeTeamLogo: ImageView = findViewById(R.id.display_home_team_logo)
        val awayTeamLogo: ImageView = findViewById(R.id.display_away_team_logo)

        matchLocation.text = fixture.matchLocation
        sport.text = fixture.sport
        homeTeamName.text = fixture.homeTeam
        awayTeamName.text = fixture.awayTeam
        matchStatus.text = dbHelper.getMatchStatus(fixture.matchStatusId) ?: "No Status"
        date.text = "Date: ${fixture.matchDate}"
        time.text = "Time: ${fixture.matchTime}"
        venue.text = "Venue: ${fixture.matchLocation}"

        // Load logos if available
        fixture.homeLogo?.let {
            homeTeamLogo.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
        }
        fixture.awayLogo?.let {
            awayTeamLogo.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
        }

        // Fetch and display scores
        val scores = dbHelper.getScores(fixtureId)
        score.text = "${scores?.homeScore ?: 0} - ${scores?.awayScore ?: 0}"
    }
}
}
