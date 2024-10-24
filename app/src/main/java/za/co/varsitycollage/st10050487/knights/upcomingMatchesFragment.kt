package za.co.varsitycollage.st10050487.knights

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class upcomingMatchesFragment : Fragment() {

    private var fixtureId: Long = 6// Retrieve the static fixture ID

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_upcoming_matches, container, false)

        // Find views by their IDs
        val fixtureDate = view.findViewById<TextView>(R.id.fixture_date)
        val team1Logo = view.findViewById<ImageView>(R.id.team1_logo)
        val team1Name = view.findViewById<TextView>(R.id.team1_name)
        val fixtureTime = view.findViewById<TextView>(R.id.fixture_time)
        val fixtureDateBox = view.findViewById<TextView>(R.id.fixture_date_box)
        val team2Logo = view.findViewById<ImageView>(R.id.team2_logo)
        val team2Name = view.findViewById<TextView>(R.id.team2_name)
        val matchType = view.findViewById<TextView>(R.id.match_type)
        val ageGroup = view.findViewById<TextView>(R.id.age_group)

        // Create an instance of your database helper
        val dbHelper = DBHelper(requireContext())

        // Retrieve fixture details using the fixture ID
        val fixture = dbHelper.getFixtureDetails(fixtureId.toInt())

        if (fixture != null) {
            // Update the UI with the retrieved data
            fixtureDate.text = fixture.matchDate // Use matchDate
            fixture.homeLogo?.let {
                team1Logo.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
            }
            team1Name.text = fixture.homeTeam
            fixtureTime.text = fixture.matchTime // Use matchTime
            fixtureDateBox.text = fixture.matchDate // Use matchDate
            fixture.awayLogo?.let {
                team2Logo.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
            }
            team2Name.text = fixture.awayTeam
            matchType.text = fixture.sport
            ageGroup.text = fixture.ageGroup
        } else {
            // Handle the case where no fixture is found
            Log.e("UpcomingMatchesFragment", "No fixture found for ID: $fixtureId")
        }

        return view
    }
}