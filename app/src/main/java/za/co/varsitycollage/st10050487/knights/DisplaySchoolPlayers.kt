package za.co.varsitycollage.st10050487.knights

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import za.co.varsitycollage.st10050487.knights.databinding.ActivityDisplaySchoolPlayersBinding

class DisplaySchoolPlayers : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityDisplaySchoolPlayersBinding
    private lateinit var adapter: PlayerInforRecyclerViewAdapter
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplaySchoolPlayersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)
        val playerList = dbHelper.getAllPlayers()

        adapter = PlayerInforRecyclerViewAdapter(playerList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.searchView.setOnQueryTextListener(this)

        // Populate the Spinners
        populateSpinners()

        // Add click listener to filterIcon
        binding.filterIcon.setOnClickListener {
            val selectedGrade = binding.gradeSpinner.selectedItem.toString()
            val selectedAgeGroup = binding.ageGroupSpinner.selectedItem.toString()
            val filteredList = dbHelper.filterPlayersByAgeGroupAndGrade(selectedAgeGroup, selectedGrade)
            adapter.updateData(filteredList)
        }
    }

    private fun populateSpinners() {
        val grades = listOf("Grade 8", "Grade 9", "Grade 10", "Grade 11", "Grade 12")
        val ageGroups = listOf("Under 15", "Under 16", "Under 17", "Under 18")

        val gradeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, grades)
        gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.gradeSpinner.adapter = gradeAdapter

        val ageGroupAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ageGroups)
        ageGroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.ageGroupSpinner.adapter = ageGroupAdapter
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        val filteredList = if (newText.isNullOrEmpty()) {
            dbHelper.getAllPlayers()
        } else {
            dbHelper.searchPlayers(newText)
        }
        adapter.updateData(filteredList)
        return true
    }
}