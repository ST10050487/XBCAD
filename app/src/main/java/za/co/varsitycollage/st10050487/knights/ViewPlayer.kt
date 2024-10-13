package za.co.varsitycollage.st10050487.knights

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class ViewPlayer : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private var userId: Int = 0
    private var playerId: Int = 0
    private lateinit var profilePicture: ImageView
    private lateinit var backBtn: LinearLayout

    private lateinit var ageGroups: TextView
    private lateinit var positions: TextView
    private lateinit var sports: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_player)


        // Getting the userId from the Intent
        userId = intent.getIntExtra("USER_ID", 0)
        profilePicture = findViewById(R.id.profilePicture)
        // Initializing your database helper
        dbHelper = DBHelper(this)
        backBtn = findViewById(R.id.back_btn)
        ageGroups = findViewById(R.id.txtAgeGroup)
        positions = findViewById(R.id.txtPosition)
        sports = findViewById(R.id.txtSports)

        backBtn.setOnClickListener {
            finish()
            val intent = Intent(this, EditFixture::class.java)
            startActivity(intent)
        }

        if (savedInstanceState == null) {
            val fragment = PlayerCardFragment.newInstance(userId)
            supportFragmentManager.beginTransaction()
                .replace(R.id.player_card_container, fragment)
                .commitNow()
            //getPlayerData()
            getDummyPlayerData()
        }
    }
    private fun getDummyPlayerData() {
        // Dummy data
        positions.text = "Forward"
        ageGroups.text = "Under 17's"
        sports.text = "Rugby"

    }

    private fun getPlayerData() {
        val playerProfile = dbHelper.getPlayerProfile(userId)
        if (playerProfile != null) {
            val positionsList = playerProfile.position
            val ageGroupList =  playerProfile.ageGroup
            val sportsList = playerProfile.nickname
            //val positionsString = positionsList.joinToString(", ")
           // val ageGroupString = ageGroupList.joinToString(", ")
            positions.text = positionsList
            ageGroups.text = ageGroupList
            sports.text = sportsList

            // Load profile picture if available
            val profilePictureBlob = playerProfile.profilePicture
            if (profilePictureBlob != null) {
                val profilePictureBitmap = BitmapFactory.decodeByteArray(profilePictureBlob, 0, profilePictureBlob.size)
                profilePicture.setImageBitmap(profilePictureBitmap)
            }
        }
        else {
            // Displaying a toast message if the player data is not found
            Toast.makeText(this, "Player data not found", Toast.LENGTH_SHORT).show()
        }
    }
}