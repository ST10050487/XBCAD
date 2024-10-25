package za.co.varsitycollage.st10050487.knights

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class upcomingMatchesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_upcoming_matches, container, false)

        // Find the LinearLayout that will hold all fixture cards
        val linearLayout = view.findViewById<LinearLayout>(R.id.linear_layout)

        // Create an instance of your database helper
        val dbHelper = DBHelper(requireContext())

        // Retrieve all fixtures sorted from latest to oldest
        val fixtures = dbHelper.getAllFixturesSorted()

        if (fixtures.isNotEmpty()) {
            for (fixture in fixtures) {
                // Inflate a new fixture card layout
                val fixtureCard =
                    inflater.inflate(R.layout.fragment_upcoming_matches, linearLayout, false)

                // Find views by their IDs in the fixture card
                val fixtureDate = fixtureCard.findViewById<TextView>(R.id.fixture_date)
                val team1Logo = fixtureCard.findViewById<ImageView>(R.id.team1_logo)
                val team1Name = fixtureCard.findViewById<TextView>(R.id.team1_name)
                val fixtureTime = fixtureCard.findViewById<TextView>(R.id.fixture_time)
                val fixtureDateBox = fixtureCard.findViewById<TextView>(R.id.fixture_date_box)
                val team2Logo = fixtureCard.findViewById<ImageView>(R.id.team2_logo)
                val team2Name = fixtureCard.findViewById<TextView>(R.id.team2_name)
                val matchType = fixtureCard.findViewById<TextView>(R.id.match_type)
                val ageGroup = fixtureCard.findViewById<TextView>(R.id.age_group)

                // Update the UI with the retrieved data
                fixtureDate.text = fixture.matchDate
                fixture.homeLogo?.let {
                    team1Logo.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
                }
                team1Name.text = fixture.homeTeam
                fixtureTime.text = fixture.matchTime
                fixtureDateBox.text = fixture.matchDate
                fixture.awayLogo?.let {
                    team2Logo.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
                }
                team2Name.text = fixture.awayTeam
                matchType.text = fixture.sport
                ageGroup.text = fixture.ageGroup

                // Add the inflated fixture card to the LinearLayout
                linearLayout.addView(fixtureCard)
            }
        } else {
            // Handle the case where no fixtures are found
            Log.e("UpcomingMatchesFragment", "No fixtures found in the database.")
        }

        return view
    }
}