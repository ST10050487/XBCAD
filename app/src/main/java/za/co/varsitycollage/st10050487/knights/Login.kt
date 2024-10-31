package za.co.varsitycollage.st10050487.knights

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Button
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.database
import org.mindrot.jbcrypt.BCrypt
import za.co.varsitycollage.st10050487.knights.databinding.ActivityLoginBinding
import kotlin.math.log10

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
//    private lateinit var emailTxt: EditText
//    private lateinit var passwordTxt: EditText
//    private lateinit var loginBtn: Button
//    private lateinit var registerBtn: Button
    private lateinit var dbHelper: DBHelper

    private lateinit var regOp: Button
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button


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




        auth = FirebaseAuth.getInstance()
        emailEditText = binding.emailTxt
        passwordEditText = binding.passwordTxt
        loginButton = binding.LoginBtn
        regOp = binding.RegisterBtn

        googleSignIn = binding.btnGoogle


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
            signIn();
        }


            // Initializing views
//            emailTxt = findViewById(R.id.emailTxt)
//            passwordTxt = findViewById(R.id.passwordTxt)
//            loginBtn = findViewById(R.id.LoginBtn)
//            registerBtn = findViewById(R.id.RegisterBtn)
//            dbHelper = DBHelper(this)

            // Initializing the validation class
            valid = Validations()

            // Set click listener for the Register button
            regOp.setOnClickListener {
                // Redirect to the Register activity
                val intent = Intent(this, StudentParentReg::class.java)
                startActivity(intent)
            }

        // Set click listener for the Login button
        loginButton.setOnClickListener {
            //getUserInput()
            loginUser()
        }
    }
    private fun loginUser() {
        // if the user inputs are valid
        if (Validation()) {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            // Authenticate the user
            authenticateUser(email, password) { isAuthenticated ->
                // If the user is authenticated, get the user data
                if (isAuthenticated) {
                    val user = auth.currentUser
                    user?.let {
                        val uid = user.uid
                        database.reference.child("users").child(uid).get()
                            .addOnSuccessListener { dataSnapshot ->
                                val intent = Intent(
                                    this,
                                    SuccessReg::class.java
                                )//intent.putExtra("USER_FIRST_NAME", firstName)

                                startActivity(intent)
                                finish()
                            }.addOnFailureListener {
                            Toast.makeText(this, "Failed to retrieve user data", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    val intent = Intent(  this,SuccessReg::class.java)
                    startActivity(intent)
                }
                // If the user is not authenticated, show a toast message
                else {
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
        //Method to handle the Google Sign-In logic
        private fun signIn() {
            val signInIntent = gsc.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        //Method to handle the result of the sign-in attempt
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            // Check if the request code matches the sign-in request code
            if (requestCode == RC_SIGN_IN) {
                // Retrieve the sign-in task from the intent data
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(data)

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

        //Method to authenticate the user for normal login
        private fun authenticateUser(email: String, password: String, callback: (Boolean) -> Unit) {
            // Authenticate the user with the provided email and password
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    // If the task is successful, show a toast message indicating authentication success
                    if (task.isSuccessful) {
                        callback(true)
                        Toast.makeText(this, "Authentication successful.", Toast.LENGTH_SHORT)
                            .show()
                    }
                    // If the task is not successful, show a toast message indicating authentication failure
                    else {
                        callback(false)
                        Toast.makeText(
                            this,
                            "Authentication failed: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        //Method to authenticate the user for SSO login / registration
        private fun auth(idToken: String) {
            // Create a credential using the ID token
            val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
            // Sign in with the credential
            auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // If sign-in is successful, get the current user
                        val user = auth.currentUser
                        if (user != null) {
                            // Check if the user is registered in the database
                            database.reference.child("users").child(user.uid).get()
                                .addOnSuccessListener {

                                    if (it.exists()) {
                                        // If the user is registered, navigate to SuccessActivity
                                        val intent = Intent(this,HomeActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else if (!it.exists()) {
                                        // Navigate to Complete Registration activity
                                        val intent =
                                            Intent(baseContext, HomeActivity::class.java)
                                        startActivity(intent)
                                        finish();
                                    }
                                }
                        }
                    } else {
                        // If sign-in fails, display a message to the user
                        Toast.makeText(
                            this,
                            "Authentication failed: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        // A method to get user input
        private fun getUserInput() {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val encryptedPassword: String

       if (Validation()) {
           // Hashing the inputted password
           //encryptedPassword = hashPassword(password)
           // Check if user exists in the database
           val dbHelper = DBHelper(this)
           val userId = dbHelper.validateUser(email, password)

           if (userId != null) {
               // Get the ROLE_ID of the user
               val roleId = dbHelper.getRoleId(userId)

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
           } else {
               // User does not exist or incorrect password
               emailEditText.error = "Invalid email or password"
               passwordEditText.error = "Invalid email or password"
               Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
           }
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

//    // A method to hash the entered password
//    fun hashPassword(password: String): String {
//        return BCrypt.hashpw(password, BCrypt.gensalt())
//    }
    }


