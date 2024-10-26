package za.co.varsitycollage.st10050487.knights

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import za.co.varsitycollage.st10050487.knights.databinding.ActivityEditFixtureBinding
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditFixture : AppCompatActivity() {
    private lateinit var binding: ActivityEditFixtureBinding
    private lateinit var dbHelper: DBHelper
    private lateinit var sportsList: List<String>
    private lateinit var ageGroupList: List<String>
    private lateinit var leagueList: List<String>
    private var homeHolder: ByteArray? = null
    private var awayHolder: ByteArray? = null
    private val PICK_IMAGE_REQUEST = 1
    private val CAMERA_REQUEST = 2
    private var isHomeLogo: Boolean = true
    private var fixtureId: Int = 1
    private var userId: Int = 1
    private var leagueId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditFixtureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)

        // Fetch sports data from the database
        sportsList = dbHelper.getAllSports()
        populateSpinner( binding.spinnerSport, sportsList)
        // Fetch age group data from the database
        ageGroupList = dbHelper.getAllAgeGroups()
        // Set the data to the spinner using ArrayAdapter
        populateSpinner( binding.spinnerAgeGroup, ageGroupList)
        // Fetch league data from the database
        leagueList= dbHelper.getAllLeagues()
        populateSpinner(  binding.spinnerLeague, leagueList)

        binding.btnAwayUpload.setOnClickListener {
            isHomeLogo = false
            showImagePickerOptions()
        }
        binding.btnHomeUpload.setOnClickListener {
            isHomeLogo = true
            showImagePickerOptions()
        }
        binding.txtDateLayout.setEndIconOnClickListener {
            showDatePickerDialog()
        }

        binding.btnUpdate.setOnClickListener {
            if (validateInputs()) {
                updateFixtureData()
            }
        }
        binding.btnPlayers.setOnClickListener {
            val intent = Intent(this, GetPlayers::class.java)
            intent.putExtra("FIXTURE_ID", fixtureId) // Pass fixtureId
            startActivity(intent)
        }
        binding.btnDelete.setOnClickListener {
            if (dbHelper.checkIsAdmin(userId)) {
                binding.btnDelete.setOnClickListener {
                    val num =  dbHelper.checkFixtureId(2)
                    if (num > 0) {
                        showConfirmationDialog(2)
                    } else {
                        Toast.makeText(this, "Fixture does not exist", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        // take out
       //val id = dbHelper.addDummyFixtureWithUserId(userId)
       //Toast.makeText(this, id.toString(), Toast.LENGTH_SHORT).show()
        loadFixture(2)
    }

    private fun updateFixtureData() {
        val away = binding.txtAwayTeam.text.toString()
        val home = binding.txtHomeTeam.text.toString()
        val description = binding.txtDescrip.text.toString()
        val venue = binding.txtVenue.text.toString()
        val time = binding.txtTime.text.toString()
        val date = binding.txtDate.text.toString()
        val sport = binding.spinnerSport.selectedItem.toString()
        val ageGroup = binding.spinnerAgeGroup.selectedItem.toString()
        val league = binding.spinnerLeague.selectedItem.toString()
        val homeLogo = homeHolder
        val awayLogo = awayHolder
        val picture = null


        val fixture = FixtureModel(
            fixtureId = fixtureId, // Assuming fixtureId is available
            userId = userId, // Assuming userId is available
            sport = sport,
            homeTeam = home,
            awayTeam = away,
            ageGroup = ageGroup,
            league = league,
            matchLocation = venue,
            matchDate = date,
            matchTime = time,
            matchDescription = description,
            homeLogo = homeLogo,
            awayLogo = awayLogo,
            picture = picture,
            leagueId = leagueId
        )

        val result = dbHelper.updateFixture(fixture)

        if (result > 0) {
            Toast.makeText(this, "Fixture updated successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed to update fixture", Toast.LENGTH_SHORT).show()
        }
    }
    private fun populateSpinner(spinner: Spinner, data: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, data)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
       // val id = dbHelper.addDummyFixtureWithUserId(dummyId)
      //  Toast.makeText(this, id.toString(), Toast.LENGTH_SHORT).show()
       // loadFixture(3)
    }

    private fun showConfirmationDialog(fixtureId: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Fixture")
        builder.setMessage("Are you sure you want to delete this Fixture?")

        builder.setPositiveButton("Yes") { dialog: DialogInterface, which: Int ->
            // Handle the delete operation here
            dbHelper.deleteFixture(fixtureId)
            val num = dbHelper.countFixtures()
            dialog.dismiss()
            Toast.makeText(this, "Number of fixtures: $num", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No") { dialog: DialogInterface, which: Int ->
            // Dismiss the dialog
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun loadFixture(fixtureId: Int) {
        val fixture = dbHelper.getFixtureDetails(fixtureId)
        fixture?.let {
            leagueId = it.leagueId
            binding.txtHomeTeam.setText(it.homeTeam)
            binding.txtAwayTeam.setText(it.awayTeam)
            binding.txtVenue.setText(it.matchLocation)
            binding.txtDescrip.setText(it.matchDescription)
            binding.txtTime.setText(it.matchTime)
            binding.txtDate.setText(it.matchDate)
            // Assuming you have methods to set the spinner values
            setSpinner(binding.spinnerSport,sportsList, it.sport)
            setSpinner(binding.spinnerAgeGroup,ageGroupList, it.ageGroup)
            setSpinner(binding.spinnerLeague,leagueList, it.league)
            // Set images if available
            if (it.homeLogo != null) {
                homeHolder = it.homeLogo
                binding.imgHomeLogo.setImageBitmap(
                    BitmapFactory.decodeByteArray(
                        it.homeLogo,
                        0,
                        it.homeLogo.size
                    )
                )
            }
            if (it.awayLogo != null) {
                awayHolder = it.awayLogo
                binding.imgAwayLogo.setImageBitmap(
                    BitmapFactory.decodeByteArray(
                        it.awayLogo,
                        0,
                        it.awayLogo.size
                    )
                )
            }
        }
    }
    private fun setSpinner(spinner: Spinner, items: List<String>, value: String) {
        val index = items.indexOf(value)
        if (index >= 0) {
            spinner.setSelection(index)
        }
    }

    private fun updatePicture(bitmap: Bitmap) {
        if (isHomeLogo) {
            binding.imgHomeLogo.setImageBitmap(bitmap)
            homeHolder = bitmapToByteArray(bitmap)
        } else {
            binding.imgAwayLogo.setImageBitmap(bitmap)
            awayHolder = bitmapToByteArray(bitmap)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    val imageUri = data?.data
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                    updatePicture(bitmap)
                }
                CAMERA_REQUEST -> {
                    val bitmap = data?.extras?.get("data") as Bitmap
                    updatePicture(bitmap)
                }
            }
        }
    }
    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun showImagePickerOptions() {
        val options = arrayOf("Take a Photo", "Choose from Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose Profile Picture")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> checkCameraPermission()
                1 -> openGallery()
            }
        }
        builder.show()
    }
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
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        } else {
            Toast.makeText(this, "Camera permission is required to take a picture.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST)
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = sdf.format(calendar.time)
                binding.txtDate.setText(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun validateInputs(): Boolean {
        val away = binding.txtAwayTeam.text.toString()
        val home = binding.txtHomeTeam.text.toString()
        val description = binding.txtDescrip.text.toString()
        val venue = binding.txtVenue.text.toString()
        val time = binding.txtTime.text.toString()
        val date = binding.txtDate.text.toString()

        if (home.isEmpty()) {
            binding.txtHomeTeam.error = "Home team is required"
            return false
        }
        if (away.isEmpty()) {
            binding.txtAwayTeam.error = "Away team is required"
            return false
        }
        if (venue.isEmpty()) {
            binding.txtVenue.error = "Venue is required"
            return false
        }
        if (time.isEmpty()) {
            binding.txtTime.error = "Time is required"
            return false
        }
        if (date.isEmpty()) {
            binding.txtDate.error = "Date is required"
            return false
        }

        val (isValid, errorMessage) = isDateValid(date)
        if (!isValid) {
            binding.txtDate.error = errorMessage
            return false
        }
        if (description.isEmpty()) {
            binding.txtDescrip.error = "Description is required"
            return false
        }

        return true
    }
    private fun isDateValid(date: String): Pair<Boolean, String?> {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        sdf.isLenient = false
        return try {
            val selectedDate = sdf.parse(date)
            val currentDate = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time

            if (selectedDate != null) {
                if (selectedDate.before(currentDate)) {
                    Pair(false, "Date has already passed")
                } else {
                    Pair(true, null)
                }
            } else {
                Pair(false, "Invalid date, example: 2025-12-31")
            }
        } catch (e: Exception) {
            Pair(false, "Invalid date, example: 2025-12-31")
        }
    }

}