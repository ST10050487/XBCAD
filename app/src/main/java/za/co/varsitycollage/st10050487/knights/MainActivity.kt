package za.co.varsitycollage.st10050487.knights


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import net.sqlcipher.database.SQLiteDatabase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       // SQLiteDatabase.loadLibs(this)

        // Applying edge-to-edge UI behavior
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Delay of 3 seconds (3000 milliseconds)
        Handler(Looper.getMainLooper()).postDelayed({
            // Navigate to StudentParentReg Activity after the delay
          //  val intent = Intent(this, EditTimesheet::class.java)
           //val intent = Intent(this, EventManagement::class.java)
            // val intent = Intent(this, UpdateUser::class.java)
       //     val intent = Intent(this, UpdateProduct::class.java)
           //  val intent = Intent(this, Admin_Home::class.java)
            val intent = Intent(this, Login::class.java)
           // val intent = Intent(this@MainActivity, HomeScreen::class.java)
            startActivity(intent)
            // Finishing MainActivity so that the user cannot go back to it
            finish()
        }, 3000)
    }
}
