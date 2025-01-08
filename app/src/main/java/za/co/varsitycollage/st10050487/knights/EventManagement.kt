package za.co.varsitycollage.st10050487.knights

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import za.co.varsitycollage.st10050487.knights.databinding.ActivityEventManagementBinding

class EventManagement : AppCompatActivity() {
    private var mainMenu: Menu? = null
    private lateinit var binding: ActivityEventManagementBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private var roleId: Int = -1
    private var userPrivileges: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEventManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the ROLE_ID and user privileges from the intent
        roleId = intent.getIntExtra("ROLE_ID", -1)
        userPrivileges = intent.getStringExtra("USER_PRIVILEGES")

        if (roleId == -1) {
            Log.e("EventManagement", "ROLE_ID not found in intent")
        } else {
            Log.d("EventManagement", "ROLE_ID: $roleId")
        }

        val drawerLayout = binding.drawerLayout
        val navView = binding.navView

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


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
                        Log.e("EventManagement", "Access denied to Sport Management")
                    }
                }
                R.id.nav_event_management -> {
                    if (roleId == 1 || roleId == 3 || userPrivileges?.contains("EVENT_MANAGEMENT") == true) {
                        val intent = Intent(this, EventManagement::class.java)
                        intent.putExtra("ROLE_ID", roleId)
                        startActivity(intent)
                    } else {
                        showToast("Access denied to Event Management")
                        Log.e("EventManagement", "Access denied to Event Management")
                    }
                }
                R.id.nav_shop -> {
                    if (roleId == 1 || userPrivileges?.contains("SHOP") == true) {
                        val intent = Intent(this, DisplayCatalogProducts::class.java)
                        intent.putExtra("ROLE_ID", roleId)
                        startActivity(intent)
                    } else {
                        showToast("AAccess denied to Shop")
                        Log.e("EventManagement", "Access denied to Shop")
                    }
                }
                R.id.nav_profile -> {
                    if (roleId == 1 || roleId == 2 || userPrivileges?.contains("GENERATE_REPORTS") == true) {
                        val intent = Intent(this, PlayerProfileView::class.java)
                        intent.putExtra("ROLE_ID", roleId)
                        startActivity(intent)
                    } else {
                        showToast("Access denied to Player Profile")
                        Log.e("EventManagement", "Access denied to Player Profile")
                    }
                }
                R.id.nav_player_profiles -> {
                    if (roleId == 1 || roleId == 2 || userPrivileges?.contains("PLAYER_PROFILES") == true) {
                        val intent = Intent(this, PlayerProfile::class.java)
                        intent.putExtra("ROLE_ID", roleId)
                        startActivity(intent)
                    } else {
                        showToast("Access denied to Player Profile")
                        Log.e("EventManagement", "Access denied to Player Profiles")
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

        if (savedInstanceState == null) {
            val fragment = EventCardFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.event_card_container, fragment)
                .commitNow()
        }
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.btnCreateEvent.setOnClickListener {
            val intent = Intent(this, CreateEvent::class.java)
            startActivity(intent)
        }
        binding.btnDelete.setOnClickListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.event_card_container) as EventCardFragment
            fragment.deleteSelectedEvents()
        }

        binding.btnCancel.setOnClickListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.event_card_container) as EventCardFragment
            fragment.clearSelection()
            showDeleteMenu(false)
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

    fun showDeleteMenu(show: Boolean) {
        binding.selectLayout.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun updateDeleteButton(count: Int) {
        binding.btnDelete.text = "Delete ($count) Items"
    }
}
