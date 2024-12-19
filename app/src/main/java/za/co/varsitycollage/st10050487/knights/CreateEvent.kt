package za.co.varsitycollage.st10050487.knights

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*

class CreateEvent : AppCompatActivity() {

    private var roleId: Int = -1
    private var userPrivileges: String? = null
    private lateinit var toggle: ActionBarDrawerToggle
    // Database Helper
    private lateinit var dbHelper: DBHelper

    // UI Components
    private lateinit var uploadImageButton: Button
    private lateinit var createEventButton: Button
    private lateinit var btnBack: androidx.appcompat.widget.Toolbar
    private lateinit var nameInput: TextInputEditText
    private lateinit var locationInput: TextInputEditText
    private lateinit var timeInput: TextInputEditText
    private lateinit var dateInput: TextInputEditText
    private lateinit var priceInput: TextInputEditText
    private lateinit var aboutInput: TextInputEditText
    private lateinit var eventImage: ImageView

    private val PICK_IMAGE_REQUEST = 1
    private var eventImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        // Retrieve the ROLE_ID and user privileges from the intent
        roleId = intent.getIntExtra("ROLE_ID", -1)
        userPrivileges = intent.getStringExtra("USER_PRIVILEGES")

        if (roleId == -1) {
            Log.e("HomeScreen", "ROLE_ID not found in intent")
            // Handle the case where the ROLE_ID is not found
        } else {
            Log.d("HomeScreen", "ROLE_ID: $roleId")
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize drawerLayout and navView
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navView = findViewById<NavigationView>(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Setup NavigationView and load the header image
        //setupNavigationView(navView)
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, AdminHome::class.java)
                    intent.putExtra("ROLE_ID", roleId)
                    startActivity(intent)
                }
                R.id.nav_sport_management -> {
                    if (roleId == 1 || roleId == 2 || userPrivileges?.contains("SPORT_MANAGEMENT") == true) {
                        val intent = Intent(this, AdminSportsFixtures::class.java)
                        intent.putExtra("ROLE_ID", roleId)
                        startActivity(intent)
                    } else {
                        showToast("Access denied to Sport Management")
                        Log.e("AdminHome", "Access denied to Sport Management")
                    }
                }
                R.id.nav_event_management -> {
                    if (roleId == 1 || roleId == 3 || userPrivileges?.contains("EVENT_MANAGEMENT") == true) {
                        val intent = Intent(this, EventManagement::class.java)
                        intent.putExtra("ROLE_ID", roleId)
                        startActivity(intent)
                    } else {
                        showToast("Access denied to Event Management")
                        Log.e("AdminHome", "Access denied to Event Management")
                    }
                }
                R.id.nav_shop -> {
                    if (roleId == 1 || userPrivileges?.contains("SHOP") == true) {
                        val intent = Intent(this, DisplayCatalogProducts::class.java)
                        intent.putExtra("ROLE_ID", roleId)
                        startActivity(intent)
                    } else {
                        showToast("Access denied to Shop")
                        Log.e("AdminHome", "Access denied to Shop")
                    }
                }
                R.id.nav_profile -> {
                    if (roleId == 1 || roleId == 2 || userPrivileges?.contains("GENERATE_REPORTS") == true) {
                        val intent = Intent(this, PlayerProfileView::class.java)
                        intent.putExtra("ROLE_ID", roleId)
                        startActivity(intent)
                    } else {
                        showToast("Access denied to Player Profile")
                        Log.e("AdminHome", "Access denied to Player Profile")
                    }
                }
                R.id.nav_player_profiles -> {
                    if (roleId == 1 || roleId == 2 || userPrivileges?.contains("PLAYER_PROFILES") == true) {
                        val intent = Intent(this, ViewAllPlayerProfiles::class.java)
                        intent.putExtra("ROLE_ID", roleId)
                        startActivity(intent)
                    } else {
                        showToast("Access denied to Player Profile")
                        Log.e("AdminHome", "Access denied to Player Profiles")
                    }
                }
                R.id.nav_logout -> {
                    val intent = Intent(this, Login::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
            }
            true
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply { type = "image/*" }
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                eventImageUri = it
                eventImage.setImageURI(it)
            }
        }
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        TimePickerDialog(this, { _, hour, minute ->
            timeInput.setText(String.format("%02d:%02d", hour, minute))
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(this, { _, year, month, dayOfMonth ->
            dateInput.setText(String.format("%02d-%02d-%04d", dayOfMonth, month + 1, year))
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun sendNotificationToUser() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("CreateEvent", "Fetching FCM token failed", task.exception)
                return@OnCompleteListener
            }

            val userToken = task.result
            if (userToken != null) {
                val title = "New Event Created"
                val body = "Event: ${nameInput.text}\nLocation: ${locationInput.text}\nDate: ${dateInput.text}\nTime: ${timeInput.text}"
                EventMessagingService().sendMessage(userToken, title, body)
                Log.d("CreateEvent", "Notification sent to user with token: $userToken")
            }
        })
    }

    private fun showToast(message: String) {
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_container))

        val text: TextView = layout.findViewById(R.id.toast_text)
        text.text = message

        with(Toast(applicationContext)) {
            duration = Toast.LENGTH_SHORT
            view = layout
            show()
        }
    }
}