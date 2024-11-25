package za.co.varsitycollage.st10050487.knights

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class CreateEvent : AppCompatActivity() {

    // Database Helper
    private lateinit var dbHelper: DBHelper

    // Buttons
    private lateinit var uploadImageButton: Button
    private lateinit var createEventButton: Button
    private lateinit var btnBack: androidx.appcompat.widget.Toolbar

    // Text Input Fields
    private lateinit var nameInput: TextInputEditText
    private lateinit var locationInput: TextInputEditText
    private lateinit var timeInput: TextInputEditText
    private lateinit var dateInput: TextInputEditText
    private lateinit var priceInput: TextInputEditText
    private lateinit var aboutInput: TextInputEditText

    // Image View
    private lateinit var eventImage: ImageView

    // Image Request Code
    private var userId: Int? = null
    private val PICK_IMAGE_REQUEST = 1
    private var selectedEventTime: String? = null
    private var selectedEventDate: String? = null
    private var eventImageUri: Uri? = null

    private var name: String? = null
    private var place: String? = null
    private var time: String? = null
    private var date: String? = null
    private var entryFee: Double = 0.0
    private var description: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_event)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Database Helper
        dbHelper = DBHelper(this)

        // Retrieve the USER_ID from the Intent
        userId = intent.getIntExtra("USER_ID", -1) // Default value is -1 if not found

        // Initialise UI Components
        // Buttons
        uploadImageButton = findViewById(R.id.btnUpload)
        createEventButton = findViewById(R.id.createBtn)

        // Image View
        eventImage = findViewById(R.id.eventImg)

        // Text Input Fields
        nameInput = findViewById(R.id.eventName)
        locationInput = findViewById(R.id.eventLocation)
        timeInput = findViewById(R.id.eventTime)
        dateInput = findViewById(R.id.eventDate)
        priceInput = findViewById(R.id.eventPrice)
        aboutInput = findViewById(R.id.eventAbout)


        // Correctly cast the Toolbar
        btnBack = findViewById(R.id.btnToolbar)
        setSupportActionBar(btnBack)
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //supportActionBar?.setDisplayShowHomeEnabled(true)

        btnBack.setNavigationOnClickListener {
            onBackPressed()
        }
        uploadImageButton.setOnClickListener {
            openImagePicker()
        }
        createEventButton.setOnClickListener {
            storeData()
            insertEventIntoDatabase()
        }
        timeInput.setOnClickListener {
            showTimePicker()
        }
        dateInput.setOnClickListener {
            showDatePicker()
        }

    }

    // Store the data from the input fields
    private fun storeData() {
        name = nameInput.text.toString()
        place = locationInput.text.toString()
        time = timeInput.text.toString()
        date = dateInput.text.toString()
        entryFee = priceInput.text.toString().toDouble()
        description = aboutInput.text.toString()

        Log.d("CreateEvent", "Event Name: $name")
        Log.d("CreateEvent", "Event Location: $place")
        Log.d("CreateEvent", "Event Time: $time")
        Log.d("CreateEvent", "Event Date: $date")
        Log.d("CreateEvent", "Event Price: $entryFee")
        Log.d("CreateEvent", "Event Description: $description")
        Log.d("CreateEvent", "Event Image URI: $eventImageUri")
    }
    private fun validateInputs(): Boolean {
        val name = nameInput.text.toString().trim()
        val location = locationInput.text.toString().trim()
        val time = timeInput.text.toString().trim()
        val date = dateInput.text.toString().trim()
        val price = priceInput.text.toString().trim()
        val about = aboutInput.text.toString().trim()

        if (name.isEmpty()) {
            nameInput.error = "Event name is required"
            return false
        }

        if (location.isEmpty()) {
            locationInput.error = "Event location is required"
            return false
        }

        if (time.isEmpty()) {
            timeInput.error = "Event time is required"
            return false
        }

        if (date.isEmpty()) {
            dateInput.error = "Event date is required"
            return false
        }

        if (price.isEmpty()) {
            priceInput.error = "Event price is required"
            return false
        } else if (!price.matches("\\d*\\.?\\d*".toRegex())) {
            priceInput.error = "Please enter a valid price"
            return false
        }

        if (about.isEmpty()) {
            aboutInput.error = "Event description is required"
            return false
        }

        return true
    }
    // Insert the event data into the database
    private fun insertEventIntoDatabase() {
        if (!validateInputs()) {
            return
        }
        val eventImageBlob = eventImageUri?.let { uri ->
            contentResolver.openInputStream(uri)?.use { inputStream ->
                inputStream.readBytes()
            }
        }

        val eventId = dbHelper.addEvent(
            name ?: "",
            date ?: "",
            time ?: "",
            place ?: "",
            entryFee,
            description ?: "",
            eventImageBlob,
            userId ?: -1
        )

        if (eventId == -1L) {
            Toast.makeText(this, "Failed to create event", Toast.LENGTH_LONG).show()
        } else {
            Log.d("CreateEvent", "Generated Event ID: $eventId") // Log the event ID
            Toast.makeText(this, "You have successfully created the event", Toast.LENGTH_LONG)
                .show()

            // Start EventDetailActivity and pass the event ID
            val intent = Intent(this, EventDetailActivity::class.java).apply {
                putExtra("EVENT_ID", eventId.toInt())
            }
            startActivity(intent)
            finish()
        }

    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*" // Set type to allow picking images
        }
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri: Uri? = data.data
            if (imageUri != null) {
                eventImage.setImageURI(imageUri)
                eventImageUri = imageUri
            } else {
                // Handle the case where the image URI is null
                Toast.makeText(this, "Unable to get image URI", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Handle the case where the request was not successful
            Toast.makeText(this, "Image selection canceled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            // Format the selected time
            selectedEventTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            timeInput.setText(selectedEventTime) // Display the selected time in the EditText
        }, hour, minute, true) // true for 24-hour format

        timePickerDialog.show()
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            // Format the selected date
            selectedEventDate = String.format("%02d-%02d-%04d", selectedDay, selectedMonth + 1, selectedYear)
            dateInput.setText(selectedEventDate) // Display the selected date in the EditText
        }, year, month, day)

        datePickerDialog.show()
    }
}