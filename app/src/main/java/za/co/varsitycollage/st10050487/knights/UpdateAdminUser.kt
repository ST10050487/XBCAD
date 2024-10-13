package za.co.varsitycollage.st10050487.knights

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import za.co.varsitycollage.st10050487.knights.databinding.ActivityCreateTimesheetBinding
import za.co.varsitycollage.st10050487.knights.databinding.ActivityUpdateAdminUserBinding

class UpdateAdminUser : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateAdminUserBinding
    private lateinit var btnSave: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_timesheet)
        binding = ActivityUpdateAdminUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnSave = binding.saveBtn
        btnSave.setOnClickListener {
            // Save the user details
            Toast.makeText(this, "User details saved", Toast.LENGTH_SHORT).show()
        }

    }
}