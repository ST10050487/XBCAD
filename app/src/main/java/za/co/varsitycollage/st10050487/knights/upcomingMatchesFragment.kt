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
        val linearLayout = view.findViewById<LinearLayout>(R.id.linear_layout)

        val matches = listOf(
            Match(
                "Saturday - August 26",
                R.drawable.bosemansdamhig,
                "BOSMANSDAM",
                R.drawable.egmeadhigh,
                "EGMEAD HIGH",
                "22 : 19",
                "Netball",
                "Under 16's",
                "26 August"
            ),
            // Add more matches here
        )

        for (match in matches) {
            linearLayout.addView(createMatchCard(match))
        }

        return view
    }

    private fun createMatchCard(match: Match): View {
        val cardView = layoutInflater.inflate(R.layout.fragment_upcoming_matches, null)

        cardView.findViewById<TextView>(R.id.fixture_date).text = match.date
        cardView.findViewById<ImageView>(R.id.team1_logo).setImageResource(match.team1Logo)
        cardView.findViewById<TextView>(R.id.team1_name).text = match.team1Name
        cardView.findViewById<ImageView>(R.id.team2_logo).setImageResource(match.team2Logo)
        cardView.findViewById<TextView>(R.id.team2_name).text = match.team2Name
        cardView.findViewById<TextView>(R.id.fixture_time).text = match.score
        cardView.findViewById<TextView>(R.id.match_type).text = match.matchType
        cardView.findViewById<TextView>(R.id.age_group).text = match.ageGroup
        cardView.findViewById<TextView>(R.id.fixture_date_box).text = match.matchDate

        return cardView
    }
}