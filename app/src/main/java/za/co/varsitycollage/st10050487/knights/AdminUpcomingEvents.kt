package za.co.varsitycollage.st10050487.knights

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.navigation.NavigationView

class AdminUpcomingEvents : AppCompatActivity() {
    private var roleId: Int = -1
    private var userPrivileges: String? = null
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_upcoming_events)

        // Retrieve the ROLE_ID and user privileges from the intent
        roleId = intent.getIntExtra("ROLE_ID", -1)
        userPrivileges = intent.getStringExtra("USER_PRIVILEGES")

        if (roleId == -1) {
            Log.e("HomeScreen", "ROLE_ID not found in intent")
            // Handle the case where the ROLE_ID is not found
        } else {
            Log.d("HomeScreen", "ROLE_ID: $roleId")
        }

        // Find your Fragment Container
        val fragmentContainer = findViewById<FrameLayout>(R.id.fragment_container)

        // Create an instance of your fragment
        val eventListFragment = AdminEventListFragment()

        // Add the fragment to the container
        supportFragmentManager.beginTransaction()
            .add(fragmentContainer.id, eventListFragment)
            .commit()

        // Setup navigation drawer
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navView = findViewById<NavigationView>(R.id.nav_view)

        ViewCompat.setOnApplyWindowInsetsListener(drawerLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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

    private fun setupImageView(imageView: ImageView?, drawableResId: Int) {
        if (imageView != null) {
            val requestOptions = RequestOptions()
                .override(400, 300) // Resize the image
                .centerCrop() // Crop the image to fit

            Glide.with(this)
                .load(drawableResId)
                .apply(requestOptions)
                .into(imageView)
        } else {
            Log.e("AdminHome", "ImageView is null")
        }
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