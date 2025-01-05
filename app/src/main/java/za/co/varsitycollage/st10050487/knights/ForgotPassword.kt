package za.co.varsitycollage.st10050487.knights

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ForgotPassword : AppCompatActivity() {
    private lateinit var backArrow: ImageView
    private lateinit var backText: TextView
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgotpassword)

        // Initialize the back arrow, back text, and login button views
        backArrow = findViewById(R.id.back_arrow)
        backText = findViewById(R.id.back_function)
        loginButton = findViewById(R.id.LoginBtn)

        // Set click listener for the back arrow
        backArrow.setOnClickListener {
            navigateToLogin()
        }

        // Set click listener for the back text
        backText.setOnClickListener {
            navigateToLogin()
        }

        // Set click listener for the login button
        loginButton.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        // Create an intent to navigate to the Login activity
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish() // Optional: Call finish() if you want to remove the ForgotPassword activity from the back stack
    }
}