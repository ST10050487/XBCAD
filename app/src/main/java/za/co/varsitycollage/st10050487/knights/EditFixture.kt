package za.co.varsitycollage.st10050487.knights

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import za.co.varsitycollage.st10050487.knights.databinding.ActivityEditFixtureBinding

class EditFixture : AppCompatActivity() {
    private lateinit var binding: ActivityEditFixtureBinding
    private lateinit var dbHelper: DBHelper
    private lateinit var sportsList: List<String>
    private lateinit var ageGroupList: List<String>
    private var dummyId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditFixtureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)

        // Fetch sports data from the database
        sportsList = dbHelper.getAllSports()
        // Set the data to the spinner using ArrayAdapter
        val sportAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sportsList)
        sportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSport.adapter = sportAdapter

        // Fetch age group data from the database
        ageGroupList = dbHelper.getAllAgeGroups()
        // Set the data to the spinner using ArrayAdapter
        val ageGroupAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ageGroupList)
        ageGroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTeam.adapter = ageGroupAdapter

        // take out
       // val id = dbHelper.addDummyFixtureWithUserId(dummyId)
      //  Toast.makeText(this, id.toString(), Toast.LENGTH_SHORT).show()
        loadFixture(3)
        val deleteFixture = findViewById<Button>(R.id.btnDelete)
        deleteFixture.setOnClickListener {
            if (!dbHelper.checkIsAdmin(dummyId)) {
                deleteFixture.visibility = View.GONE
            } else {
                deleteFixture.setOnClickListener {
                    showConfirmationDialog(3)
                }
            }
        }
    }

    private fun showConfirmationDialog(fixtureId: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Fixture")
        builder.setMessage("Are you sure you want to delete this Fixture?")

        builder.setPositiveButton("Yes") { dialog: DialogInterface, which: Int ->
            // Handle the delete operation here
            dbHelper.deleteFixture(fixtureId)
        }
        builder.setNegativeButton("No") { dialog: DialogInterface, which: Int ->
            // Dismiss the dialog
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    private fun loadFixture(fixtureId: Int) {
        val fixture = dbHelper.getFixtureDetails(fixtureId)
        fixture?.let {
            binding.txtHomeTeam.setText(it.homeTeam)
            binding.txtAwayTeam.setText(it.awayTeam)
            binding.txtVenue.setText(it.matchLocation)
            binding.txtTime.setText(it.matchTime)
            binding.txtDate.setText(it.matchDate)
            // Assuming you have methods to set the spinner values
            setSpinner(binding.spinnerSport,sportsList, it.sport)
            setSpinner(binding.spinnerTeam,ageGroupList, it.ageGroup)
            // Set images if available
            if (it.homeLogo != null) {
                binding.imgHomeLogo.setImageBitmap(
                    BitmapFactory.decodeByteArray(
                        it.homeLogo,
                        0,
                        it.homeLogo.size
                    )
                )
            }
            if (it.awayLogo != null) {
                binding.imgAwayLogo.setImageBitmap(
                    BitmapFactory.decodeByteArray(
                        it.awayLogo,
                        0,
                        it.awayLogo.size
                    )
                )
            }
        }
    }
    private fun setSpinner(spinner: Spinner, items: List<String>, value: String) {
        val index = items.indexOf(value)
        if (index >= 0) {
            spinner.setSelection(index)
        }
    }
}