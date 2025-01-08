package za.co.varsitycollage.st10050487.knights

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AdminSportsFixtures : AppCompatActivity() {
    private val selectedSports = mutableListOf<String>() // Store selected sports
    private val selectedAgeGroups = mutableListOf<String>() // Store selected age groups

    private lateinit var searchEditText: EditText // Declare the search EditText
    private lateinit var selectedSportsLayout: LinearLayout // Declare the LinearLayout for sports
    private lateinit var selectedAgeGroupsLayout: LinearLayout // Declare the LinearLayout for age groups

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_sports_fixtures)

        // Clear SharedPreferences to reset selected sports and age groups
        clearSelectedSportsFromPreferences()
        clearSelectedAgeGroupsFromPreferences()

        // Initialize the LinearLayouts
        selectedSportsLayout = findViewById(R.id.selected_sports_layout)
        selectedAgeGroupsLayout = findViewById(R.id.selected_age_groups_layout)

        // Load the SportsFixturesHomeScreenFragment into the fragment_container
        loadingUpcomingPastFixtures(savedInstanceState)

        // Initialize the search EditText
        searchEditText = findViewById(R.id.search_fixtures)

        // Set up back button listener
        setupBackButton()

        navigationToFixtures()

        // Set up search listener
        setupSearchListener()

        filterLogic()
    }

    private fun clearSelectedSportsFromPreferences() {
        val sharedPreferences = getSharedPreferences("SportsPreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("selectedSports") // Clear the selected sports
        editor.apply()
    }

    private fun clearSelectedAgeGroupsFromPreferences() {
        val sharedPreferences = getSharedPreferences("SportsPreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("selectedAgeGroups") // Clear the selected age groups
        editor.apply()
    }

    private fun setupBackButton() {
        val backButton = findViewById<LinearLayout>(R.id.back_btn)
        backButton.setOnClickListener {
            // Navigate to AdminHomePageActivity
            val intent = Intent(this, AdminHome::class.java)
            startActivity(intent)
            finish() // Optionally call finish() if you want to remove this activity from the back stack
        }
    }

    private fun navigationToFixtures() {
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
                    refreshCurrentFragment()
                }
            }
        })
    }

    private fun loadingUpcomingPastFixtures(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val fragment = SportsFixturesHomeScreenFragment(isAdmin = true) // Pass true for admin
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
    }

    private fun filterLogic() {
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
                showSportDropdown()
            }

            // Set up age group arrow click listener
            val ageGroupArrow = dialog.findViewById<ImageView>(R.id.age_group_arrow)
            ageGroupArrow.setOnClickListener {
                showAgeGroupDropdown()
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

    private fun showSportDropdown() {
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
            selectedSportsLayout.removeAllViews() // Clear previous views

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

            // Save selected sports to SharedPreferences
            saveSelectedSportsToPreferences(selectedSports)

            // Update the LinearLayout with selected sports
            for (sport in selectedSports) {
                val sportView = createSportView(sport)
                selectedSportsLayout.addView(sportView)
            }

            // Refresh the fragment and pass the selected sports
            refreshCurrentFragment()

            sportDialog.dismiss()  // Close the dropdown dialog
        }

        sportDialog.show()  // Display the dropdown dialog
    }

    private fun saveSelectedSportsToPreferences(selectedSports: List<String>) {
        val sharedPreferences = getSharedPreferences("SportsPreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putStringSet("selectedSports", selectedSports.toSet()) // Save as a Set
        editor.apply()
    }

    private fun createSportView(sport: String): View {
        val sportLayout = LinearLayout(this)
        sportLayout.orientation = LinearLayout.HORIZONTAL
        sportLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val sportTextView = TextView(this).apply {
            text = sport
            textSize = 16f
            setTextColor(getColor(R.color.black))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        val removeIcon = ImageView(this).apply {
            setImageResource(R.drawable.ic_cross) // Replace with your cross icon
            layoutParams = LinearLayout.LayoutParams(
                20.dpToPx(), // Convert dp to pixels
                20.dpToPx()  // Convert dp to pixels
            )
            setOnClickListener {
                selectedSports.remove(sport) // Remove sport from the list
                selectedSportsLayout.removeView(sportLayout)

                // Save the updated selected sports to SharedPreferences
                saveSelectedSportsToPreferences(selectedSports)

                // Refresh the upcomingMatchesFragment
                refreshCurrentFragment() // This will refresh the fragment with updated selected sports
            }
        }

        sportLayout.addView(sportTextView)
        sportLayout.addView(removeIcon)

        return sportLayout
    }

    // Extension function to convert dp to pixels
    private fun Int.dpToPx(): Int {
        val density = resources.displayMetrics.density
        return (this * density).toInt()
    }

    private fun showAgeGroupDropdown() {
        val ageGroupDialog = Dialog(this)
        ageGroupDialog.setContentView(R.layout.agegroup_dropdown) // Use your age group layout
        ageGroupDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Reference each CheckBox
        val boysUnder15CheckBox = ageGroupDialog.findViewById<CheckBox>(R.id.checkbox_boys_under_15)
        val girlsUnder15CheckBox =
            ageGroupDialog.findViewById<CheckBox>(R.id.checkbox_girls_under_15)
        val boysUnder16CheckBox = ageGroupDialog.findViewById<CheckBox>(R.id.checkbox_boys_under_16)
        val girlsUnder16CheckBox =
            ageGroupDialog.findViewById<CheckBox>(R.id.checkbox_girls_under_16)
        val boysUnder17CheckBox = ageGroupDialog.findViewById<CheckBox>(R.id.checkbox_boys_under_17)
        val girlsUnder17CheckBox =
            ageGroupDialog.findViewById<CheckBox>(R.id.checkbox_girls_under_17)
        val boysUnder18CheckBox = ageGroupDialog.findViewById<CheckBox>(R.id.checkbox_boys_under_18)
        val girlsUnder18CheckBox =
            ageGroupDialog.findViewById<CheckBox>(R.id.checkbox_girls_under_18)

        // Handle OK button
        val okButton = ageGroupDialog.findViewById<Button>(R.id.button_age_group_ok)
        okButton.setOnClickListener {
            // Logic to collect selected age groups
            selectedAgeGroups.clear()
            if (boysUnder15CheckBox.isChecked) selectedAgeGroups.add("Boys Under 15")
            if (girlsUnder15CheckBox.isChecked) selectedAgeGroups.add("Girls Under 15")
            if (boysUnder16CheckBox.isChecked) selectedAgeGroups.add("Boys Under 16")
            if (girlsUnder16CheckBox.isChecked) selectedAgeGroups.add("Girls Under 16")
            if (boysUnder17CheckBox.isChecked) selectedAgeGroups.add("Boys Under 17")
            if (girlsUnder17CheckBox.isChecked) selectedAgeGroups.add("Girls Under 17")
            if (boysUnder18CheckBox.isChecked) selectedAgeGroups.add("Boys Under 18")
            if (girlsUnder18CheckBox.isChecked) selectedAgeGroups.add("Girls Under 18")

            // Save selected age groups to SharedPreferences
            saveSelectedAgeGroupsToPreferences(selectedAgeGroups)

            // Update the LinearLayout with selected age groups
            selectedAgeGroupsLayout.removeAllViews() // Clear previous views
            for (ageGroup in selectedAgeGroups) {
                val ageGroupView = createAgeGroupView(ageGroup)
                selectedAgeGroupsLayout.addView(ageGroupView)
            }

            // Refresh the fragment
            refreshCurrentFragment()

            ageGroupDialog.dismiss()  // Close the dropdown dialog
        }

        ageGroupDialog.show()  // Display the dropdown dialog
    }

    private fun createAgeGroupView(ageGroup: String): View {
        val ageGroupLayout = LinearLayout(this)
        ageGroupLayout.orientation = LinearLayout.HORIZONTAL
        ageGroupLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val ageGroupTextView = TextView(this).apply {
            text = ageGroup
            textSize = 16f
            setTextColor(getColor(R.color.black))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        val removeIcon = ImageView(this).apply {
            setImageResource(R.drawable.ic_cross) // Replace with your cross icon
            layoutParams = LinearLayout.LayoutParams(
                20.dpToPx(), // Convert dp to pixels
                20.dpToPx()  // Convert dp to pixels
            )
            setOnClickListener {
                selectedAgeGroups.remove(ageGroup) // Remove age group from the list
                selectedAgeGroupsLayout.removeView(ageGroupLayout)

                // Save the updated selected age groups to SharedPreferences
                saveSelectedAgeGroupsToPreferences(selectedAgeGroups)

                // Refresh the upcomingMatchesFragment
                refreshCurrentFragment() // This will refresh the fragment with updated selected age groups
            }
        }

        ageGroupLayout.addView(ageGroupTextView)
        ageGroupLayout.addView(removeIcon)

        return ageGroupLayout
    }

    private fun saveSelectedAgeGroupsToPreferences(selectedAgeGroups: List<String>) {
        val sharedPreferences = getSharedPreferences("SportsPreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putStringSet("selectedAgeGroups", selectedAgeGroups.toSet()) // Save as a Set
        editor.apply()
    }

    private fun refreshCurrentFragment(searchQuery: String = "") {
        val fragment = if (searchQuery.isEmpty()) {
            // Load the SportsFixturesHomeScreenFragment when there is no search query
            SportsFixturesHomeScreenFragment(isAdmin = true)
        } else {
            // Load the upcomingMatchesFragment with the search query
            upcomingMatchesFragment(isAdmin = true).apply {
                arguments = Bundle().apply {
                    putStringArrayList("selectedSports", getSelectedSportsFromPreferences())
                    putString("searchQuery", searchQuery) // Pass the search query
                }
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun getSelectedSportsFromPreferences(): ArrayList<String> {
        val sharedPreferences = getSharedPreferences("SportsPreferences", MODE_PRIVATE)
        return ArrayList(sharedPreferences.getStringSet("selectedSports", emptySet()) ?: emptySet())
    }
}