package za.co.varsitycollage.st10050487.knights

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       // SQLiteDatabase.loadLibs(this)
        // Initialising Firebase
        FirebaseApp.initializeApp(this)
        // Applying edge-to-edge UI behavior
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Delay of 3 seconds (3000 milliseconds)
        Handler(Looper.getMainLooper()).postDelayed({
            // Navigate to StudentParentReg Activity after the delay
            val intent = Intent(this, AdminSportsFixtures::class.java)
            startActivity(intent)
            // Finishing MainActivity so that the user cannot go back to it
            finish()
        }, 3000)
    }
}
