package za.co.varsitycollage.st10050487.knights

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import java.io.ByteArrayOutputStream
import java.util.Calendar

class UpdatePlayerProfile : AppCompatActivity() {
    private lateinit var databaseHelper: DBHelper
    private var userId: Int = 0
    private var playerId: Int = 0
    private lateinit var profilePicture: ImageView
    private lateinit var dateOfBirthEditText: TextInputEditText

    private val PICK_IMAGE_REQUEST = 1
    private val CAMERA_REQUEST = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_player_profile)

        // Getting the userId from the Intent
        userId = intent.getIntExtra("USER_ID", 0)

        // Initializing your database helper
        databaseHelper = DBHelper(this)

        profilePicture = findViewById(R.id.profilePicture)
        dateOfBirthEditText = findViewById(R.id.playerDateOfBirth)

        // Loading and displaying the player data based on userId
        loadPlayerData(userId)

        // Set up camera icon click listener to update profile picture
        val cameraIcon = findViewById<ImageView>(R.id.cameraIcon)
        cameraIcon.setOnClickListener {
            showImagePickerOptions()
        }

        // Set up date of birth field click listener
        dateOfBirthEditText.setOnClickListener {
            showDatePickerDialog()
        }

        // Setting the onClickListener for the update button
        val updateBtn = findViewById<Button>(R.id.updateButton)
        updateBtn.setOnClickListener {
            if (validateInputs()) {
                updatePlayerData()
            }
        }
        val cancelBtn = findViewById<Button>(R.id.cancelButton)
        cancelBtn.setOnClickListener {
            val intent = Intent(this, ViewPlayer::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
            finish()
        }
    }

    // Method to show options for picking image
    private fun showImagePickerOptions() {
        val options = arrayOf("Take a Photo", "Choose from Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose Profile Picture")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> checkCameraPermission() // Check camera permission
                1 -> openGallery()
            }
        }
        builder.show()
    }

    // Check for camera permissions and open camera
    private fun checkCameraPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST)
            } else {
                openCamera()
            }
        } else {
            openCamera()
        }
    }

    // Handle permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    Toast.makeText(this, "Camera permission is required to take a picture.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Open gallery to select an image
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    // Open the camera to take a photo
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    val imageUri = data?.data
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                    updateProfilePicture(bitmap)
                }
                CAMERA_REQUEST -> {
                    val bitmap = data?.extras?.get("data") as Bitmap
                    updateProfilePicture(bitmap)
                }
            }
        }
    }

    // Update the profile picture in the ImageView and save it to the database
    private fun updateProfilePicture(bitmap: Bitmap) {
        profilePicture.setImageBitmap(bitmap)

        // Convert bitmap to byte array and save it to the database
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()

        // Save byteArray as profile picture in database
        databaseHelper.updatePlayerProfilePicture(playerId, byteArray)
    }

    // A method to load player data
    private fun loadPlayerData(userId: Int) {
        val player = databaseHelper.getPlayerProfile(userId)
        if (player != null) {
            playerId = player.playerId

            // Set the data to respective views
            findViewById<TextInputEditText>(R.id.playerName).setText(player.name)
            findViewById<TextInputEditText>(R.id.playerSurname).setText(player.surname)
            findViewById<TextInputEditText>(R.id.playerNickname).setText(player.nickname)
            dateOfBirthEditText.setText(player.dateOfBirth) // Load date of birth here
            findViewById<TextInputEditText>(R.id.playerAge).setText(player.age.toString())
            findViewById<TextInputEditText>(R.id.playerGrade).setText(player.grade)
            findViewById<TextInputEditText>(R.id.playerHeight).setText(player.height)
            findViewById<TextInputEditText>(R.id.playerPosition).setText(player.position)
            findViewById<TextInputEditText>(R.id.playerAgeGroup).setText(player.ageGroup)

            // Load profile picture if available
            val profilePictureBlob = player.profilePicture
            if (profilePictureBlob != null) {
                val profilePictureBitmap = BitmapFactory.decodeByteArray(profilePictureBlob, 0, profilePictureBlob.size)
                profilePicture.setImageBitmap(profilePictureBitmap)
            }
        } else {
            // Displaying a toast message if the player data is not found
            Toast.makeText(this, "Player data not found", Toast.LENGTH_SHORT).show()
        }
    }

    // A method to show DatePickerDialog
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format("%02d-%02d-%04d", selectedDay, selectedMonth + 1, selectedYear)
                dateOfBirthEditText.setText(formattedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    // A method to update player data
    private fun updatePlayerData() {
        val name = findViewById<TextInputEditText>(R.id.playerName).text.toString()
        val surname = findViewById<TextInputEditText>(R.id.playerSurname).text.toString()
        val nickname = findViewById<TextInputEditText>(R.id.playerNickname).text.toString()
        val dateOfBirth = dateOfBirthEditText.text.toString() // Get the date directly from the EditText
        val age = findViewById<TextInputEditText>(R.id.playerAge).text.toString().toIntOrNull() ?: 0
        val grade = findViewById<TextInputEditText>(R.id.playerGrade).text.toString()
        val height = findViewById<TextInputEditText>(R.id.playerHeight).text.toString()
        val position = findViewById<TextInputEditText>(R.id.playerPosition).text.toString()
        val ageGroup = findViewById<TextInputEditText>(R.id.playerAgeGroup).text.toString()

        // Updating the player data in the database using playerId
        val result = databaseHelper.updatePlayerProfile(playerId, name, surname, nickname,
            age, dateOfBirth, grade, height, position, ageGroup)

        if (result > 0) {
            Toast.makeText(this, "Player profile updated successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Login::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Failed to update player profile", Toast.LENGTH_SHORT).show()
        }
    }
    // A method to validate user inputs
    private fun validateInputs(): Boolean {
        val name = findViewById<TextInputEditText>(R.id.playerName).text.toString()
        val surname = findViewById<TextInputEditText>(R.id.playerSurname).text.toString()
        val nickname = findViewById<TextInputEditText>(R.id.playerNickname).text.toString()
        val dateOfBirth = dateOfBirthEditText.text.toString()
        val age = findViewById<TextInputEditText>(R.id.playerAge).text.toString()
        val grade = findViewById<TextInputEditText>(R.id.playerGrade).text.toString()
        val height = findViewById<TextInputEditText>(R.id.playerHeight).text.toString()
        val position = findViewById<TextInputEditText>(R.id.playerPosition).text.toString()
        val ageGroup = findViewById<TextInputEditText>(R.id.playerAgeGroup).text.toString()

        if (name.isEmpty()) {
            findViewById<TextInputEditText>(R.id.playerName).error = "Name is required"
            return false
        }
        if (surname.isEmpty()) {
            findViewById<TextInputEditText>(R.id.playerSurname).error = "Surname is required"
            return false
        }
        if (nickname.isEmpty()) {
            findViewById<TextInputEditText>(R.id.playerNickname).error = "Nickname is required"
            return false
        }
        if (dateOfBirth.isEmpty()) {
            dateOfBirthEditText.error = "Date of birth is required"
            return false
        }
        if (age.isEmpty()) {
            findViewById<TextInputEditText>(R.id.playerAge).error = "Age is required"
            return false
        }
        if (grade.isEmpty()) {
            findViewById<TextInputEditText>(R.id.playerGrade).error = "Grade is required"
            return false
        }
        if (height.isEmpty()) {
            findViewById<TextInputEditText>(R.id.playerHeight).error = "Height is required"
            return false
        }
        if (position.isEmpty()) {
            findViewById<TextInputEditText>(R.id.playerPosition).error = "Position is required"
            return false
        }
        if (ageGroup.isEmpty()) {
            findViewById<TextInputEditText>(R.id.playerAgeGroup).error = "Age group is required"
            return false
        }
        return true
    }
}


