package za.co.varsitycollage.st10050487.knights

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class pastMatchesFragment(private val isAdmin: Boolean = false) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_past_matches, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshData()
    }

    private fun refreshData() {
        val linearLayout = view?.findViewById<LinearLayout>(R.id.linear_layout)
        linearLayout?.removeAllViews() // Clear existing views

        // Hardcoded past match data
        val pastMatches = listOf(
            Match(
                date = "17 July 2024",
                team1Logo = R.drawable.bosemansdamhig,
                team1Name = "BOSMANSDAM",
                team2Logo = R.drawable.egmeadhigh,
                team2Name = "EGMEAD",
                score = "22 : 19",
                matchType = "Rugby",
                ageGroup = "Under 17's",
                matchDate = "17 July 2024"
            ),
        )

        if (pastMatches.isNotEmpty()) {
            for (match in pastMatches) {
                val matchCard = LayoutInflater.from(requireContext()).inflate(
                    if (isAdmin) R.layout.card_layout_admin_past else R.layout.card_layout_past,
                    linearLayout,
                    false
                )

                val team1Logo = matchCard.findViewById<ImageView>(R.id.team1_logo)
                val team1Name = matchCard.findViewById<TextView>(R.id.team1_name)
                val team2Logo = matchCard.findViewById<ImageView>(R.id.team2_logo)
                val team2Name = matchCard.findViewById<TextView>(R.id.team2_name)
                val scoreText = matchCard.findViewById<TextView>(R.id.score_text)
                val matchType = matchCard.findViewById<TextView>(R.id.match_type)
                val ageGroup = matchCard.findViewById<TextView>(R.id.age_group)
                val matchDate = matchCard.findViewById<TextView>(R.id.match_date)

                // Log null checks
                Log.d(
                    "pastMatchesFragment",
                    "Team 1 Name: ${team1Name != null}, Team 2 Name: ${team2Name != null}"
                )

                // Set the data for the match card
                team1Logo.setImageResource(match.team1Logo)
                team1Name?.text = match.team1Name ?: "Unknown Team"
                team2Logo.setImageResource(match.team2Logo)
                team2Name?.text = match.team2Name ?: "Unknown Team"
                scoreText?.text = match.score ?: "N/A"
                matchType?.text = match.matchType ?: "N/A"
                ageGroup?.text = match.ageGroup ?: "N/A"
                matchDate?.text = match.matchDate ?: "N/A"

                linearLayout?.addView(matchCard)
            }
        } else {
            // Show empty state if no matches
            val emptyStateImage = view?.findViewById<ImageView>(R.id.empty_state_image)
            emptyStateImage?.visibility = View.VISIBLE
        }
    }
}