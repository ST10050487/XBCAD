package za.co.varsitycollage.st10050487.knights

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import za.co.varsitycollage.st10050487.knights.databinding.ActivityEventManagementBinding

class EventManagement : AppCompatActivity() {
    private var mainMenu: Menu? = null
    private lateinit var binding: ActivityEventManagementBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEventManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)


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
    fun showDeleteMenu(show: Boolean) {
        binding.selectLayout.visibility = if (show) View.VISIBLE else View.GONE
    }
    fun updateDeleteButton(count: Int) {
        binding.btnDelete.text = "Delete ($count) Items"
    }
}