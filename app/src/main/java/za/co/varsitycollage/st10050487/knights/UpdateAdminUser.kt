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
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import za.co.varsitycollage.st10050487.knights.databinding.ActivityUpdateAdminUserBinding
import java.io.ByteArrayOutputStream
import java.util.Calendar

class UpdateAdminUser : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateAdminUserBinding
    private lateinit var dbHelper: DBHelper
    private var userId: Int = 0
    private var dummyId: Int = 0
    private lateinit var profilePicture: ImageView
    private lateinit var dateOfBirthEditText: TextInputEditText

    private val PICK_IMAGE_REQUEST = 1
    private val CAMERA_REQUEST = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateAdminUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initializing your database helper
        dbHelper = DBHelper(this)

        // Getting the userId from the Intent
        userId= intent.getIntExtra("USER_ID", 0)

        profilePicture = findViewById(R.id.profilePicture)
        dateOfBirthEditText = findViewById(R.id.userDateOfBirth)

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
        binding.saveBtn.setOnClickListener {
            // Save the user details
            if (validateInputs()) {
                updateAdminData()
            }
        }

        // Loading and displaying the player data based on userId
        loadAdminUserDetails()
    }

    private fun loadAdminUserDetails() {
        //val adminUser = dbHelper.getAdminUserDetails(userId)
        val adminUser = dbHelper.getAdminUserDetails()
        adminUser?.let {
            binding.userName.setText(it.name)
            binding.userSurname.setText(it.surname)
            binding.userEmail.setText(it.email)
            binding.userPassword.setText(it.password)
            binding.userDateOfBirth.setText(it.dateOfBirth)
            binding.cbMngusers.isChecked = it.manageUsers
            binding.cbMngFixtures.isChecked = it.manageFixtures
            binding.cbInteractions.isChecked = it.interactionReports
            binding.cbCreateAdmin.isChecked = it.createAdmin
        }
        //REMOVE AND CHANAGE TO REAL ID
        dummyId = adminUser?.userId ?: 0

        // Load profile picture if available
        val profilePictureBlob = adminUser?.profilePicture
        if (profilePictureBlob != null) {
            val profilePictureBitmap = BitmapFactory.decodeByteArray(profilePictureBlob, 0, profilePictureBlob.size)
            profilePicture.setImageBitmap(profilePictureBitmap)
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

    private fun updateAdminData() {
        val name = binding.userName.text.toString()
        val surname = binding.userSurname.text.toString()
        val email = binding.userEmail.text.toString()
        val password = binding.userPassword.text.toString()
        val dateOfBirth = binding.userDateOfBirth.text.toString()
        val manageUsers = binding.cbMngusers.isChecked
        val manageFixtures = binding.cbMngFixtures.isChecked
        val interactionReports = binding.cbInteractions.isChecked
        val createAdmin = binding.cbCreateAdmin.isChecked

        val adminUser = AdminModel(
            userId = dummyId,
            name = name,
            surname = surname,
            email = email,
            password = password,
            dateOfBirth = dateOfBirth,
            manageUsers = manageUsers,
            manageFixtures = manageFixtures,
            interactionReports = interactionReports,
            createAdmin = createAdmin,
            profilePicture = null // Handle profile picture separately if needed
        )

        val result = dbHelper.updateAdminUser(adminUser)

        if (result > 0) {
            Toast.makeText(this, "Admin profile updated successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Login::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
            finish()
        }
        else {
            Toast.makeText(this, "Failed to update admin profile", Toast.LENGTH_SHORT).show()
        }
    }

    // A method to validate user inputs
    private fun validateInputs(): Boolean {
        val name = binding.userName.text.toString()
        val surname = binding.userSurname.text.toString()
        val email = binding.userEmail.text.toString()
        val password = binding.userPassword.text.toString()
        val dateOfBirth = binding.userDateOfBirth.text.toString()

        if (name.isEmpty()) {
            binding.userName.error = "Name is required"
            return false
        }
        if (surname.isEmpty()) {
            binding.userSurname.error = "Surname is required"
            return false
        }
        if (email.isEmpty()) {
            binding.userEmail.error = "Email is required"
            return false
        }
        if (password.isEmpty()) {
            binding.userPassword.error = "Password is required"
            return false
        }
        if (dateOfBirth.isEmpty()) {
            binding.userDateOfBirth.error = "Date of birth is required"
            return false
        }
        return true
    }
}