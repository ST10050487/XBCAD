package za.co.varsitycollage.st10050487.knights

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class pastMatchesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_past_matches, container, false)

        // Find views by their IDs
        val team1Logo = view.findViewById<ImageView>(R.id.team1_logo)
        val team1Name = view.findViewById<TextView>(R.id.team1_name)
        val team2Logo = view.findViewById<ImageView>(R.id.team2_logo)
        val team2Name = view.findViewById<TextView>(R.id.team2_name)
        val scoreText = view.findViewById<TextView>(R.id.score_text)
        val matchType = view.findViewById<TextView>(R.id.match_type)
        val ageGroup = view.findViewById<TextView>(R.id.age_group)
        val matchDate = view.findViewById<TextView>(R.id.match_date)

        // Set the text and image resources
        team1Logo.setImageResource(R.drawable.bosemansdamhig)
        team1Name.text = "BOSMANSDAM"
        team2Logo.setImageResource(R.drawable.egmeadhigh)
        team2Name.text = "EGMEAD"
        scoreText.text = "22 : 19"
        matchType.text = "Rugby"
        ageGroup.text = "Under 17's"
        matchDate.text = "17 July 2024"

        return view
    }
}