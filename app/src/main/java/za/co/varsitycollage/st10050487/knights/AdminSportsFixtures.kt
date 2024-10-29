package za.co.varsitycollage.st10050487.knights

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class AdminSportsFixtures : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_sports_fixtures)

        // Load the SportsFixturesHomeScreenFragment into the fragment_container
        if (savedInstanceState == null) {
            val fragment = SportsFixturesHomeScreenFragment(isAdmin = true) // Pass true for admin
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }

        // Set up the filter dialog
        val filterIcon = findViewById<ImageView>(R.id.filter_icon)
        filterIcon.setOnClickListener {
            // Create and display the filter dialog
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.fixture_filter_popup) // Make sure filter_popup.xml is in res/layout
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            // Close icon in the dialog
            val closeIcon = dialog.findViewById<ImageView>(R.id.close_icon)
            closeIcon.setOnClickListener { dialog.dismiss() }

            dialog.show()
        }
    }
}
