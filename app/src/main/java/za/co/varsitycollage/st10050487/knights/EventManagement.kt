package za.co.varsitycollage.st10050487.knights

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

class EventManagement : AppCompatActivity() {
    private var mainMenu: Menu? = null
    private lateinit var btnDelete: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_event_management)


        if (savedInstanceState == null) {
            val fragment = EventCardFragment.newInstance("param1", "param2")
            supportFragmentManager.beginTransaction()
                .replace(R.id.event_card_container, fragment)
                .commitNow()

        }
        btnDelete = findViewById(R.id.btnDelete)
        btnDelete.setOnClickListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.event_card_container) as EventCardFragment
            fragment.deleteSelectedEvents()
        }

    }


    fun showDeleteMenu(show: Boolean) {
        btnDelete.visibility = if (show) View.VISIBLE else View.GONE
    }
}