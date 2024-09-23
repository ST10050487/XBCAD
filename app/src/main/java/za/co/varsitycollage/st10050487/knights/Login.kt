package za.co.varsitycollage.st10050487.knights

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Button

class Login : AppCompatActivity() {

    private lateinit var emailTxt: EditText
    private lateinit var passwordTxt: EditText
    private lateinit var loginBtn: Button
    //Creating an instance of the Validations class
    private lateinit var valid: Validations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize views
        emailTxt = findViewById(R.id.emailTxt)
        passwordTxt = findViewById(R.id.passwordTxt)
        loginBtn = findViewById(R.id.LoginBtn)

        // Initialize your validation class
        valid = Validations()

        // Set click listener for the Login button
        loginBtn.setOnClickListener {
            getUserInput()
        }
    }

    // A method to get user input
    private fun getUserInput() {
        val email = emailTxt.text.toString()
        val password = passwordTxt.text.toString()

        // Calling the validateEmail method
        if (!valid.CheckEmail(email)) {
            Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show()
            return
        }

        // Calling the validatePassword method
        if (!valid.CheckPassword(password)) {
            Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show()
            return
        }
        // If the email and password are valid, the user will be logged in
        // The user will be redirected to the StudentParentReg activity
        val intent = Intent(this, StudentParentReg::class.java)
        startActivity(intent)
    }
}

