package za.co.varsitycollage.st10050487.knights

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
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
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.mindrot.jbcrypt.BCrypt
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.regex.Pattern


class StudentParentReg : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper
    private lateinit var loginOp : LinearLayout
    private lateinit var googleSignIn: ImageButton
    private lateinit var gso : GoogleSignInOptions
    private lateinit var gsc : GoogleSignInClient
    private lateinit var auth : FirebaseAuth

    private lateinit var firstNameField: EditText
    private lateinit var lastNameField: EditText
    private lateinit var dateField :EditText
    private lateinit var passwordField : TextInputEditText
    private lateinit var confirmPasswordField :TextInputEditText
    private lateinit var emailField : EditText
    private lateinit var checkBox : CheckBox
    private lateinit var regButton : ImageButton
    private lateinit var textView: TextView
    private lateinit var googleReg : ImageButton
    val database = Firebase.database

    //declare the request code
    private  var RC_SIGN_IN = 20

    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_student_parent_reg)

        // Initialize DBHelper
        dbHelper = DBHelper(this)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        //Create a GoogleSignInOptions object with the default sign-in options
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        //Create a GoogleSignInClient object with the GoogleSignInOptions object
        gsc = GoogleSignIn.getClient(this , gso)


        // Get references to UI elements
        firstNameField = findViewById<EditText>(R.id.FirstNameField)
        lastNameField = findViewById<EditText>(R.id.LastnameField)
        dateField = findViewById<EditText>(R.id.DateField)
        passwordField = findViewById<TextInputEditText>(R.id.txtPasswordField)
        confirmPasswordField = findViewById<TextInputEditText>(R.id.txtConfirmPassword)
        emailField = findViewById<EditText>(R.id.EmailField)
        checkBox = findViewById<CheckBox>(R.id.checkBox)
        regButton = findViewById<ImageButton>(R.id.Regbtn)
        loginOp = findViewById<LinearLayout>(R.id.loginOp)
        textView =  findViewById(R.id.textView7)
        googleSignIn= findViewById<ImageButton>(R.id.googlebtn)

        // Adding a color for the text view
        makeTxtColor(textView)

        // Set checkbox color programmatically
        setCheckboxColor(checkBox)

        // Register button click listener
        regButton.setOnClickListener {
            registerUser()
        }
        loginOp.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }


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
        googleSignIn.setOnClickListener {
            // Handle Google Sign-In logic here
            Toast.makeText(this, "Google Sign-In Clicked", Toast.LENGTH_SHORT).show()
            signIn();
        }
        passwordField.setOnClickListener {
            PasswordGuidelines()
        }

    }
    private fun PasswordGuidelines() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Password Guidelines")
        builder.setMessage("Password must be at least 8 characters, include one special character, one number, and one capital letter")

        builder.setPositiveButton("Okay") { dialog: DialogInterface, which: Int ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    //Function to handle Google Sign-In
    private fun signIn() {
        val signInIntent = gsc.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    //Function to handle the result of the Google Sign-In
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Check if the request code matches the sign-in request code
        if (requestCode == RC_SIGN_IN) {
            // Retrieve the sign-in task from the intent data
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                // Attempt to get the GoogleSignInAccount from the task
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)

                // If successful, authenticate with the obtained ID token
                auth(account.idToken!!)

            } catch (e: ApiException) {
                // If an exception occurs, show a toast message indicating sign-in failure
                Toast.makeText(this, "Google Sign-In Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
    //Function to authenticate the user for Google Sign-In | Register with Google
    private fun auth(idToken: String) {
        // Create a credential using the ID token
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)

        // Sign in with the credential
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {
                    // If sign-in is successful, get the current user
                    val user = auth.currentUser
                    if (user != null)
                    {
                        // Check if the user is registered in the database
                        database.reference.child("users").child(user.uid).get().addOnSuccessListener {
                            // If the user is registered, navigate to SuccessActivity
                            if (it.exists())
                            {
                                // If the user is registered, navigate to SuccessActivity
                                val intent = Intent(this,HomeActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            // If the user is not registered, navigate to CompleteActivity
                            else if (!it.exists())
                            {
                                // Navigate to Complete Registration activity
                                val intent = Intent(this,SuccessReg::class.java)
                                startActivity(intent)
                                finish();
                            }
                        }

                    }
                } else {
                    // If sign-in fails, display a message to the user
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
    //Function to set up the UI
    private fun registerUser() {
            if (Validation()) {
                val firstName = firstNameField.text.toString()
                val lastName = lastNameField.text.toString()
                val dateOfBirth = dateField.text.toString()
                val email = emailField.text.toString().trim()
                val password = passwordField.text.toString().trim()

                // Hash the password before storing it
                val hashedPassword = hashPassword(password)

                // Determine roleId based on email domain
                val roleId = if (email.endsWith("@bmdhs.co.za")) 2 else 3

                // Insert data into SQLite database with hashed password
                dbHelper.addUsers(firstName, lastName, dateOfBirth, email, hashedPassword, roleId)
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
                // Navigate to SuccessReg Activity
                val intent = Intent(this, SuccessReg::class.java)
                startActivity(intent)

//                // Create user account with email and password
//                auth.createUserWithEmailAndPassword(email, password)
//                    .addOnCompleteListener(this) { task ->
//                        //
//                        if (task.isSuccessful) {
//                            // Handle registration logic here
//                            Toast.makeText(
//                                baseContext,
//                                "Registration Successful",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                            // Navigate to onboarding activity
//                            val intent = Intent(baseContext, SuccessReg::class.java)
//                            intent.putExtra("FIRST_NAME", firstNameField.text.toString())
//                            intent.putExtra("SURNAME", lastNameField.text.toString())
//                            startActivity(intent)
//                            finish();
//                        } else {
//                            // If sign-in fails, display a message to the user.
//                            Toast.makeText(
//                                baseContext,
//                                "Authentication failed: ${task.exception?.message}",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
            }
        }


    private fun Validation(): Boolean {
        var isValid = true
            val firstName = firstNameField.text.toString()
            val lastName = lastNameField.text.toString()
            val dateOfBirth = dateField.text.toString()
            val password = passwordField.text.toString()
            val confirmPassword = confirmPasswordField.text.toString()
            val email = emailField.text.toString()

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
        return isValid
    }

    private fun updateDateField(dateField: EditText) {
        val dateFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(dateFormat, Locale.US)
        dateField.setText(sdf.format(calendar.time))
    }

    // A method to hash the entered password
    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

//    private fun Validation() {
//        regButton.setOnClickListener {
//            val firstName = firstNameField.text.toString()
//            val lastName = lastNameField.text.toString()
//            val dateOfBirth = dateField.text.toString()
//            val password = passwordField.text.toString()
//            val confirmPassword = confirmPasswordField.text.toString()
//            val email = emailField.text.toString()
//
//            var isValid = true
//
//            if (firstName.isEmpty()) {
//                firstNameField.error = "First Name is required"
//                isValid = false
//            }
//
//            if (lastName.isEmpty()) {
//                lastNameField.error = "Last Name is required"
//                isValid = false
//            }
//
//            if (dateOfBirth.isEmpty()) {
//                dateField.error = "Date of Birth is required"
//                isValid = false
//            }
//
//            if (email.isEmpty()) {
//                emailField.error = "Email is required"
//                isValid = false
//            }
//
//            if (password.isEmpty()) {
//                passwordField.error = "Password is required"
//                isValid = false
//            } else if (!isValidPassword(password)) {
//                passwordField.error =
//                    "Password must be at least 8 characters, include one special character, one number, and one capital letter"
//                isValid = false
//            }
//
//            if (confirmPassword.isEmpty()) {
//                confirmPasswordField.error = "Confirm Password is required"
//                isValid = false
//            } else if (password != confirmPassword) {
//                confirmPasswordField.error = "Passwords do not match"
//                isValid = false
//            }
//
//            if (!checkBox.isChecked) {
//                Toast.makeText(this, "You must agree to the terms", Toast.LENGTH_SHORT).show()
//                isValid = false
//            }
//
//            if (isValid) {
//                // Hash the password before storing it
//                val hashedPassword = hashPassword(password)
//
//                // Determine roleId based on email domain
//                val roleId = if (email.endsWith("@bmdhs.co.za")) 2 else 3
//
//                // Insert data into SQLite database with hashed password
//                dbHelper.addUsers(firstName, lastName, dateOfBirth, email, hashedPassword, roleId)
//                Toast.makeText(this, "User Registered Successfully", Toast.LENGTH_SHORT).show()
//                clearFields(
//                    firstNameField,
//                    lastNameField,
//                    dateField,
//                    passwordField,
//                    confirmPasswordField,
//                    emailField,
//                    checkBox
//                )
//                // Navigate to SuccessReg Activity
//                val intent = Intent(this, SuccessReg::class.java)
//                startActivity(intent)
//            }
//        }
//    }

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