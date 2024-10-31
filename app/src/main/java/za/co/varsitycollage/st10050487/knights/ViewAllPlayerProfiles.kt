package za.co.varsitycollage.st10050487.knights

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast

class ViewAllPlayerProfiles : AppCompatActivity() {


    private lateinit var rvProfiles: RecyclerView
    private lateinit var llEmptyState: LinearLayout
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_player_profiles)

        rvProfiles = findViewById(R.id.rv_profiles)
        llEmptyState = findViewById(R.id.ll_empty_state)
        dbHelper = DBHelper(this)
        val backButton: Button = findViewById(R.id.button2)

        // Set up the back button to navigate to the home screen
        backButton.setOnClickListener {
            val intent = Intent(this, HomeScreen::class.java)
            startActivity(intent)
            finish()
        }

        try {
            // Fetch data from the database
            val playerProfiles = dbHelper.getAllPlayerProfiles()

            // Display profiles or empty state
            if (playerProfiles.isNotEmpty()) {
                llEmptyState.visibility = View.GONE
                rvProfiles.visibility = View.VISIBLE

                rvProfiles.layoutManager = LinearLayoutManager(this)
                rvProfiles.adapter = PlayerProfileAdapter(playerProfiles)
            } else {
                rvProfiles.visibility = View.GONE
                llEmptyState.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "An error occurred while fetching player profiles.", Toast.LENGTH_SHORT).show()
        }
    }
}