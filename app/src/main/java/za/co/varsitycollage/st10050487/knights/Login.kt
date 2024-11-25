package za.co.varsitycollage.st10050487.knights

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import za.co.varsitycollage.st10050487.knights.databinding.ActivityLoginBinding


class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var regOp: Button
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private var loginAttempts = 0
    private val MAX_ATTEMPTS = 5
    private var lockoutEndTime = 0L
    // private val LOCKOUT_DURATION_MS = 30L * 1000 // 30 seconds
    private val LOCKOUT_DURATION_MS = 300000L // 5 minutes

    private lateinit var lockout: SecureAttempts
    private lateinit var googleSignIn: ImageView
    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    val database = Firebase.database

    private var RC_SIGN_IN = 20

    //Creating an instance of the Validations class
    private lateinit var valid: Validations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the binding property
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize SecureAttempts
        lockout = SecureAttempts(this)

        emailEditText = binding.emailTxt
        passwordEditText = binding.passwordTxt
        loginButton = binding.LoginBtn
        regOp = binding.RegisterBtn
        googleSignIn = binding.btnGoogle
        // Initializing the validation class
        valid = Validations()

        // Create a GoogleSignInOptions object with the default sign-in options
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Create a GoogleSignInClient object with the GoogleSignInOptions object
        gsc = GoogleSignIn.getClient(this, gso)


        //Set an onClickListener for the Google Sign-In button
        googleSignIn.setOnClickListener {
            // Handle Google Sign-In logic here
            Toast.makeText(this, "Google Sign-In Clicked", Toast.LENGTH_SHORT).show()
            //      signIn();
        }


        // Set click listener for the Register button
        regOp.setOnClickListener {
            // Redirect to the Register activity
            val intent = Intent(this, StudentParentReg::class.java)
            startActivity(intent)
        }

        // Set click listener for the Login button
        loginButton.setOnClickListener {

            getUserInput()
            //loginUser()
        }
    }

    private fun Validation(): Boolean {
        var isValid = true
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        // Validating email
        if (email.isEmpty()) {
            emailEditText.error = "Email is required"
            isValid = false
        }
        if (!valid.CheckEmail(email)) {
            emailEditText.error = "Invalid email format"
            isValid = false
        }
        // Validate password
        if (password.isEmpty()) {
            Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show()
            isValid = false
        }
        return isValid
    }

    // A method to get user input
    private fun getUserInput() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (Validation()) {

            if (isLockedOut()) {
                loginAttempts = 0
                Toast.makeText(
                    this,
                    "Account is locked. Try again in 5 minutes.",
                    Toast.LENGTH_LONG
                ).show()
                return
            }

            // Check if user exists in the database
            val dbHelper = DBHelper(this)
            val userId = dbHelper.validateUser(email, password)
            if (userId != null) {
                // Get the ROLE_ID of the user
                val roleId = dbHelper.getRoleId(userId)
                loginAttempts = 0 // Reset attempts on successful login
                val intent = when (roleId) {
                    1 -> Intent(this, AdminHome::class.java)
                    2, 3 -> Intent(this, HomeScreen::class.java)
                    else -> null
                }

                if (intent != null) {
                    // Passing the USER_ID to the respective Activity
                    intent.putExtra("USER_ID", userId)
                    startActivity(intent)
                    // Finishing the login activity once the user is logged in
                    finish()
                } else {
                    Toast.makeText(this, "Invalid role", Toast.LENGTH_SHORT).show()
                }
            } else
            {
                if (loginAttempts >= MAX_ATTEMPTS) {
                    lockoutEndTime = System.currentTimeMillis() + LOCKOUT_DURATION_MS
                    Toast.makeText(
                        this,
                        "Too many attempts. Account locked for 5 minutes.",
                        Toast.LENGTH_LONG
                    ).show()
                    saveSuspiciousActivity(email, "Too many failed login attempts")
                }
                else {
                    loginAttempts++
                    // User does not exist or incorrect password
                    Toast.makeText(
                        this,
                        ("Invalid credentials. Attempt $loginAttempts").toString() + " of " + MAX_ATTEMPTS,
                        Toast.LENGTH_SHORT
                    ).show()
                    //Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }


    private fun saveSuspiciousActivity(email: String, activityDescription: String) {
        val dbHelper = DBHelper(this)
        val userId =
            dbHelper.getUserIdByEmail(email)
        if (userId != null) {
            val timestamp = System.currentTimeMillis()
            dbHelper.addSuspiciousActivity(userId, activityDescription, timestamp)
        }
        else
        {
            val timestamp = System.currentTimeMillis()
            dbHelper.addSuspiciousActivity(email, activityDescription, timestamp)
        }
    }
    private fun isLockedOut(): Boolean {
        val currentTime = System.currentTimeMillis()
        return currentTime < lockoutEndTime
    }

//    override fun onPause() {
//        super.onPause()
//        lockout.saveInt("loginAttempts", loginAttempts)
//        lockout.saveLong("lockoutEndTime", lockoutEndTime)
//    }
//
//    override fun onResume() {
//        super.onResume()
//        loginAttempts = lockout.getInt("loginAttempts", 0)
//        lockoutEndTime = lockout.getLong("lockoutEndTime", 0)
//    }

//    private fun loginUser() {
//        // if the user inputs are valid
//        if (Validation()) {
//            val email = emailEditText.text.toString().trim()
//            val password = passwordEditText.text.toString().trim()
//            // Authenticate the user
//            authenticateUser(email, password) { isAuthenticated ->
//                // If the user is authenticated, get the user data
//                if (isAuthenticated) {
//                    val user = auth.currentUser
//                    user?.let {
//                        val uid = user.uid
//
//                        database.reference.child("users").child(uid).get()
//                            .addOnSuccessListener { dataSnapshot ->
//                                val intent = Intent(
//                                    this,
//                                    SuccessReg::class.java
//                                )//intent.putExtra("USER_FIRST_NAME", firstName)
//
//                                startActivity(intent)
//                                finish()
//                            }.addOnFailureListener {
//                                Toast.makeText(
//                                    this,
//                                    "Failed to retrieve user data",
//                                    Toast.LENGTH_SHORT
//                                )
//                                    .show()
//                            }
//                    }
//                    val intent = Intent(this, SuccessReg::class.java)
//                    startActivity(intent)
//                }
//                // If the user is not authenticated, show a toast message
//                else {
//                    //      Logger.logSuspiciousActivity("Invalid login attempt for email: $email")
//
//                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
//
//    //Method to handle the Google Sign-In logic
//    private fun signIn() {
//        val signInIntent = gsc.signInIntent
//        startActivityForResult(signInIntent, RC_SIGN_IN)
//    }
//
//    //Method to Handle the Result of the Sign-In Attempt
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        // Check if the request code matches the sign-in request code
//        if (requestCode == RC_SIGN_IN) {
//            // Retrieve the sign-in task from the intent data
//            val task: Task<GoogleSignInAccount> =
//                GoogleSignIn.getSignedInAccountFromIntent(data)
//
//            try {
//                // Attempt to get the GoogleSignInAccount from the task
//                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
//                // If successful, authenticate with the obtained ID token
//                auth(account.idToken!!)
//            } catch (e: ApiException) {
//
//                // If an exception occurs, show a toast message indicating sign-in failure
//                Toast.makeText(this, "Google Sign-In Failed", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    //Method to Authenticate the User with Email and Password
//    private fun authenticateUser(email: String, password: String, callback: (Boolean) -> Unit) {
//        // Authenticate the user with the provided email and password
//        auth.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) { task ->
//                // If the task is successful, show a toast message indicating authentication success
//                if (task.isSuccessful) {
//                    callback(true)
//                    Toast.makeText(this, "Authentication successful.", Toast.LENGTH_SHORT)
//                        .show()
//                }
//                // If the task is not successful, show a toast message indicating authentication failure
//                else {
//
//                    callback(false)
//                    Toast.makeText(
//                        this,
//                        "Authentication failed: ${task.exception?.message}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//    }
//
//    //Method to Authenticate the User for SSO Login/Registration
//    private fun auth(idToken: String) {
//        // Create a credential using the ID token
//        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
//        // Sign in with the credential
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // If sign-in is successful, get the current user
//                    val user = auth.currentUser
//                    if (user != null) {
//
//                        // Check if the user is registered in the database
//                        database.reference.child("users").child(user.uid).get()
//                            .addOnSuccessListener {
//
//                                if (it.exists()) {
//                                    // If the user is registered, navigate to SuccessActivity
//                                    val intent = Intent(this, HomeActivity::class.java)
//                                    startActivity(intent)
//                                    finish()
//                                } else if (!it.exists()) {
//                                    // Navigate to Complete Registration activity
//                                    val intent =
//                                        Intent(baseContext, HomeActivity::class.java)
//                                    startActivity(intent)
//                                    finish();
//                                }
//                            }
//                    }
//                } else {
//
//                      }
//            }
//    }


}