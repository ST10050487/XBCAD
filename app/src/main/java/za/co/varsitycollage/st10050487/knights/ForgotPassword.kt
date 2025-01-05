package za.co.varsitycollage.st10050487.knights

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ForgotPassword : AppCompatActivity() {
    private lateinit var backArrow: ImageView
    private lateinit var backText: TextView
    private lateinit var loginButton: Button
    private lateinit var emailEditText: EditText
    private lateinit var sendPasswordButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgotpassword)

        // Initialize views
        backArrow = findViewById(R.id.back_arrow)
        backText = findViewById(R.id.back_function)
        loginButton = findViewById(R.id.LoginBtn)
        emailEditText = findViewById(R.id.emailTxt)
        sendPasswordButton = findViewById(R.id.forgotEmailBtn)

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

        // Set click listener for the send password button
        sendPasswordButton.setOnClickListener {
            sendPassword()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    private fun sendPassword() {
        val email = emailEditText.text.toString().trim()

        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            return
        }

        val dbHelper = DBHelper(this)
        val password = dbHelper.getPasswordByEmail(email)

        if (password != null) {
            // Redirect to ResetPasswordActivity
            val intent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Email not found. Please register.", Toast.LENGTH_SHORT).show()
        }
    }
}