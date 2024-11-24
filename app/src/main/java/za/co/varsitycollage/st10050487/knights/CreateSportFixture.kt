package za.co.varsitycollage.st10050487.knights

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CreateSportFixture : AppCompatActivity() {
    companion object {
        var fixtureID: Long = -1 // Static variable to hold the fixture ID
    }

    private lateinit var dbHelper: DBHelper
    private lateinit var homeTeamLogo: ImageView
    private lateinit var awayTeamLogo: ImageView
    private lateinit var matchTimeEditText: EditText
    private lateinit var matchDateEditText: EditText
    private lateinit var uploadHomeTeamBtn: TextView
    private lateinit var uploadAwayTeamBtn: TextView
    private lateinit var homeTeamNameEditText: EditText
    private lateinit var awayTeamNameEditText: EditText
    private lateinit var venueEditText: EditText
    private lateinit var matchDescriptionEditText: EditText
    private lateinit var createFixtureButton: Button
    private lateinit var navigateToTimesheetButton: FloatingActionButton // Change to FloatingActionButton

    private val PICK_IMAGE_REQUEST = 1
    private var selectedSport: String? = null
    private var selectedAgeGroup: String? = null
    private var selectedLeague: String? = null
    private var selectedMatchTime: String? = null
    private var selectedMatchDate: String? = null
    private var homeTeamName: String? = null
    private var awayTeamName: String? = null
    private var venue: String? = null
    private var matchDescription: String? = null
    private var homeTeamLogoUri: Uri? = null
    private var awayTeamLogoUri: Uri? = null
    private var isHomeTeamLogo: Boolean = true
    private var userId: Int? = null // Declare a variable to hold the user ID

    private val leagueIdMapping = mapOf(
        "WP League" to 1,
        "Inter-School" to 2,
        "Provincial" to 3,
        "National" to 4,
        "International" to 5,
        "Friendly" to 6,
        "Tournament" to 7
    )

    private var leagueId: Int? = null // Variable to hold the selected league ID
    private var generatedFixtureId: Long = -1 // Variable to hold the generated fixture ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_sport_fixture)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DBHelper(this)

        NavigatingBackBtn()

        // Retrieve the USER_ID from the Intent
        userId = intent.getIntExtra("USER_ID", -1) // Default value is -1 if not found

        // Initialize the ImageView and the upload button
        homeTeamLogo = findViewById(R.id.display_home_team_logo)
        awayTeamLogo = findViewById(R.id.display_away_team_logo)
        uploadHomeTeamBtn = findViewById(R.id.upload_home_team_btn)
        uploadAwayTeamBtn = findViewById(R.id.upload_away_team_btn)

        // Initialize the EditText and Button fields
        homeTeamNameEditText = findViewById(R.id.home_team_name)
        awayTeamNameEditText = findViewById(R.id.away_team_name)
        venueEditText = findViewById(R.id.venue)
        matchDescriptionEditText = findViewById(R.id.Matchdescription)
        createFixtureButton = findViewById(R.id.create_fixtureBtn)
        navigateToTimesheetButton = findViewById(R.id.timesheet_fab) // Updated ID

        // Set onClickListener to the upload buttons
        uploadHomeTeamBtn.setOnClickListener {
            isHomeTeamLogo = true
            openImagePicker()
        }
        uploadAwayTeamBtn.setOnClickListener {
            isHomeTeamLogo = false
            openImagePicker()
        }

        // Set onClickListener to the create fixture button
        createFixtureButton.setOnClickListener {
            if (validateInputs()) {
                storeValues()
                insertFixtureIntoDatabase()
            }
        }

        // Set onClickListener to the navigate to timesheet button
        navigateToTimesheetButton.setOnClickListener {
            navigateToCreateTimesheet() // Call the method to navigate
        }

        val sportSpinner: Spinner = findViewById(R.id.sport_dropdown)
        val ageGroupSpinner: Spinner = findViewById(R.id.age_group_dropdown)
        val leagueSpinner: Spinner = findViewById(R.id.league_dropdown)
        matchTimeEditText = findViewById(R.id.Matchtime)
        matchDateEditText = findViewById(R.id.Matchdate)

        val sports = dbHelper.getAllSports()
        val ageGroups = dbHelper.getAllAgeGroups()
        val leagues = dbHelper.getAllLeagues()

        Log.d("CreateSportFixture", "Sports: $sports")
        Log.d("CreateSportFixture", "Age Groups: $ageGroups")
        Log.d("CreateSportFixture", "Leagues: $leagues")

        populateSpinner(sportSpinner, sports)
        populateSpinner(ageGroupSpinner, ageGroups)
        populateSpinner(leagueSpinner, leagues)

        // Set OnItemSelectedListener to the sportSpinner
        sportSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: android.view.View,
                position: Int,
                id: Long
            ) {
                selectedSport = parent.getItemAtPosition(position) as String
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        // Set OnItemSelectedListener to the ageGroupSpinner
        ageGroupSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: android.view.View,
                position: Int,
                id: Long
            ) {
                selectedAgeGroup = parent.getItemAtPosition(position) as String
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        // Set OnItemSelectedListener to the leagueSpinner
        leagueSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: android.view.View,
                position: Int,
                id: Long
            ) {
                selectedLeague = parent.getItemAtPosition(position) as String
                leagueId =
                    leagueIdMapping[selectedLeague] // Get the league ID based on selected league
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        // Set OnClickListener to the matchTimeEditText
        matchTimeEditText.setOnClickListener {
            showTimePicker()
        }

        // Set OnClickListener to the matchDateEditText
        matchDateEditText.setOnClickListener {
            showDatePicker()
        }
    }

    private fun validateInputs(): Boolean {
        if (homeTeamNameEditText.text.toString().trim().isEmpty()) {
            homeTeamNameEditText.error = "Home team name is required"
            return false
        }

        if (awayTeamNameEditText.text.toString().trim().isEmpty()) {
            awayTeamNameEditText.error = "Away team name is required"
            return false
        }

        if (venueEditText.text.toString().trim().isEmpty()) {
            venueEditText.error = "Venue is required"
            return false
        }

        if (selectedMatchTime.isNullOrEmpty()) {
            matchTimeEditText.error = "Match time is required"
            return false
        }

        if (selectedMatchDate.isNullOrEmpty()) {
            matchDateEditText.error = "Match date is required"
            return false
        }

        val (isValid, errorMessage) = isDateValid(selectedMatchDate!!)
        if (!isValid) {
            matchDateEditText.error = errorMessage
            return false
        }

        if (leagueId == null) {
            Toast.makeText(this, "Please select a league", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun isDateValid(date: String): Pair<Boolean, String?> {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        sdf.isLenient = false
        return try {
            val selectedDate = sdf.parse(date)
            val currentDate = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time

            if (selectedDate != null) {
                if (selectedDate.before(currentDate)) {
                    Pair(false, "Date has already passed")
                } else {
                    Pair(true, null)
                }
            } else {
                Pair(false, "Invalid date, example: 2025-12-31")
            }
        } catch (e: Exception) {
            Pair(false, "Invalid date, example: 2025-12-31")
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                // Format the selected date as yyyy-MM-dd
                selectedMatchDate =
                    String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                matchDateEditText.setText(selectedMatchDate) // Display the selected date in the EditText
            }, year, month, day)

        datePickerDialog.show()
    }

    private fun NavigatingBackBtn() {
        // Find the back button TextView
        val backButton = findViewById<TextView>(R.id.back_btn)

        // Set an OnClickListener to navigate to AdminSportsFixtures
        backButton.setOnClickListener {
            val intent = Intent(
                this,
                AdminSportsFixtures::class.java
            ) // Change to your AdminSportsFixtures class
            startActivity(intent)
            finish() // Optionally call finish() if you want to remove this activity from the back stack
        }
    }

    private fun populateSpinner(spinner: Spinner, data: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, data)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri: Uri? = data.data
            if (imageUri != null) {
                if (isHomeTeamLogo) {
                    homeTeamLogo.setImageURI(imageUri) // Display the selected image in the home team ImageView
                    homeTeamLogoUri = imageUri
                } else {
                    awayTeamLogo.setImageURI(imageUri) // Display the selected image in the away team ImageView
                    awayTeamLogoUri = imageUri
                }
            }
        }
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            // Format the selected time
            selectedMatchTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            matchTimeEditText.setText(selectedMatchTime) // Display the selected time in the EditText
        }, hour, minute, true) // true for 24-hour format

        timePickerDialog.show()
    }

    private fun navigateToCreateTimesheet() {
        // Allow navigation to the CreateTimesheet activity regardless of fixture ID
        val intent = Intent(this, CreateTimesheet::class.java)
        intent.putExtra("FIXTURE_ID", generatedFixtureId) // Pass the fixture ID
        startActivity(intent)
    }

    private fun storeValues() {
        homeTeamName = homeTeamNameEditText.text.toString()
        awayTeamName = awayTeamNameEditText.text.toString()
        venue = venueEditText.text.toString()
        matchDescription = matchDescriptionEditText.text.toString()
        selectedMatchTime = matchTimeEditText.text.toString()
        selectedMatchDate = matchDateEditText.text.toString()

        // You can now use these variables as needed
        Log.d("CreateSportFixture", "Home Team Name: $homeTeamName")
        Log.d("CreateSportFixture", "Away Team Name: $awayTeamName")
        Log.d("CreateSportFixture", "Venue: $venue")
        Log.d("CreateSportFixture", "Match Description: $matchDescription")
        Log.d("CreateSportFixture", "Match Time: $selectedMatchTime")
        Log.d("CreateSportFixture", "Match Date: $selectedMatchDate")
        Log.d("CreateSportFixture", "Home Team Logo URI: $homeTeamLogoUri")
        Log.d("CreateSportFixture", "Away Team Logo URI: $awayTeamLogoUri")
    }

    private fun insertFixtureIntoDatabase(): Long {
        val homeLogoBlob = homeTeamLogoUri?.let { uri ->
            contentResolver.openInputStream(uri)?.use { inputStream ->
                inputStream.readBytes()
            }
        }

        val awayLogoBlob = awayTeamLogoUri?.let { uri ->
            contentResolver.openInputStream(uri)?.use { inputStream ->
                inputStream.readBytes()
            }
        }

        // Determine if the game is a home game based on away team name and venue
        val isHomeGame = when {
            awayTeamName?.contains("Bosemansdam", ignoreCase = true) == true ||
                    awayTeamName?.contains("Boseman'sdam", ignoreCase = true) == true -> false
            venue?.contains("Bosemansdam High School", ignoreCase = true) == true ||
                    venue?.contains("Boseman'sdam High School", ignoreCase = true) == true -> true
            else -> false // If venue is not one of the specified, consider it an away game
        }

        val values = ContentValues().apply {
            put("SPORT", selectedSport)
            put("HOME_TEAM", homeTeamName)
            put("AWAY_TEAM", awayTeamName)
            put("AGE_GROUP", selectedAgeGroup)
            put("LEAGUE", selectedLeague)
            put("HOME_LOGO", homeLogoBlob)
            put("AWAY_LOGO", awayLogoBlob)
            put("MATCH_LOCATION", venue)
            put("MATCH_DATE", selectedMatchDate)
            put("MATCH_TIME", selectedMatchTime)
            put("MATCH_DESCRIPTION", matchDescription ?: "") // Insert as empty string if null
            put("USER_ID", userId ?: -1) // Use the retrieved user ID, default to -1 if null
            put("LEAGUE_ID", leagueId ?: -1) // Default to -1 if leagueId is null
            put("IS_HOME_GAME", if (isHomeGame) 1 else 0) // Store whether it's a home game
            put("SET_TIME", selectedMatchTime) // Add this line to provide the SET_TIME value
            put("SET_DATE", selectedMatchDate)
        }

        val fixtureId = dbHelper.writableDatabase.insert("SPORT_FIXTURES", null, values)
        if (fixtureId == -1L) {
            Toast.makeText(this, "Failed to create sports fixture", Toast.LENGTH_LONG).show()
        } else {
            generatedFixtureId = fixtureId // Store the generated fixture ID
            fixtureID = generatedFixtureId // Update the static variable
            Log.d("CreateSportFixture", "Generated Fixture ID: $generatedFixtureId") // Log the fixture ID
            Toast.makeText(this, "You have successfully created the sports fixture", Toast.LENGTH_LONG).show()
        }

        // Clear the input fields
        homeTeamNameEditText.text.clear()
        awayTeamNameEditText.text.clear()
        venueEditText.text.clear()
        matchDescriptionEditText.text.clear()
        matchTimeEditText.text.clear()
        matchDateEditText.text.clear()
        homeTeamLogo.setImageURI(null)
        awayTeamLogo.setImageURI(null)
        homeTeamLogoUri = null
        awayTeamLogoUri = null

        return fixtureId // Return the generated fixture ID
    }
}