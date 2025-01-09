package za.co.varsitycollage.st10050487.knights

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout

class ViewAllPlayerProfiles : AppCompatActivity() {


    private lateinit var rvProfiles: RecyclerView
    private lateinit var llEmptyState: LinearLayout
    private lateinit var dbHelper: DBHelper
    private var roleId: Int = -1
    private var userId: Int = -1
    private var userPrivileges: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_player_profiles)

        // Retrieve the ROLE_ID, USER_ID, and user privileges from the intent
        roleId = intent.getIntExtra("ROLE_ID", -1)
        userId = intent.getIntExtra("USER_ID", -1)
        userPrivileges = intent.getStringExtra("USER_PRIVILEGES")

        rvProfiles = findViewById(R.id.rv_profiles)
        llEmptyState = findViewById(R.id.ll_empty_state)
        dbHelper = DBHelper(this)

        // Fetch data from the database
        val playerProfiles = dbHelper.getAllPlayerProfiles()

        // Display profiles or empty state
        if (playerProfiles.isNotEmpty()) {
            llEmptyState.visibility = View.GONE
            rvProfiles.visibility = View.VISIBLE

            rvProfiles.layoutManager = LinearLayoutManager(this)
            rvProfiles.adapter = PlayerProfileAdapter(playerProfiles)
        } else {
            rvProfiles.visibility = View.GONE
            llEmptyState.visibility = View.VISIBLE
        }
    }
}