package za.co.varsitycollage.st10050487.knights

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sport_fixtures)

        // Retrieve the USER_ID
        val userId = intent.getIntExtra("USER_ID", -1)
        if (userId != -1) {
            // Use the USER_ID as needed
        } else {
            // Handle error if USER_ID is not passed
        }
    }
}
