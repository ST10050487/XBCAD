package za.co.varsitycollage.st10050487.knights

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
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

class AdminHome : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    private var roleId: Int = -1
    private var userPrivileges: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

        // Retrieve the ROLE_ID and user privileges from the intent
        roleId = intent.getIntExtra("ROLE_ID", -1)
        userPrivileges = intent.getStringExtra("USER_PRIVILEGES")

        if (roleId == -1) {
            Log.e("HomeScreen", "ROLE_ID not found in intent")
            // Handle the case where the ROLE_ID is not found
        } else {
            Log.d("HomeScreen", "ROLE_ID: $roleId")
        }

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navView = findViewById<NavigationView>(R.id.nav_view)
        val btnShop = findViewById<LinearLayout>(R.id.btn_shop)
        val btnSport = findViewById<LinearLayout>(R.id.btn_sport)
        val btnEvents = findViewById<LinearLayout>(R.id.btn_events)
        val btnPlayer = findViewById<LinearLayout>(R.id.btn_players)
        val btnAddFixture = findViewById<LinearLayout>(R.id.btn_addFixture)
        val btnAddEvent = findViewById<LinearLayout>(R.id.btn_addEvent)

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
        setupNavigationView(navView)

        roleId = 1
        btnShop.setOnClickListener {
            if (roleId == 1 || userPrivileges?.contains("SHOP") == true) {
                val intent = Intent(this, DisplayCatalogProducts::class.java)
                intent.putExtra("ROLE_ID", roleId)
                startActivity(intent)
            } else {
                showToast("Access denied to Shop")
                Log.e("AdminHome", "Access denied to Shop")
            }
        }
        btnSport.setOnClickListener{
            if(roleId == 1 || userPrivileges?.contains("SPORT_MANAGEMENT") == true){
                val intent = Intent(this, AdminSportsFixtures::class.java)
                intent.putExtra("ROLE_ID", roleId)
                startActivity(intent)
            } else {
                showToast("Access denied to Sport Management")
                Log.e("AdminHome", "Access denied to Sport Management")
            }
        }
        btnEvents.setOnClickListener{
            if(roleId == 1 || userPrivileges?.contains("EVENT_MANAGEMENET") == true){
                val intent = Intent(this, EventManagement::class.java)
                intent.putExtra("ROLE_ID", roleId)
                startActivity(intent)
            }else
            {
                showToast("Access denied to Event Management")
                Log.e("AdminHome", "Access denied to Event Management")
            }
        }
        btnPlayer.setOnClickListener{
            if(roleId == 1 || userPrivileges?.contains("PLAYER_PROFILES") == true){
                val intent = Intent(this, ViewAllPlayerProfiles:: class.java)
                intent.putExtra("ROLE_ID", roleId)
                startActivity(intent)
            }else
            {
                showToast("Access denied to Player Profiles")
                Log.e("AdminHome", "Access denied to Player Profiles")
            }
        }
        btnAddFixture.setOnClickListener {
            if (roleId == 1 || userPrivileges?.contains("SPORT_MANAGEMENT") == true) {
                val intent = Intent(this, CreateSportFixture::class.java)
                intent.putExtra("ROLE_ID", roleId)
                startActivity(intent)
            } else {
                showToast("Access denied to Add Fixture")
                Log.e("AdminHome", "Access denied to Add Fixture")
            }
        }
        btnAddEvent.setOnClickListener {
            if (roleId == 1 || userPrivileges?.contains("EVENT_MANAGEMENT") == true) {
                val intent = Intent(this, CreateEvent::class.java)
                intent.putExtra("ROLE_ID", roleId)
                startActivity(intent)
            } else {
                showToast("Access denied to Add Event")
                Log.e("AdminHome", "Access denied to Add Event")
            }
        }
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

    private fun setupNavigationView(navView: NavigationView) {
        val headerView = navView.getHeaderView(0)
        val headerImageView = headerView?.findViewById<ImageView>(R.id.imageView3)
        setupImageView(headerImageView, R.drawable.banner_frame)

        val logoImageView = findViewById<ImageView>(R.id.Logo)
        setupImageView(logoImageView, R.drawable.trans_logo_figma)

        val interactionsImageView = findViewById<LinearLayout>(R.id.btn_interactions).findViewById<ImageView>(R.id.imageView)
        setupImageView(interactionsImageView, R.drawable.ic_interactions)

        val createAdminImageView = findViewById<LinearLayout>(R.id.btn_CreateAdmin).findViewById<ImageView>(R.id.imageView)
        setupImageView(createAdminImageView, R.drawable.ic_create_admin)

        val reviewProfilesImageView = findViewById<LinearLayout>(R.id.btn_reviewProfiles).findViewById<ImageView>(R.id.imageView)
        setupImageView(reviewProfilesImageView, R.drawable.ic_player_review)

        val shopImageView = findViewById<LinearLayout>(R.id.btn_shop).findViewById<ImageView>(R.id.imageView)
        setupImageView(shopImageView, R.drawable.ic_shop_icon)

        val sportImageView = findViewById<LinearLayout>(R.id.btn_sport).findViewById<ImageView>(R.id.imageView)
        setupImageView(sportImageView, R.drawable.ic_sport_management_icon)

        val eventsImageView = findViewById<LinearLayout>(R.id.btn_events).findViewById<ImageView>(R.id.imageView)
        setupImageView(eventsImageView, R.drawable.ic_event_management_icon)

        val playersImageView = findViewById<LinearLayout>(R.id.btn_players).findViewById<ImageView>(R.id.imageView)
        setupImageView(playersImageView, R.drawable.ic_players_profile_icon)

        val addFixtureImageView = findViewById<LinearLayout>(R.id.btn_addFixture).findViewById<ImageView>(R.id.imageView)
        setupImageView(addFixtureImageView, R.drawable.ic_new_event)

        val addEventImageView = findViewById<LinearLayout>(R.id.btn_addEvent).findViewById<ImageView>(R.id.imageView)
        setupImageView(addEventImageView, R.drawable.ic_new_fixture)
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