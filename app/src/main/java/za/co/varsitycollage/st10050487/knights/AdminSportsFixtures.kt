package za.co.varsitycollage.st10050487.knights

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AdminSportsFixtures : AppCompatActivity() {
    private val selectedSports = mutableListOf<String>() // Store selected sports

    private lateinit var searchEditText: EditText // Declare the search EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_sports_fixtures)

        // Load the SportsFixturesHomeScreenFragment into the fragment_container
        LoadingUpcomingPastFixtures(savedInstanceState)


        // Initialize the search EditText
        searchEditText = findViewById(R.id.search_fixtures)

        NavigationToFixtures()

        // Set up search listener
        setupSearchListener()

        FilterLogic()
    }

    private fun NavigationToFixtures() {
        // Set up the create fixture button listener
        val createFixtureButton = findViewById<Button>(R.id.uploadBtn)
        createFixtureButton.setOnClickListener {
            // Navigate to CreateSportsActivity
            val intent = Intent(this, CreateSportFixture::class.java)
            startActivity(intent)
        }
    }

    private fun setupSearchListener() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchQuery = s.toString()
                refreshCurrentFragment(searchQuery) // Pass the search query
            }

            override fun afterTextChanged(s: Editable?) {
                // If the search query is empty, refresh to show all upcoming matches
                if (s.isNullOrEmpty()) {
                    refreshCurrentFragment() // Refresh without a search query
                }
            }
        })
    }

    private fun LoadingUpcomingPastFixtures(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val fragment = SportsFixturesHomeScreenFragment(isAdmin = true) // Pass true for admin
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
    }

    private fun FilterLogic() {
        // Filter icon logic
        val filterIcon = findViewById<ImageView>(R.id.filter_icon)
        filterIcon.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.fixture_filter_popup)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val closeIcon = dialog.findViewById<ImageView>(R.id.close_icon)
            closeIcon.setOnClickListener { dialog.dismiss() }

            // Set up sport arrow click to show dropdown
            val sportArrow = dialog.findViewById<ImageView>(R.id.sport_arrow)
            sportArrow.setOnClickListener {
                showSportDropdown(dialog)
            }

            // Show Results button logic
            val showResultsButton = dialog.findViewById<ImageButton>(R.id.show_results_button)
            showResultsButton.setOnClickListener {
                // Logic to return to the previous screen
                refreshCurrentFragment()
                dialog.dismiss() // Optionally dismiss the dialog
            }

            dialog.show()
        }
    }

    private fun refreshCurrentFragment(searchQuery: String = "") {
        val fragment = upcomingMatchesFragment(isAdmin = true).apply {
            arguments = Bundle().apply {
                putStringArrayList("selectedSports", ArrayList(selectedSports))
                putString("searchQuery", searchQuery) // Pass the search query
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun showSportDropdown(parentDialog: Dialog) {
        val sportDialog = Dialog(this)
        sportDialog.setContentView(R.layout.sport_dropdown)
        sportDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Reference each CheckBox
        val soccerCheckBox = sportDialog.findViewById<CheckBox>(R.id.checkbox_soccer)
        val netballCheckBox = sportDialog.findViewById<CheckBox>(R.id.checkbox_netball)
        val rugbyCheckBox = sportDialog.findViewById<CheckBox>(R.id.checkbox_rugby)
        val hockeyCheckBox = sportDialog.findViewById<CheckBox>(R.id.checkbox_hockey)
        val cricketCheckBox = sportDialog.findViewById<CheckBox>(R.id.checkbox_cricket)
        val tennisCheckBox = sportDialog.findViewById<CheckBox>(R.id.checkbox_tennis)
        val basketballCheckBox = sportDialog.findViewById<CheckBox>(R.id.checkbox_basketball)
        val athleticsCheckBox = sportDialog.findViewById<CheckBox>(R.id.checkbox_athletics)
        val swimmingCheckBox = sportDialog.findViewById<CheckBox>(R.id.checkbox_swimming)

        // Handle OK button
        val okButton = sportDialog.findViewById<Button>(R.id.button_ok)
        okButton.setOnClickListener {
            // Clear previous selections
            selectedSports.clear()

            // Collect selected sports
            if (soccerCheckBox.isChecked) selectedSports.add("Soccer")
            if (netballCheckBox.isChecked) selectedSports.add("Netball")
            if (rugbyCheckBox.isChecked) selectedSports.add("Rugby")
            if (hockeyCheckBox.isChecked) selectedSports.add("Hockey")
            if (cricketCheckBox.isChecked) selectedSports.add("Cricket")
            if (tennisCheckBox.isChecked) selectedSports.add("Tennis")
            if (basketballCheckBox.isChecked) selectedSports.add("Basketball")
            if (athleticsCheckBox.isChecked) selectedSports.add("Athletics")
            if (swimmingCheckBox.isChecked) selectedSports.add("Swimming")

            // Show a toast or update UI with selected items
            Toast.makeText(this, "Selected: ${selectedSports.joinToString()}", Toast.LENGTH_SHORT)
                .show()

            // Now refresh the fragment and pass the selected sports
            refreshCurrentFragment()

            sportDialog.dismiss()  // Close the dropdown dialog
        }

        sportDialog.show()  // Display the dropdown dialog
    }
}