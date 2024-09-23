package za.co.varsitycollage.st10050487.knights

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class GetStarted : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started)

        // Assigning the getStartedButton to the Button with the id getStartedButton
        val getStartedButton: Button = findViewById(R.id.getStartedButton)

        // Setting the colour of the button
        getStartedButton.setBackgroundResource(R.drawable.system_button)

        // An onClickListener to navigate to StudentParentReg activity
        getStartedButton.setOnClickListener {
            // Create an Intent to start the StudentParentReg activity
            val intent = Intent(this@GetStarted, StudentParentReg::class.java)
            startActivity(intent)
        }
    }
}
