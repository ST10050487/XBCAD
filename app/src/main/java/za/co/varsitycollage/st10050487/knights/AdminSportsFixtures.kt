package za.co.varsitycollage.st10050487.knights

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
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

class AdminSportsFixtures : AppCompatActivity() {
    private val selectedSports = mutableListOf<String>() // Store selected sports
    private var roleId: Int = -1
    private var userPrivileges: String? = null

    private lateinit var searchEditText: EditText // Declare the search EditText
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_sports_fixtures)

        // Retrieve the ROLE_ID and user privileges from the intent
        roleId = intent.getIntExtra("ROLE_ID", -1)
        userPrivileges = intent.getStringExtra("USER_PRIVILEGES")

        if (roleId == -1) {
            Log.e("HomeScreen", "ROLE_ID not found in intent")
            // Handle the case where the ROLE_ID is not found
        } else {
            Log.d("HomeScreen", "ROLE_ID: $roleId")
        }

        // Load the SportsFixturesHomeScreenFragment into the fragment_container
        LoadingUpcomingPastFixtures(savedInstanceState)

        // Initialize the search EditText
        searchEditText = findViewById(R.id.search_fixtures)

        // Set up back button listener
        setupBackButton()

        NavigationToFixtures()

        // Set up search listener
        setupSearchListener()

        FilterLogic()

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

    private fun setupBackButton() {
        val backButton = findViewById<LinearLayout>(R.id.back_btn)
        backButton.setOnClickListener {
            // Navigate to AdminHomePageActivity
            val intent = Intent(this, AdminHome::class.java)
            startActivity(intent)
            finish() // Optionally call finish() if you want to remove this activity from the back stack
        }
    }

    private fun NavigationToFixtures() {
        // Set up the create fixture button listener
        val createFixtureButton = findViewById<Button>(R.id.uploadBtn)
        createFixtureButton.setOnClickListener {
            // Navigate to CreateSportsActivity
            val intent = Intent(this, CreateSportFixture::class.java)
            startActivity(intent)
        }
    }

    private fun setupSearchListener() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchQuery = s.toString()
                refreshCurrentFragment(searchQuery) // Pass the search query
            }

            override fun afterTextChanged(s: Editable?) {
                // If the search query is empty, refresh to show all upcoming matches
                if (s.isNullOrEmpty()) {
                    refreshCurrentFragment()
                }
            }
        })
    }

    private fun LoadingUpcomingPastFixtures(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val fragment = SportsFixturesHomeScreenFragment(isAdmin = true) // Pass true for admin
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
    }

    private fun FilterLogic() {
        // Filter icon logic
        val filterIcon = findViewById<ImageView>(R.id.filter_icon)
        filterIcon.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.fixture_filter_popup)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val closeIcon = dialog.findViewById<ImageView>(R.id.close_icon)
            closeIcon.setOnClickListener { dialog.dismiss() }

            // Set up sport arrow click to show dropdown
            val sportArrow = dialog.findViewById<ImageView>(R.id.sport_arrow)
            sportArrow.setOnClickListener {
                showSportDropdown(dialog)
            }

            // Show Results button logic
            val showResultsButton = dialog.findViewById<ImageButton>(R.id.show_results_button)
            showResultsButton.setOnClickListener {
                // Logic to return to the previous screen
                refreshCurrentFragment()
                dialog.dismiss() // Optionally dismiss the dialog
            }

            dialog.show()
        }
    }

    private fun refreshCurrentFragment(searchQuery: String = "") {
        val fragment = upcomingMatchesFragment(isAdmin = true).apply {
            arguments = Bundle().apply {
                putStringArrayList("selectedSports", ArrayList(selectedSports))
                putString("searchQuery", searchQuery) // Pass the search query
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun showSportDropdown(parentDialog: Dialog) {
        val sportDialog = Dialog(this)
        sportDialog.setContentView(R.layout.sport_dropdown)
        sportDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Reference each CheckBox
        val soccerCheckBox = sportDialog.findViewById<CheckBox>(R.id.checkbox_soccer)
        val netballCheckBox = sportDialog.findViewById<CheckBox>(R.id.checkbox_netball)
        val rugbyCheckBox = sportDialog.findViewById<CheckBox>(R.id.checkbox_rugby)
        val hockeyCheckBox = sportDialog.findViewById<CheckBox>(R.id.checkbox_hockey)
        val cricketCheckBox = sportDialog.findViewById<CheckBox>(R.id.checkbox_cricket)
        val tennisCheckBox = sportDialog.findViewById<CheckBox>(R.id.checkbox_tennis)
        val basketballCheckBox = sportDialog.findViewById<CheckBox>(R.id.checkbox_basketball)
        val athleticsCheckBox = sportDialog.findViewById<CheckBox>(R.id.checkbox_athletics)
        val swimmingCheckBox = sportDialog.findViewById<CheckBox>(R.id.checkbox_swimming)

        // Handle OK button
        val okButton = sportDialog.findViewById<Button>(R.id.button_ok)
        okButton.setOnClickListener {
            // Clear previous selections
            selectedSports.clear()

            // Collect selected sports
            if (soccerCheckBox.isChecked) selectedSports.add("Soccer")
            if (netballCheckBox.isChecked) selectedSports.add("Netball")
            if (rugbyCheckBox.isChecked) selectedSports.add("Rugby")
            if (hockeyCheckBox.isChecked) selectedSports.add("Hockey")
            if (cricketCheckBox.isChecked) selectedSports.add("Cricket")
            if (tennisCheckBox.isChecked) selectedSports.add("Tennis")
            if (basketballCheckBox.isChecked) selectedSports.add("Basketball")
            if (athleticsCheckBox.isChecked) selectedSports.add("Athletics")
            if (swimmingCheckBox.isChecked) selectedSports.add("Swimming")

            // Show a toast or update UI with selected items
            Toast.makeText(this, "Selected: ${selectedSports.joinToString()}", Toast.LENGTH_SHORT)
                .show()

            // Now refresh the fragment and pass the selected sports
            refreshCurrentFragment()

            sportDialog.dismiss()  // Close the dropdown dialog
        }

        sportDialog.show()  // Display the dropdown dialog
    }
}