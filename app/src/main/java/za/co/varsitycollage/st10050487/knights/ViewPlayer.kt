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
import za.co.varsitycollage.st10050487.knights.databinding.ActivityUpdateProductBinding
import za.co.varsitycollage.st10050487.knights.databinding.ActivityViewPlayerBinding

class ViewPlayer : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private var userId: Int = 1
    private var playerId: Int = 0
    private var profileImageHolder: ByteArray? = null
    private lateinit var binding: ActivityViewPlayerBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Getting the userId from the Intent
        //userId = intent.getIntExtra("USER_ID", 0)

        // Initializing your database helper
        dbHelper = DBHelper(this)

        binding.backBtn.setOnClickListener {
           Toast.makeText(this, "Back button clicked", Toast.LENGTH_SHORT).show()

        }

        if (savedInstanceState == null) {
            val fragment = PlayerCardFragment.newInstance(userId)
            supportFragmentManager.beginTransaction()
                .replace(R.id.player_card_container, fragment)
                .commitNow()

           // getDummyPlayerData()
        }
        loadPlayerData()
    }

    private fun loadPlayerData() {
        val playerProfile = dbHelper.getPlayerProfile(userId)

        if (playerProfile != null) {

            binding.txtAgeGroup.text = "Age Group: ${playerProfile.ageGroup}"
            binding.txtPosition.text = "Position: ${playerProfile.position}"
            binding.txtHeight.text =  "Height: ${playerProfile.height}"


            // Set the product picture to image holder variable
            profileImageHolder = playerProfile?.profilePicture

            // if product picture is not null
            if ( profileImageHolder!= null)
            { // set UI image to product picture
                binding.profilePicture.setImageBitmap( profileImageHolder?.size?.let { BitmapFactory.decodeByteArray( profileImageHolder, 0, it) })
            }
        }
        else {
            // Displaying a toast message if the player data is not found
            Toast.makeText(this, "Player data not found", Toast.LENGTH_SHORT).show()
        }
    }
}