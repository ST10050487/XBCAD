package za.co.varsitycollage.st10050487.knights

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ViewPlayer : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var teams: TextView
    private lateinit var positions: TextView
    private lateinit var sports: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_player)



        dbHelper = DBHelper(this)
        teams = findViewById(R.id.txtTeam)
        positions = findViewById(R.id.txtPosition)
        sports = findViewById(R.id.txtSports)


        addDummyPlayerProfile(dbHelper.writableDatabase)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.player_card_container, PlayerCardFragment())
                .commitNow()
            getPlayerData()
        }
    }
        fun addDummyPlayerProfile(db: SQLiteDatabase) {
        val values = ContentValues()
        values.put("NAME", "John")
        values.put("SURNAME", "Marcus")
        values.put("NICKNAME", "Johnny")
        values.put("AGE", 20)
        values.put("GRADE", "11")
        values.put("HEIGHT", "64")
        values.put("POSITION", "Forward")
        values.put("DATEOFBIRTH", "2003-01-01")
        values.put("USER_ID", 121) // Assuming a user with USER_ID 1 exists
        db.insert("PLAYER_PROFILE", null, values)

            val result = db.insert("USERS", null, values)
            if(result != -1L) {
                Toast.makeText(this, "Player profile added successfully", Toast.LENGTH_SHORT).show()
            }

    }
    private fun getPlayerData() {
        val userId = 121 // Replace with the actual user ID
        val playerProfile = dbHelper.getPlayerProfileByUserId(userId)

        if (playerProfile != null) {
            val positionsList = playerProfile.positions
            val teamsList = playerProfile.teams
            val sport = playerProfile.grade

            val positionsString = positionsList.joinToString(", ")
            val teamsString = teamsList.joinToString(", ")

            positions.text = positionsString
            teams.text = teamsString
            sports.text = sport
        }
    }
}