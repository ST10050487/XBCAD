package za.co.varsitycollage.st10050487.knights

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CreateSportFixture : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper

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

        val sportSpinner: Spinner = findViewById(R.id.sport_spinner)
        val ageGroupSpinner: Spinner = findViewById(R.id.age_group_spinner)
        val leagueSpinner: Spinner = findViewById(R.id.league_spinner)

        val sports = dbHelper.getAllSports()
        val ageGroups = dbHelper.getAllAgeGroups()
        val leagues = dbHelper.getAllLeagues()

        Log.d("CreateSportFixture", "Sports: $sports")
        Log.d("CreateSportFixture", "Age Groups: $ageGroups")
        Log.d("CreateSportFixture", "Leagues: $leagues")

        populateSpinner(sportSpinner, sports)
        populateSpinner(ageGroupSpinner, ageGroups)
        populateSpinner(leagueSpinner, leagues)
    }

    private fun populateSpinner(spinner: Spinner, data: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, data)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
}