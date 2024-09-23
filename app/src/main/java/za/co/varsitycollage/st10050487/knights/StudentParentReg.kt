package za.co.varsitycollage.st10050487.knights

import android.view.ViewGroup
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.constraintlayout.widget.ConstraintLayout

class StudentParentReg : Fragment() {
    private lateinit var dbHelper: DBHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_student_parent_reg, container, false)

        // Initialize DBHelper
        dbHelper = DBHelper(requireContext())

        // Get references to UI elements
        val firstNameField = view.findViewById<EditText>(R.id.FirstNameField)
        val lastNameField = view.findViewById<EditText>(R.id.LastnameField)
        val passwordField = view.findViewById<EditText>(R.id.PasswordField)
        val confirmPasswordField = view.findViewById<EditText>(R.id.ConfirmPasswordField)
        val emailField = view.findViewById<EditText>(R.id.EmailField)
        val checkBox = view.findViewById<CheckBox>(R.id.checkBox)
        val regButton = view.findViewById<Button>(R.id.Regbtn)
        val textView: TextView = view.findViewById(R.id.textView7)

        // Adding a color for the text view
        makeTxtColor(textView)

        // Adding a color for the register button
        changeRegisterButtonColor(regButton)

        // Set checkbox color programmatically
        setCheckboxColor(checkBox)

        // Register button click listener
        regButton.setOnClickListener {
            val firstName = firstNameField.text.toString()
            val lastName = lastNameField.text.toString()
            val password = passwordField.text.toString()
            val confirmPassword = confirmPasswordField.text.toString()
            val email = emailField.text.toString()

            if (firstName.isEmpty() || lastName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else if (!checkBox.isChecked) {
                Toast.makeText(requireContext(), "You must agree to the terms", Toast.LENGTH_SHORT).show()
            } else {
                // Insert data into SQLite database
                dbHelper.addUsers(firstName, lastName, "", email, 1)
                Toast.makeText(requireContext(), "User Registered Successfully", Toast.LENGTH_SHORT).show()
                clearFields(firstNameField, lastNameField, passwordField, confirmPasswordField, emailField, checkBox)
            }
        }

        return view
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

    private fun changeRegisterButtonColor(registerButton: Button) {
        registerButton.setBackgroundColor(Color.parseColor("#FFA500")) // Set the desired color here
    }

    private fun setCheckboxColor(checkBox: CheckBox) {
        checkBox.setButtonDrawable(R.drawable.checkbox_custom)
    }

    private fun clearFields(
        firstNameField: EditText, lastNameField: EditText,
        passwordField: EditText, confirmPasswordField: EditText,
        emailField: EditText, checkBox: CheckBox
    ) {
        firstNameField.text.clear()
        lastNameField.text.clear()
        passwordField.text.clear()
        confirmPasswordField.text.clear()
        emailField.text.clear()
        checkBox.isChecked = false
    }
}