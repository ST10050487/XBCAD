package za.co.varsitycollage.st10050487.knights


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class SportsFixtureFragment : Fragment() {

    companion object {
        private const val ARG_FIXTURE_NAME = "fixture_name"
        private const val ARG_FIXTURE_TIME = "fixture_time"
        private const val ARG_FIXTURE_DATE = "fixture_date"
        private const val ARG_SPORT_NAME = "sport_name"
        private const val ARG_SPORT_CATEGORY = "sport_category"

        // Use this method to pass fixture data dynamically to the fragment
        fun newInstance(
            fixtureName: String,
            fixtureTime: String,
            fixtureDate: String,
            sportName: String,
            sportCategory: String
        ): SportsFixtureFragment {
            val fragment = SportsFixtureFragment()
            val args = Bundle()
            args.putString(ARG_FIXTURE_NAME, fixtureName)
            args.putString(ARG_FIXTURE_TIME, fixtureTime)
            args.putString(ARG_FIXTURE_DATE, fixtureDate)
            args.putString(ARG_SPORT_NAME, sportName)
            args.putString(ARG_SPORT_CATEGORY, sportCategory)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment layout (your modified fixtures_fragment.xml)
        val view = inflater.inflate(R.layout.fixtures_fragment, container, false)

        // Extract data from the arguments
        val fixtureTime = arguments?.getString(ARG_FIXTURE_TIME)
        val fixtureDate = arguments?.getString(ARG_FIXTURE_DATE)
        val sportName = arguments?.getString(ARG_SPORT_NAME)
        val sportCategory = arguments?.getString(ARG_SPORT_CATEGORY)

        // Find views and set the data
        val fixtureTimeTextView: TextView = view.findViewById(R.id.textView1)
        val fixtureDateTextView: TextView = view.findViewById(R.id.textView2)
        val sportNameTextView: TextView = view.findViewById(R.id.textView3)
        val sportCategoryTextView: TextView = view.findViewById(R.id.textView4)

        // Set the data dynamically to the views
        fixtureTimeTextView.text = fixtureTime ?: "Time not available"
        fixtureDateTextView.text = fixtureDate ?: "Date not available"
        sportNameTextView.text = sportName ?: "Sport name not available"
        sportCategoryTextView.text = sportCategory ?: "Category not available"

        return view
    }
}