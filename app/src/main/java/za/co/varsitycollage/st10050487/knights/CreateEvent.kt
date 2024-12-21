package za.co.varsitycollage.st10050487.knights

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*

class CreateEvent : AppCompatActivity() {

    // Database Helper
    private lateinit var dbHelper: DBHelper

    // UI Components
    private lateinit var uploadImageButton: Button
    private lateinit var createEventButton: Button
    private lateinit var btnBack: androidx.appcompat.widget.Toolbar
    private lateinit var nameInput: TextInputEditText
    private lateinit var locationInput: TextInputEditText
    private lateinit var timeInput: TextInputEditText
    private lateinit var dateInput: TextInputEditText
    private lateinit var priceInput: TextInputEditText
    private lateinit var aboutInput: TextInputEditText
    private lateinit var eventImage: ImageView

    private val PICK_IMAGE_REQUEST = 1
    private var eventImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DBHelper(this)

        // Initialize UI components
        uploadImageButton = findViewById(R.id.btnUpload)
        createEventButton = findViewById(R.id.createBtn)
        btnBack = findViewById(R.id.btnToolbar)
        nameInput = findViewById(R.id.eventName)
        locationInput = findViewById(R.id.eventLocation)
        timeInput = findViewById(R.id.eventTime)
        dateInput = findViewById(R.id.eventDate)
        priceInput = findViewById(R.id.eventPrice)
        aboutInput = findViewById(R.id.eventAbout)
        eventImage = findViewById(R.id.eventImg)

        setSupportActionBar(btnBack)
        btnBack.setNavigationOnClickListener { onBackPressed() }

        uploadImageButton.setOnClickListener { openImagePicker() }
        createEventButton.setOnClickListener { validateAndCreateEvent() }
        timeInput.setOnClickListener { showTimePicker() }
        dateInput.setOnClickListener { showDatePicker() }
    }

    private fun validateAndCreateEvent() {
        if (!validateInputs()) return

        val eventId = insertEventIntoDatabase()
        if (eventId != -1L) {
            sendNotificationToUser()
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        if (nameInput.text.isNullOrBlank()) {
            nameInput.error = "Event name is required"
            isValid = false
        }
        if (locationInput.text.isNullOrBlank()) {
            locationInput.error = "Event location is required"
            isValid = false
        }
        if (timeInput.text.isNullOrBlank()) {
            timeInput.error = "Event time is required"
            isValid = false
        }
        if (dateInput.text.isNullOrBlank()) {
            dateInput.error = "Event date is required"
            isValid = false
        }
        val priceText = priceInput.text.toString().trim()
        if (priceText.isEmpty()) {
            priceInput.error = "Event price is required"
            isValid = false
        } else if (!priceText.matches("\\d*\\.?\\d*".toRegex())) {
            priceInput.error = "Please enter a valid price"
            isValid = false
        }
        if (aboutInput.text.isNullOrBlank()) {
            aboutInput.error = "Event description is required"
            isValid = false
        }

        return isValid
    }

    private fun insertEventIntoDatabase(): Long {
        val eventImageBlob = eventImageUri?.let { uri ->
            contentResolver.openInputStream(uri)?.use { it.readBytes() }
        }

        val name = nameInput.text.toString().trim()
        val place = locationInput.text.toString().trim()
        val time = timeInput.text.toString().trim()
        val date = dateInput.text.toString().trim()
        val entryFee = priceInput.text.toString().trim().toDouble()
        val description = aboutInput.text.toString().trim()

        val eventId = dbHelper.addEvent(
            name, date, time, place, entryFee, description, eventImageBlob, -1
        )

        if (eventId == -1L) {
            Toast.makeText(this, "Failed to create event", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Event created successfully!", Toast.LENGTH_LONG).show()
            val intent = Intent(this, EventDetailActivity::class.java)
            intent.putExtra("EVENT_ID", eventId.toInt())
            startActivity(intent)
            finish()
        }

        return eventId
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply { type = "image/*" }
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                eventImageUri = it
                eventImage.setImageURI(it)
            }
        }
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        TimePickerDialog(this, { _, hour, minute ->
            timeInput.setText(String.format("%02d:%02d", hour, minute))
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(this, { _, year, month, dayOfMonth ->
            dateInput.setText(String.format("%02d-%02d-%04d", dayOfMonth, month + 1, year))
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun sendNotificationToUser() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("CreateEvent", "Fetching FCM token failed", task.exception)
                return@OnCompleteListener
            }

            val userToken = task.result
            if (userToken != null) {
                val title = "New Event Created"
                val body = "Event: ${nameInput.text}\nLocation: ${locationInput.text}\nDate: ${dateInput.text}\nTime: ${timeInput.text}"
                EventMessagingService().sendMessage(userToken, title, body)
                Log.d("CreateEvent", "Notification sent to user with token: $userToken")
            }
        })
    }
}
