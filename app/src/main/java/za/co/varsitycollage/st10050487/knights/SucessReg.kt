package za.co.varsitycollage.st10050487.knights

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class SuccessReg : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_sucess_reg)

        // Set up the OK button click listener
        val okButton = findViewById<ImageButton>(R.id.login_backfunction)
        okButton.setOnClickListener {
            // Navigate back to StudentParentReg activity
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }
}