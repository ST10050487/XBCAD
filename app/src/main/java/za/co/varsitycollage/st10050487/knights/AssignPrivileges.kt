package za.co.varsitycollage.st10050487.knights

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class AssignPrivileges : AppCompatActivity() {
    private lateinit var userListView: ListView
    private lateinit var privilegesContainer: LinearLayout
    private lateinit var assignPrivilegesButton: Button
    private lateinit var searchView: SearchView
    private lateinit var dbHelper: DBHelper
    private lateinit var users: List<UserModel>
    private lateinit var privileges: List<String>
    private var selectedUserId: Int = -1
    private var roleId: Int = -1
    private var userId: Int = -1
    private var userPrivileges: String? = null
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_assign_privileges)

        // Initialize drawerLayout
        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.nav_view) // Initialize navView

        // Retrieve the ROLE_ID, USER_ID, and user privileges from the intent
        roleId = intent.getIntExtra("ROLE_ID", -1)
        userId = intent.getIntExtra("USER_ID", -1)
        userPrivileges = intent.getStringExtra("USER_PRIVILEGES")

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, AdminHome::class.java)
                    intent.putExtra("USER_ID", userId)
                    intent.putExtra("ROLE_ID", roleId)
                    intent.putExtra("USER_PRIVILEGES", userPrivileges)
                    startActivity(intent)
                }

                R.id.nav_sport_management -> {
                    if (roleId == 1 || roleId == 2 || userPrivileges?.contains("SPORT_MANAGEMENT") == true) {
                        val intent = Intent(this, AdminSportsFixtures::class.java)
                        intent.putExtra("ROLE_ID", roleId)
                        intent.putExtra("USER_ID", userId)
                        intent.putExtra("USER_PRIVILEGES", userPrivileges)
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
                        intent.putExtra("USER_PRIVILEGES", userPrivileges)
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
                        intent.putExtra("USER_PRIVILEGES", userPrivileges)
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
                        intent.putExtra("USER_PRIVILEGES", userPrivileges)
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
                        intent.putExtra("USER_PRIVILEGES", userPrivileges)
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
                        intent.putExtra("USER_PRIVILEGES", userPrivileges)
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

        dbHelper = DBHelper(this)
        userListView = findViewById(R.id.user_list_view)
        privilegesContainer = findViewById(R.id.privileges_container)
        assignPrivilegesButton = findViewById(R.id.assign_privileges_button)
        searchView = findViewById(R.id.search_view)

        loadUsers()
        loadPrivileges()

        userListView.setOnItemClickListener { _, _, position, _ ->
            val selectedUser = users[position]
            selectedUserId = selectedUser.userId
            Toast.makeText(
                this,
                "Selected user: ${selectedUser.name} ${selectedUser.surname}",
                Toast.LENGTH_SHORT
            ).show()
        }

        assignPrivilegesButton.setOnClickListener { assignPrivileges() }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    searchUsers(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    searchUsers(newText)
                }
                return false
            }
        })

        setupNavigationView(navView)
    }

    private fun loadUsers() {
        users = dbHelper.getAllUsers()
        val adapter = UserAdapter(this, users)
        userListView.adapter = adapter
    }

    private fun loadPrivileges() {
        privileges = dbHelper.getAllPrivileges()
        for (privilege in privileges) {
            val checkBox = CheckBox(this)
            checkBox.text = privilege
            privilegesContainer.addView(checkBox)
        }
    }

    private fun assignPrivileges() {
        if (selectedUserId == -1) {
            Toast.makeText(this, "Please select a user", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedPrivilegeIds = mutableListOf<Int>()
        for (i in 0 until privilegesContainer.childCount) {
            val checkBox = privilegesContainer.getChildAt(i) as CheckBox
            if (checkBox.isChecked) {
                selectedPrivilegeIds.add(i + 1) // Assuming privilege IDs are 1-based
            }
        }
        dbHelper.assignPrivilegesToUser(selectedUserId, selectedPrivilegeIds)
        Toast.makeText(this, "Privileges assigned successfully", Toast.LENGTH_SHORT).show()
    }

    private fun searchUsers(query: String) {
        users = dbHelper.searchUsers(query)
        val adapter = UserAdapter(this, users)
        userListView.adapter = adapter
    }

    private fun showToast(message: String) {
        val inflater = layoutInflater
        val layout =
            inflater.inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_container))

        val text: TextView = layout.findViewById(R.id.toast_text)
        text.text = message

        with(Toast(applicationContext)) {
            duration = Toast.LENGTH_SHORT
            view = layout
            show()
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
            //val txtUsername = findViewById<TextView>(R.id.txtUsername)

            userNameTextView.text = user.name + " " + user.surname
            userEmailTextView.text = user.email
            //txtUsername.text = user.name + " " + user.surname

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
            intent.putExtra("USER_PRIVILEGES", userPrivileges)
            startActivity(intent)
        }
    }
}