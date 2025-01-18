package za.co.varsitycollage.st10050487.knights

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.content.res.Resources
import android.graphics.Bitmap


class AdminHome : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    private var roleId: Int = -1
    private var userId: Int = -1
    private var userPrivileges: String? = null
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

        // Retrieve the ROLE_ID, USER_ID, and user privileges from the intent
        roleId = intent.getIntExtra("ROLE_ID", -1)
        userId = intent.getIntExtra("USER_ID", -1)
        userPrivileges = intent.getStringExtra("USER_PRIVILEGES")

        if (roleId == -1) {
            Log.e("HomeScreen", "ROLE_ID not found in intent")
            // Handle the case where the ROLE_ID is not found
        } else {
            Log.d("HomeScreen", "ROLE_ID: $roleId")
        }

        dbHelper = DBHelper(this)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navView = findViewById<NavigationView>(R.id.nav_view)
        val btnShop = findViewById<LinearLayout>(R.id.btn_shop)
        val btn_CreateAdmin = findViewById<LinearLayout>(R.id.btn_CreateAdmin)
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

        // Load user details and update UI
        displayUserDetails()

        // Displaying the current date
        val dateTextView: TextView = findViewById(R.id.txtDate)
        val currentDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date())
        dateTextView.text = currentDate

        btn_CreateAdmin.setOnClickListener {
            if (roleId == 1 || userPrivileges?.contains("GRANT_PRIVILEGES") == true) {
                val intent = Intent(this, AssignPrivileges::class.java)
                intent.putExtra("USER_ID", userId)
                intent.putExtra("ROLE_ID", roleId)
                startActivity(intent)
            } else {
                showToast("Access denied to Create Admin")
                Log.e("AdminHome", "Access denied to Create Admin")
            }
        }
        btnShop.setOnClickListener {
            if (roleId == 1 || userPrivileges?.contains("SHOP_MANAGEMENT") == true) {
                val intent = Intent(this, DisplayCatalogProducts::class.java)
                intent.putExtra("USER_ID", userId)
                intent.putExtra("ROLE_ID", roleId)
                startActivity(intent)
            } else {
                showToast("Access denied to Shop")
                Log.e("AdminHome", "Access denied to Shop")
            }
        }
        btnSport.setOnClickListener {
            if (roleId == 1 || userPrivileges?.contains("SPORT_MANAGEMENT") == true) {
                val intent = Intent(this, AdminSportsFixtures::class.java)
                intent.putExtra("USER_ID", userId)
                intent.putExtra("ROLE_ID", roleId)
                startActivity(intent)
            } else {
                showToast("Access denied to Sport Management")
                Log.e("AdminHome", "Access denied to Sport Management")
            }
        }
        btnEvents.setOnClickListener {
            if (roleId == 1 || userPrivileges?.contains("EVENT_MANAGEMENT") == true) {
                val intent = Intent(this, EventManagement::class.java)
                intent.putExtra("USER_ID", userId)
                intent.putExtra("ROLE_ID", roleId)
                startActivity(intent)
            } else {
                showToast("Access denied to Event Management")
                Log.e("AdminHome", "Access denied to Event Management")
            }
        }
        btnPlayer.setOnClickListener {
            if (roleId == 1 || userPrivileges?.contains("PLAYER_PROFILES") == true) {
                val intent = Intent(this, ViewAllPlayerProfiles::class.java)
                intent.putExtra("USER_ID", userId)
                intent.putExtra("ROLE_ID", roleId)
                startActivity(intent)
            } else {
                showToast("Access denied to Player Profiles")
                Log.e("AdminHome", "Access denied to Player Profiles")
            }
        }
        btnAddFixture.setOnClickListener {
            if (roleId == 1 || userPrivileges?.contains("SPORT_MANAGEMENT") == true) {
                val intent = Intent(this, CreateSportFixture::class.java)
                intent.putExtra("USER_ID", userId)
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
                intent.putExtra("USER_ID", userId)
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
                    intent.putExtra("USER_ID", userId)
                    intent.putExtra("ROLE_ID", roleId)
                    startActivity(intent)
                }
                R.id.nav_sport_management -> {
                    if (roleId == 1 || roleId == 2 || userPrivileges?.contains("SPORT_MANAGEMENT") == true) {
                        val intent = Intent(this, AdminSportsFixtures::class.java)
                        intent.putExtra("ROLE_ID", roleId)
                        intent.putExtra("USER_ID", userId)
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
                        intent.putExtra("USER_ID", userId)
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
                        intent.putExtra("USER_ID", userId)
                        startActivity(intent)
                    } else {
                        showToast("Access denied to Shop")
                        Log.e("AdminHome", "Access denied to Shop")
                    }
                }
                R.id.nav_profile -> {
                    if (roleId == 1 || roleId == 2 || userPrivileges?.contains("GENERATE_REPORTS") == true) {
                        val intent = Intent(this, User::class.java)
                        intent.putExtra("USER_ID", userId)
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
                        intent.putExtra("USER_ID", userId)
                        startActivity(intent)
                    } else {
                        showToast("Access denied to Player Profile")
                        Log.e("AdminHome", "Access denied to Player Profiles")
                    }
                }
                R.id.nav_create_admin -> {
                    if (roleId == 1 || userPrivileges?.contains("GRANT_PRIVILEGES") == true) {
                        val intent = Intent(this, AssignPrivileges::class.java)
                        intent.putExtra("USER_ID", userId)
                        intent.putExtra("ROLE_ID", roleId)
                        startActivity(intent)
                    } else {
                        showToast("Access denied to Create Admin")
                        Log.e("AdminHome", "Access denied to Create Admin")
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

    override fun onResume() {
        super.onResume()
        displayUserDetails()
    }

    private fun displayUserDetails() {
        val user = dbHelper.getUser(userId)
        if (user != null) {
            val navView = findViewById<NavigationView>(R.id.nav_view)
            val headerView = navView.getHeaderView(0)
            val userNameTextView = headerView.findViewById<TextView>(R.id.user_name)
            val userEmailTextView = headerView.findViewById<TextView>(R.id.user_email)
            val userImageView = headerView.findViewById<ImageView>(R.id.profile_image)
            val txtUsername = findViewById<TextView>(R.id.txtUsername)

            userNameTextView.text = user.name + " " + user.surname
            userEmailTextView.text = user.email
            txtUsername.text = user.name + " " + user.surname

            val profilePicture = user.profilePicture
            if (profilePicture != null && profilePicture.isNotEmpty()) {
                val bitmap = BitmapFactory.decodeByteArray(profilePicture, 0, profilePicture.size)
                userImageView.setImageBitmap(bitmap)
            } else {
                userImageView.setImageResource(R.drawable.user_admin) // Default image
            }
        }
    }

    private fun setupNavigationView(navView: NavigationView) {
    val headerView = navView.getHeaderView(0)
    val profileSection = headerView.findViewById<LinearLayout>(R.id.admin_profile_section)
    profileSection.setOnClickListener {
        val intent = Intent(this, User::class.java)
        intent.putExtra("USER_ID", userId)
        intent.putExtra("ROLE_ID", roleId)
        startActivity(intent)
    }

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
    fun decodeSampledBitmapFromResource(res: Resources, resId: Int, reqWidth: Int, reqHeight: Int): Bitmap {
        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeResource(res, resId, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(res, resId, options)
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }
}