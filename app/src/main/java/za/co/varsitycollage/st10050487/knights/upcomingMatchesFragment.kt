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
import java.text.SimpleDateFormat
import java.util.*

class upcomingMatchesFragment(private val isAdmin: Boolean = false) : Fragment() {
    private var selectedSports: List<String> = emptyList() // Store selected sports

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            selectedSports = it.getStringArrayList("selectedSports") ?: emptyList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_upcoming_matches, container, false)
        val linearLayout = view.findViewById<LinearLayout>(R.id.linear_layout)

        val dbHelper = DBHelper(requireContext())
        val fixtures = dbHelper.getAllFixtures()

        if (fixtures.isNotEmpty()) {
            for (fixture in fixtures) {
                // Check if no sports are selected, then show all fixtures
                if (selectedSports.isEmpty() || selectedSports.contains(fixture.sport)) {
                    // Inflate a new fixture card layout
                    val fixtureCard = LayoutInflater.from(requireContext()).inflate(
                        if (isAdmin) R.layout.card_layout_admin_upcoming else R.layout.card_layout_upcoming,
                        linearLayout,
                        false
                    )

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

                    FormattingDate(fixture, fixtureDate, fixtureDateBox)

                    fixture.homeLogo?.let {
                        team1Logo.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
                    }
                    team1Name.text = fixture.homeTeam
                    fixtureTime.text = fixture.matchTime
                    fixture.awayLogo?.let {
                        team2Logo.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
                    }
                    team2Name.text = fixture.awayTeam
                    matchType.text = fixture.sport
                    ageGroup.text = fixture.ageGroup

                    // Add the inflated fixture card to the LinearLayout
                    linearLayout.addView(fixtureCard)
                }
            }
        } else {
            Log.e("UpcomingMatchesFragment", "No fixtures found in the database.")
        }

        return view
    }

    private fun FormattingDate(
        fixture: FixtureModel,
        fixtureDate: TextView,
        fixtureDateBox: TextView
    ) {
        // Assuming fixture.matchDate is in a format like "dd-MM-yyyy"
        val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        // Output format for fixtureDate
        val outputFormat = SimpleDateFormat("EEEE -- MMMM dd", Locale.getDefault())
        // Output format for fixtureDateBox
        val outputFormatBox = SimpleDateFormat("dd MMMM", Locale.getDefault())

        try {
            val date = inputFormat.parse(fixture.matchDate)
            fixtureDate.text = outputFormat.format(date) // For fixtureDate
            fixtureDateBox.text = outputFormatBox.format(date) // For fixtureDateBox
        } catch (e: Exception) {
            Log.e("UpcomingMatchesFragment", "Error parsing date: ${e.message}")
            fixtureDate.text = fixture.matchDate // Fallback to original if parsing fails
            fixtureDateBox.text = fixture.matchDate // Fallback for fixtureDateBox
        }
    }
}