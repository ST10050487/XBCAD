package za.co.varsitycollage.st10050487.knights

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

class EventManagement : AppCompatActivity() {
    private var mainMenu: Menu? = null
    private lateinit var btnDelete : Button
    private lateinit var btnCancel : Button
    private lateinit var SelectLayout : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_event_management)

       // GetDummyData()

        if (savedInstanceState == null) {
            val fragment = EventCardFragment.newInstance("param1", "param2")
            supportFragmentManager.beginTransaction()
                .replace(R.id.event_card_container, fragment)
                .commitNow()

        }
        SelectLayout = findViewById(R.id.selectLayout)
        btnDelete = findViewById(R.id.btnDelete)
        btnDelete.setOnClickListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.event_card_container) as EventCardFragment
            fragment.deleteSelectedEvents()
        }

        btnCancel = findViewById(R.id.btnCancel)
        btnCancel.setOnClickListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.event_card_container) as EventCardFragment
            fragment.clearSelection()
            showDeleteMenu(false)
        }

    }




    private fun GetDummyData() {
        val dbHelper = DBHelper(this)
        dbHelper.addEvents("Knights' Golf Day", "2024-05-26", "20:00", "Bridge Rd, Milnerton", 50.0, 1)
        dbHelper.addEvents("Market Day", "2024-06-15", "18:00", "The Barnyard Theatre",30.0, 2)
        dbHelper.addEvents("Nineties vs Noughties", "2024-07-10", "18:00", "La Monumental", 25.0, 3)
        dbHelper.addEvents("Knights' Tennis Tournament", "2024-08-05", "09:00", "Green Point Tennis Club", 20.0, 4)
        dbHelper.addEvents("Knights' Swimming Gala", "2024-09-20", "14:00", "St James Tidal Pool", 15.0, 5)
    }
    fun showDeleteMenu(show: Boolean) {
        SelectLayout.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun updateDeleteButton(count: Int) {
        btnDelete.text = "Delete ($count) Items"
    }
}