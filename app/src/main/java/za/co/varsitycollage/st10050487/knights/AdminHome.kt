package za.co.varsitycollage.st10050487.knights

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_home)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navView = findViewById<NavigationView>(R.id.nav_view)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout)) { v, insets ->
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

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> startActivity(Intent(this, AdminHome::class.java))
                R.id.nav_sport_fixtures -> startActivity(Intent(this, DisplaySportsFixturesActivity::class.java))
                R.id.nav_events -> startActivity(Intent(this, UpcomingRecentEvents::class.java))
                R.id.nav_shop -> startActivity(Intent(this, ViewProduct::class.java))
                R.id.nav_profile -> startActivity(Intent(this, AdminHome::class.java))
                R.id.nav_player_profiles -> startActivity(Intent(this, PlayerInforList::class.java))
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
}
