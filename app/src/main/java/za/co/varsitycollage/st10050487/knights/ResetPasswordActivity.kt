package za.co.varsitycollage.st10050487.knights

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.regex.Pattern

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var passwordField: EditText
    private lateinit var confirmPasswordField: EditText
    private lateinit var resetPasswordButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reset_password)

        passwordField = findViewById(R.id.txtPasswordField)
        confirmPasswordField = findViewById(R.id.txtConfirmPassword)
        resetPasswordButton = findViewById(R.id.Resetpassbtn)

        val backArrow: ImageView = findViewById(R.id.back_arrow)
        backArrow.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

        resetPasswordButton.setOnClickListener {
            if (validatePasswords()) {
                // Proceed with password reset logic
                Toast.makeText(this, "Password reset successful", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validatePasswords(): Boolean {
        val password = passwordField.text.toString()
        val confirmPassword = confirmPasswordField.text.toString()
        var isValid = true

        if (password.isEmpty()) {
            passwordField.error = "Password is required"
            isValid = false
        } else if (!isValidPassword(password)) {
            passwordField.error =
                "Password must be at least 8 characters, include one special character, one number, and one capital letter"
            isValid = false
        }

        if (confirmPassword.isEmpty()) {
            confirmPasswordField.error = "Confirm Password is required"
            isValid = false
        } else if (password != confirmPassword) {
            confirmPasswordField.error = "Passwords do not match"
            isValid = false
        }

        return isValid
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordPattern =
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$"
        val pattern = Pattern.compile(passwordPattern)
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }
}