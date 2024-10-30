package za.co.varsitycollage.st10050487.knights

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import za.co.varsitycollage.st10050487.knights.databinding.ActivityEventManagementBinding

class EventManagement : AppCompatActivity() {
    private var mainMenu: Menu? = null
    private lateinit var dbHelper: DBHelper
    private lateinit var binding: ActivityEventManagementBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEventManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)
        dbHelper = DBHelper.getInstance(this)
        GetDummyData()
      //  dbHelper.deleteAllEvents()
        if (savedInstanceState == null) {
            val fragment = EventCardFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.event_card_container, fragment)
                .commitNow()
        }

        binding.btnDelete.setOnClickListener {
            deleteEvents()
//            val fragment = supportFragmentManager.findFragmentById(R.id.event_card_container) as EventCardFragment
//            fragment.deleteSelectedEvents()
        }

        binding.btnCancel.setOnClickListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.event_card_container) as EventCardFragment
            fragment.clearSelection()
            showDeleteMenu(false)
        }
    }
    private fun deleteEvents() {


        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Events")
        builder.setMessage("Are you sure you want to delete selected events?")

        builder.setPositiveButton("Yes") { dialog: DialogInterface, which: Int ->

            val fragment = supportFragmentManager.findFragmentById(R.id.event_card_container) as EventCardFragment
            fragment.deleteSelectedEvents()
        }
        builder.setNegativeButton("No") { dialog: DialogInterface, which: Int ->
            // Dismiss the dialog
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
   private fun GetDummyData() {

    dbHelper.addEvents("Knights' Golf Day", "2024-05-26", "20:00", "Bridge Rd, Milnerton", 50.0, null, "A fun golf day event.", 1)
    dbHelper.addEvents("Market Day", "2024-06-15", "18:00", "The Barnyard Theatre", 30.0, null, "A day to enjoy various market stalls.", 2)
    dbHelper.addEvents("Nineties vs Noughties", "2024-07-10", "18:00", "La Monumental", 25.0, null, "A nostalgic event featuring music from the 90s and 00s.", 3)
    dbHelper.addEvents("Knights' Tennis Tournament", "2024-08-05", "09:00", "Green Point Tennis Club", 20.0, null, "A competitive tennis tournament.", 4)
    dbHelper.addEvents("Knights' Swimming Gala", "2024-09-20", "14:00", "St James Tidal Pool", 15.0, null, "A swimming competition for all ages.", 5)
}

    fun showDeleteMenu(show: Boolean) {
        binding.selectLayout.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun updateDeleteButton(count: Int) {
        binding.btnDelete.text = "Delete ($count) Items"
    }
}