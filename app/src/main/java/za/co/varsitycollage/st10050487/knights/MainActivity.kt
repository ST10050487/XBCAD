package za.co.varsitycollage.st10050487.knights

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check if the saved instance state is null to avoid overlapping fragments on orientation change
        if (savedInstanceState == null) {
            // Load the StudentParentReg fragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, StudentParentReg())  // Assuming fragment_container is the container in activity_main.xml
                .commit()
        }
    }
}