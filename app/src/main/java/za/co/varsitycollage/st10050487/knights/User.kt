package za.co.varsitycollage.st10050487.knights

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText

class User : AppCompatActivity() {
    private var userId: Int = -1
    private var roleId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user)
        userId = intent.getIntExtra("USER_ID", -1)
        roleId = intent.getIntExtra("ROLE_ID", -1)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<ImageView>(R.id.fab_edit).setOnClickListener {
            val intent = Intent(this, UpdateUser::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
            // Finishing MainActivity so that the user cannot go back to it
            finish()
        }

        if (userId != -1) {
            displayUserDetails(userId)
        }
    }

    override fun onResume() {
        super.onResume()
        displayUserDetails(userId)
    }
    private fun displayUserDetails(userId: Int) {
        val dbHelper = DBHelper(this)
        val user = dbHelper.getUser(userId)

        if (user != null) {
            val nameInput = findViewById<TextInputEditText>(R.id.nameInput)
            val surnameInput = findViewById<TextInputEditText>(R.id.surnameInput)
            val emailInput = findViewById<TextInputEditText>(R.id.emailInput)
            val passwordInput = findViewById<TextInputEditText>(R.id.passwordTxt)
            val dateOfBirthInput = findViewById<TextInputEditText>(R.id.Matchdate)
            val profileImageView = findViewById<ImageView>(R.id.userProfileimageView)

            nameInput.setText(user.name)
            surnameInput.setText(user.surname)
            emailInput.setText(user.email)
            passwordInput.setText(user.password)
            dateOfBirthInput.setText(user.dateOfBirth)

            if (user.profilePicture != null) {
                val bitmap = BitmapFactory.decodeByteArray(
                    user.profilePicture,
                    0,
                    user.profilePicture.size
                )
                profileImageView.setImageBitmap(bitmap)
            }
        }
    }
}