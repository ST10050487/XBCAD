package za.co.varsitycollage.st10050487.knights

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class OverviewFragment : Fragment() {

    companion object {
        private const val ARG_FIXTURE_ID = "fixture_id"

        @JvmStatic
        fun newInstance(fixtureId: Int) =
            OverviewFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_FIXTURE_ID, fixtureId)
                }
            }
    }

    private lateinit var homeTeamTextView: TextView
    private lateinit var awayTeamTextView: TextView
    private lateinit var ageGroupTextView: TextView
    private lateinit var leagueTextView: TextView
    private lateinit var meetingTimeTextView: TextView
    private lateinit var departureTimeTextView: TextView
    private lateinit var arrivalTimeTextView: TextView
    private lateinit var homeScoreTextView: TextView
    private lateinit var awayScoreTextView: TextView
    private lateinit var manOfTheMatchTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_overview, container, false)

        // Initialize TextViews
        homeTeamTextView = view.findViewById(R.id.home_team)
        awayTeamTextView = view.findViewById(R.id.away_team)
        ageGroupTextView = view.findViewById(R.id.age_group)
        leagueTextView = view.findViewById(R.id.league)
        meetingTimeTextView = view.findViewById(R.id.meeting_time)
        departureTimeTextView = view.findViewById(R.id.depature_time)
        arrivalTimeTextView = view.findViewById(R.id.arrival_time)
        homeScoreTextView = view.findViewById(R.id.home_score)
        awayScoreTextView = view.findViewById(R.id.away_score)
        manOfTheMatchTextView = view.findViewById(R.id.man_of_the_match)

        // Fetch and display match details
        val fixtureId = arguments?.getInt(ARG_FIXTURE_ID) ?: 0
        displayMatchDetails(fixtureId)

        return view
    }

    private fun displayMatchDetails(fixtureId: Int) {
        val dbHelper = DBHelper(requireContext())
        val fixture = dbHelper.getFixtureDetails(fixtureId)
        val timesheet = dbHelper.getTimesDetails(fixtureId)

        fixture?.let {
            homeTeamTextView.text = it.homeTeam
            awayTeamTextView.text = it.awayTeam
            ageGroupTextView.text = it.ageGroup
            leagueTextView.text = it.league
        }

        timesheet?.let {
            meetingTimeTextView.text = it.meetTime
            departureTimeTextView.text = it.busDepartureTime
            arrivalTimeTextView.text = it.busReturnTime
            manOfTheMatchTextView.text = it.manOfTheMatch
            homeScoreTextView.text = it.homeScore?.toString() ?: "N/A"
            awayScoreTextView.text = it.awayScore?.toString() ?: "N/A"
        }
    }
}