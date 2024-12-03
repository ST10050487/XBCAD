package za.co.varsitycollage.st10050487.knights

import android.content.Intent
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

    override fun onResume() {
        super.onResume()
        refreshData() // Refresh the data whenever the fragment comes into view
    }

    private fun refreshData() {
        val linearLayout = view?.findViewById<LinearLayout>(R.id.linear_layout)
        linearLayout?.removeAllViews() // Clear existing views

        val dbHelper = DBHelper(requireContext())
        val pastMatches = dbHelper.fetchPastFixtures() // Fetch past matches from the database

        if (pastMatches.isNotEmpty()) {
            for (fixture in pastMatches) {
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
                val eventEdit: ImageView? = if (isAdmin) {
                    matchCard.findViewById<ImageView>(R.id.event_edit)
                } else {
                    null
                }

                // Log null checks
                Log.d(
                    "pastMatchesFragment",
                    "Team 1 Name: ${team1Name != null}, Team 2 Name: ${team2Name != null}, Score Text: ${scoreText != null}, " +
                            "Match Type: ${matchType != null}, Age Group: ${ageGroup != null}"
                )

                // Set the data for the match card
                fixture.homeLogo?.let {
                    team1Logo.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
                }
                team1Name.text = fixture.homeTeam ?: "Unknown Team"
                fixture.awayLogo?.let {
                    team2Logo.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
                }
                team2Name.text = fixture.awayTeam ?: "Unknown Team"

                // Fetch scores from the TIMES table
                val scores = dbHelper.getScores(fixture.fixtureId) // Get scores based on fixture ID
                scoreText.text = if (scores != null) {
                    "${scores.homeScore} : ${scores.awayScore}"
                } else {
                    "Score not available"
                }

                matchType.text = fixture.sport ?: "N/A"
                ageGroup.text = fixture.ageGroup ?: "N/A"

                eventEdit?.setOnClickListener {
                    val intent = Intent(requireContext(), EditFixture::class.java)
                    intent.putExtra("fixture_id", fixture.fixtureId)
                    startActivity(intent)
                }

                linearLayout?.addView(matchCard)
            }
        } else {
            // Show empty state if no matches
            val emptyStateImage = view?.findViewById<ImageView>(R.id.empty_state_image)
            emptyStateImage?.visibility = View.VISIBLE
        }
    }
}