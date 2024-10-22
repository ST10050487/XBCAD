package za.co.varsitycollage.st10050487.knights

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView

class upcomingMatchesFragment : Fragment() {

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

        // Set the text and image resources
        fixtureDate.text = "Friday, October 5, 2023"
        team1Logo.setImageResource(R.drawable.bosemansdamhig)
        team1Name.text = "BOSMANSDAM"
        fixtureTime.text = "10:00"
        fixtureDateBox.text = "5 OCT"
        team2Logo.setImageResource(R.drawable.egmeadhigh)
        team2Name.text = "EGMEAD"
        matchType.text = "Netball"
        ageGroup.text = "Under 17's"


        return view
    }
}