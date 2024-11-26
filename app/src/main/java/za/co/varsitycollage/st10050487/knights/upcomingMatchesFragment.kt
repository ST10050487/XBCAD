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
import java.text.SimpleDateFormat
import java.util.*

class upcomingMatchesFragment(private val isAdmin: Boolean = false) : Fragment() {
    private var selectedSports: List<String> = emptyList() // Store selected sports
    private var searchQuery: String = "" // Store search query

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            selectedSports = it.getStringArrayList("selectedSports") ?: emptyList()
            searchQuery = it.getString("searchQuery", "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_upcoming_matches, container, false)
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
        val dbHelper = DBHelper(requireContext())
        val linearLayout = view?.findViewById<LinearLayout>(R.id.linear_layout)
        val emptyStateImage = view?.findViewById<ImageView>(R.id.empty_state_image)
        linearLayout?.removeAllViews() // Clear existing views

        val fixtures = dbHelper.getAllFixtures() // Get updated fixtures

        if (fixtures.isNotEmpty()) {
            emptyStateImage?.visibility = View.GONE
            for (fixture in fixtures) {
                if ((selectedSports.isEmpty() || selectedSports.contains(fixture.sport)) &&
                    (searchQuery.isEmpty() || fixture.homeTeam.contains(
                        searchQuery,
                        true
                    ) || fixture.awayTeam.contains(searchQuery, true))
                ) {
                    val fixtureCard = LayoutInflater.from(requireContext()).inflate(
                        if (isAdmin) R.layout.card_layout_admin_upcoming else R.layout.card_layout_upcoming,
                        linearLayout,
                        false
                    )

                    val fixtureDate = fixtureCard.findViewById<TextView>(R.id.fixture_date)
                    val team1Logo = fixtureCard.findViewById<ImageView>(R.id.team1_logo)
                    val team1Name = fixtureCard.findViewById<TextView>(R.id.team1_name)
                    val fixtureTime = fixtureCard.findViewById<TextView>(R.id.fixture_time)
                    val fixtureDateBox = fixtureCard.findViewById<TextView>(R.id.fixture_date_box)
                    val team2Logo = fixtureCard.findViewById<ImageView>(R.id.team2_logo)
                    val team2Name = fixtureCard.findViewById<TextView>(R.id.team2_name)
                    val matchType = fixtureCard.findViewById<TextView>(R.id.match_type)
                    val ageGroup = fixtureCard.findViewById<TextView>(R.id.age_group)
                    val eventEdit: ImageView? = if (isAdmin) {
                        fixtureCard.findViewById<ImageView>(R.id.event_edit)
                    } else {
                        null
                    }

                    val matchStatusValue = fixture.matchStatusId // Assuming matchStatus is an Int
                    val matchStatusText = when (matchStatusValue) {
                        1 -> "First Half"
                        2 -> "Half Time"
                        3 -> "Second Half"
                        4 -> "Match Over"
                        5 -> "Cancelled"
                        else -> "Unknown Status"
                    }
                    fixtureDateBox.text =
                        matchStatusText // Set the match status text in fixture_date_box

                    // Update team1Name to include the match status
                    team1Name.text = "${fixture.homeTeam} - $matchStatusText"

                    // Log the current match status
                    Log.d(
                        "UpcomingMatchesFragment",
                        "Current match status for fixture ID ${fixture.fixtureId}: $matchStatusText"
                    )

                    FormattingDate(fixture, fixtureDate, fixtureDateBox)

                    fixture.homeLogo?.let {
                        team1Logo.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
                    }

                    // Set the fixture time text with home/away game indication
                    fixtureTime.text = if (fixture.isHomeGame) {
                        "${fixture.matchTime} (Home Game)"
                    } else {
                        "${fixture.matchTime} (Away Game)"
                    }

                    fixture.awayLogo?.let {
                        team2Logo.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
                    }
                    team2Name.text = fixture.awayTeam
                    matchType.text = fixture.sport
                    ageGroup.text = fixture.ageGroup

                    eventEdit?.setOnClickListener {
                        val intent = Intent(requireContext(), EditFixture::class.java)
                        intent.putExtra("fixture_id", fixture.fixtureId)
                        startActivity(intent)
                    }

                    linearLayout?.addView(fixtureCard)
                }
            }
        } else {
            emptyStateImage?.visibility = View.VISIBLE
            Log.e("UpcomingMatchesFragment", "No fixtures found in the database.")
        }
    }

    private fun FormattingDate(
        fixture: FixtureModel,
        fixtureDate: TextView,
        fixtureDateBox: TextView
    ) {
        // Change input format to match the stored format
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        // Output format for fixtureDate
        val outputFormat = SimpleDateFormat("EEEE -- MMMM dd", Locale.getDefault())
        // Output format for fixtureDateBox
        val outputFormatBox = SimpleDateFormat("dd MMMM", Locale.getDefault())

        try {
            val date = inputFormat.parse(fixture.matchDate) // Parse the stored date
            fixtureDate.text = outputFormat.format(date) // For fixtureDate
            fixtureDateBox.text = outputFormatBox.format(date) // For fixtureDateBox
        } catch (e: Exception) {
            Log.e("UpcomingMatchesFragment", "Error parsing date: ${e.message}")
            fixtureDate.text = fixture.matchDate // Fallback to original if parsing fails
            fixtureDateBox.text = fixture.matchDate // Fallback for fixtureDateBox
        }
    }
}