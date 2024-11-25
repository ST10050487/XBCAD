package za.co.varsitycollage.st10050487.knights

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import za.co.varsitycollage.st10050487.knights.databinding.ActivityGetPlayersBinding

class GetPlayers : AppCompatActivity() {
    private lateinit var binding: ActivityGetPlayersBinding
    private var mainMenu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetPlayersBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Retrieve fixtureId from Intent
        val fixtureId = intent.getIntExtra("FIXTURE_ID", 0)

        // Pass fixtureId to PlayersListFragment
        if (savedInstanceState == null) {
            val fragment = PlayersListFragment.newInstance(fixtureId)
            supportFragmentManager.beginTransaction()
                .replace(R.id.players_card_container, fragment)
                .commitNow()
        }
        binding.btnSelect.setOnClickListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.players_card_container) as? PlayersListFragment
            if (fragment != null) {
               // fragment.saveSelectedPlayers()
                val selectedPlayers = fragment.getSelectedPlayers()
                val resultIntent = Intent()
                resultIntent.putParcelableArrayListExtra("SELECTED_PLAYERS", ArrayList(selectedPlayers))
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                Log.e("GetPlayers", "PlayersListFragment not found")
            }
        }

        binding.btnCancel.setOnClickListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.players_card_container) as? PlayersListFragment
            if (fragment != null) {
                fragment.clearSelection()
                Toast.makeText(this, "Selection cleared", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("GetPlayers", "PlayersListFragment not found")
            }
        }
        binding.btnBack.setOnClickListener {
            finish()
        }

    }


    fun updateSelectButton(count: Int) {
        binding.btnSelect.text = "Select ($count) Items"
    }
}