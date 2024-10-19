package za.co.varsitycollage.st10050487.knights

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.regex.Pattern

class StudentParentReg : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_student_parent_reg)

        // Initialize DBHelper
        dbHelper = DBHelper(this)

        // Get references to UI elements
        val firstNameField = findViewById<EditText>(R.id.FirstNameField)
        val lastNameField = findViewById<EditText>(R.id.LastnameField)
        val dateField = findViewById<EditText>(R.id.DateField)
        val passwordField = findViewById<EditText>(R.id.PasswordField)
        val confirmPasswordField = findViewById<EditText>(R.id.ConfirmPasswordField)
        val emailField = findViewById<EditText>(R.id.EmailField)
        val checkBox = findViewById<CheckBox>(R.id.checkBox)
        val regButton = findViewById<ImageButton>(R.id.Regbtn)
        val textView: TextView = findViewById(R.id.textView7)

        // Adding a color for the text view
        makeTxtColor(textView)

        // Set checkbox color programmatically
        setCheckboxColor(checkBox)

        // Register button click listener
        Validation(
            regButton,
            firstNameField,
            lastNameField,
            dateField,
            passwordField,
            confirmPasswordField,
            emailField,
            checkBox
        )
        // Date picker dialog for dateField
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateField(dateField)
        }

        dateField.setOnClickListener {
            DatePickerDialog(
                this@StudentParentReg,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun updateDateField(dateField: EditText) {
        val dateFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(dateFormat, Locale.US)
        dateField.setText(sdf.format(calendar.time))
    }

    private fun togglePasswordVisibility(passwordField: EditText, toggleView: ImageView) {
        if (passwordField.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            passwordField.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            toggleView.setImageResource(R.drawable.ic_visibility_off)
        } else {
            passwordField.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            toggleView.setImageResource(R.drawable.ic_visibility_on)
        }
        // Move the cursor to the end of the text
        passwordField.setSelection(passwordField.text.length)
    }

    private fun Validation(
        regButton: ImageButton,
        firstNameField: EditText,
        lastNameField: EditText,
        dateField: EditText,
        passwordField: EditText,
        confirmPasswordField: EditText,
        emailField: EditText,
        checkBox: CheckBox
    ) {
        regButton.setOnClickListener {
            val firstName = firstNameField.text.toString()
            val lastName = lastNameField.text.toString()
            val dateOfBirth = dateField.text.toString()
            val password = passwordField.text.toString()
            val confirmPassword = confirmPasswordField.text.toString()
            val email = emailField.text.toString()

            var isValid = true

            if (firstName.isEmpty()) {
                firstNameField.error = "First Name is required"
                isValid = false
            }

            if (lastName.isEmpty()) {
                lastNameField.error = "Last Name is required"
                isValid = false
            }

            if (dateOfBirth.isEmpty()) {
                dateField.error = "Date of Birth is required"
                isValid = false
            }

            if (email.isEmpty()) {
                emailField.error = "Email is required"
                isValid = false
            }

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

            if (!checkBox.isChecked) {
                Toast.makeText(this, "You must agree to the terms", Toast.LENGTH_SHORT).show()
                isValid = false
            }

            if (isValid) {
                // Determine roleId based on email domain
                val roleId = if (email.endsWith("@bmdhs.co.za")) 2 else 3

                // Insert data into SQLite database
                dbHelper.addUsers(firstName, lastName, dateOfBirth, email, password, roleId)
                Toast.makeText(this, "User Registered Successfully", Toast.LENGTH_SHORT).show()
                clearFields(
                    firstNameField,
                    lastNameField,
                    dateField,
                    passwordField,
                    confirmPasswordField,
                    emailField,
                    checkBox
                )

                //Navigate to SuccessReg Activity
                val intent = Intent(this, SuccessReg::class.java)
                startActivity(intent)
            }
        }
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordPattern =
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$"
        val pattern = Pattern.compile(passwordPattern)
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }

    private fun makeTxtColor(textView: TextView) {
        val text = "I agree to Term of Service and Privacy Policy"
        val spannableString = SpannableString(text)

        // Set color for "Term of Service"
        val termOfServiceStart = text.indexOf("Term of Service")
        val termOfServiceEnd = termOfServiceStart + "Term of Service".length
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#FFA500")),
            termOfServiceStart,
            termOfServiceEnd,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Set color for "Privacy Policy"
        val privacyPolicyStart = text.indexOf("Privacy Policy")
        val privacyPolicyEnd = privacyPolicyStart + "Privacy Policy".length
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#FFA500")),
            privacyPolicyStart,
            privacyPolicyEnd,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Set the spannable string to the TextView
        textView.text = spannableString
    }

    private fun setCheckboxColor(checkBox: CheckBox) {
        checkBox.setButtonDrawable(R.drawable.checkbox_custom)
    }

    private fun clearFields(
        firstNameField: EditText, lastNameField: EditText, dateField: EditText,
        passwordField: EditText, confirmPasswordField: EditText,
        emailField: EditText, checkBox: CheckBox
    ) {
        firstNameField.text.clear()
        lastNameField.text.clear()
        dateField.text.clear()
        passwordField.text.clear()
        confirmPasswordField.text.clear()
        emailField.text.clear()
        checkBox.isChecked = false
    }
}