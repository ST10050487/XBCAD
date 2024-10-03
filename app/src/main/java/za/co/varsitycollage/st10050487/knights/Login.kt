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
import org.mindrot.jbcrypt.BCrypt

class Login : AppCompatActivity() {

    private lateinit var emailTxt: EditText
    private lateinit var passwordTxt: EditText
    private lateinit var loginBtn: Button
    //Creating an instance of the Validations class
    private lateinit var valid: Validations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initializing views
        emailTxt = findViewById(R.id.emailTxt)
        passwordTxt = findViewById(R.id.passwordTxt)
        loginBtn = findViewById(R.id.LoginBtn)

        // Initializing the validation class
        valid = Validations()

        // Setting click listener for the Login button
        loginBtn.setOnClickListener {
            getUserInput()
        }
    }

    // A method to get user input
    private fun getUserInput() {
        val email = emailTxt.text.toString()
        val password = passwordTxt.text.toString()
        val encryptedPassword: String

        // Validating email
        if (email.isEmpty()) {
            emailTxt.error = "Email is required"
            return
        }
        if (!valid.CheckEmail(email)) {
            emailTxt.error = "Invalid email format"
            return
        }

        // Validate password
        if (!valid.CheckPassword(password)) {
            Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show()
            return
        }
        // Hashing the inputted password
        //encryptedPassword = hashPassword(password)
        // Check if user exists in the database
        val dbHelper = DBHelper(this)
        val userId = dbHelper.validateUser(email,password)

        if (userId != null) {
            // if user exists, navigate to Home Activity
            val intent = Intent(this, UpdatePlayerProfile::class.java)
            // Passing the USER_ID to Home Activity
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
            //Finishing the login activity once the user is logged in
            finish()
        } else {
            // User does not exist or incorrect password
            emailTxt.error = "Invalid email or password"
            passwordTxt.error = "Invalid email or password"
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
        }
    }
    //A method to hash the entered password
    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }
}

