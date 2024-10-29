package za.co.varsitycollage.st10050487.knights

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AdminSportsFixtures : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_sports_fixtures)


        // Load the SportsFixturesHomeScreenFragment into the fragment_container
        LoadingUpcomingPastFixtures(savedInstanceState)

        FilterLogic()


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

            dialog.show()
        }
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
            // Collect selected sports
            val selectedSports = mutableListOf<String>()
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
            Toast.makeText(this, "Selected: ${selectedSports.joinToString()}", Toast.LENGTH_SHORT).show()

            sportDialog.dismiss()  // Close the dropdown dialog
        }

        sportDialog.show()  // Display the dropdown dialog
    }
}