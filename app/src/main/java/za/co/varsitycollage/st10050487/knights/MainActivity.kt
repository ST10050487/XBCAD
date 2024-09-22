package za.co.varsitycollage.st10050487.knights

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Add the StudentParentReg fragment
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container, StudentParentReg())
            }
        }
    }
}