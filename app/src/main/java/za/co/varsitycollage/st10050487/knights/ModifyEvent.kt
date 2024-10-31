package za.co.varsitycollage.st10050487.knights

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*

class ModifyEvent : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper
    private lateinit var eventImageView: ImageView
    private val REQUEST_CODE_PICK_IMAGE = 1001
    private val REQUEST_CODE_PERMISSION = 1002
    private val REQUEST_CODE_TAKE_PHOTO = 1003

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_modify_event)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DBHelper(this)
        eventImageView = findViewById(R.id.eventImage)

        // Get the passed event ID
        val eventId = intent.getIntExtra("EVENT_ID", -1)
        if (eventId != -1) {
            displayEventDetails(eventId)
        }

        // Set up time picker
        findViewById<TextInputEditText>(R.id.time).setOnClickListener {
            showTimePickerDialog()
        }

        // Set up date picker
        findViewById<TextInputEditText>(R.id.date).setOnClickListener {
            showDatePickerDialog()
        }

        // Set up price input filter
        findViewById<TextInputEditText>(R.id.price).filters = arrayOf(DecimalDigitsInputFilter(2))

        // Set up upload button click listener
        findViewById<TextView>(R.id.upLoadBtn).setOnClickListener {
            showImageSourceDialog()
        }

        // Set up update button click listener
        val updateButton = findViewById<Button>(R.id.updateBtn)
        updateButton.setOnClickListener {
            Log.d("ModifyEvent", "Update button clicked")
            updateEventDetails()
        }
    }
//   private fun loadEvent(eventId: Int) {
//    val event = dbHelper.getEventDetails(eventId)
//
//    event?.let {
//        findViewById<TextInputEditText>(R.id.eventName).setText(it.eventName)
//        findViewById<TextInputEditText>(R.id.date).setText(it.eventDate)
//        findViewById<TextInputEditText>(R.id.time).setText(it.eventTime)
//        findViewById<TextInputEditText>(R.id.eventLocation).setText(it.eventLocation)
//        findViewById<TextInputEditText>(R.id.price).setText(it.eventPrice.toString())
//        findViewById<TextInputEditText>(R.id.description).setText(it.eventDescription)
//
//        val imgEventPicture = findViewById<ImageView>(R.id.eventImage)
//        if (it.eventPicture != null && it.eventPicture.isNotEmpty()) {
//            // Load the picture into an ImageView or handle it as needed
//            val bitmap = BitmapFactory.decodeByteArray(it.eventPicture, 0, it.eventPicture.size)
//            imgEventPicture.setImageBitmap(bitmap)
//        } else {
//            // Handle the case where there is no picture
//            imgEventPicture.setImageResource(R.drawable.ic_sample) // Assuming you have a default image
//        }
//    } ?: run {
//        Toast.makeText(this, "Failed to load event", Toast.LENGTH_SHORT).show()
//    }
//}
    private fun showImageSourceDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Image Source")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> {
                    // Take photo
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_PERMISSION)
                    } else {
                        openCamera()
                    }
                }
                1 -> {
                    // Choose from gallery
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSION)
                    } else {
                        openGallery()
                    }
                }
            }
        }
        builder.show()
    }

    @SuppressLint("SetTextI18n")
    private fun displayEventDetails(eventId: Int) {
        val event = dbHelper.getEventDetails(eventId)
        event?.let {
            eventImageView.setImageBitmap(BitmapFactory.decodeByteArray(it.pictures, 0, it.pictures.size))
            findViewById<TextInputEditText>(R.id.eventName).setText(it.eventName)
            findViewById<TextInputEditText>(R.id.eventLocation).setText(it.eventLocation)
            findViewById<TextInputEditText>(R.id.time).setText(it.eventTime)
            findViewById<TextInputEditText>(R.id.date).setText(it.eventDate)
            findViewById<TextInputEditText>(R.id.price).setText(it.eventPrice.toString())
            findViewById<TextInputEditText>(R.id.description).setText(it.eventDescription)
        }
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(this, { _, hourOfDay, minute ->
            val time = String.format("%02d:%02d", hourOfDay, minute)
            findViewById<TextInputEditText>(R.id.time).setText(time)
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
        timePickerDialog.show()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            val date = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
            findViewById<TextInputEditText>(R.id.date).setText(date)
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery()
        } else if (requestCode == REQUEST_CODE_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            val bitmap: Bitmap? = when (requestCode) {
                REQUEST_CODE_PICK_IMAGE -> {
                    val imageUri: Uri? = data.data
                    imageUri?.let {
                        val inputStream: InputStream? = contentResolver.openInputStream(it)
                        BitmapFactory.decodeStream(inputStream)
                    }
                }
                REQUEST_CODE_TAKE_PHOTO -> {
                    data.extras?.get("data") as? Bitmap
                }
                else -> null
            }

            bitmap?.let {
                eventImageView.setImageBitmap(it)

                // Convert bitmap to byte array
                val byteArrayOutputStream = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()

                // Save the image byte array to the database
                val eventId = intent.getIntExtra("EVENT_ID", -1)
                if (eventId != -1) {
                    val event = dbHelper.getEventDetails(eventId)
                    event?.let {
                        val updatedEvent = it.copy(pictures = byteArray)
                        dbHelper.updateEventDetails(updatedEvent)
                    }
                }
            }
        }
    }

    // A method to update the event details
   private fun updateEventDetails() {
    val eventId = intent.getIntExtra("EVENT_ID", -1)
    if (eventId != -1) {
        val eventNameInput = findViewById<TextInputEditText>(R.id.eventName)
        val eventLocationInput = findViewById<TextInputEditText>(R.id.eventLocation)
        val eventTimeInput = findViewById<TextInputEditText>(R.id.time)
        val eventDateInput = findViewById<TextInputEditText>(R.id.date)
        val eventPriceInput = findViewById<TextInputEditText>(R.id.price)
        val eventDescriptionInput = findViewById<TextInputEditText>(R.id.description)

        val eventName = eventNameInput.text.toString()
        val eventLocation = eventLocationInput.text.toString()
        val eventTime = eventTimeInput.text.toString()
        val eventDate = eventDateInput.text.toString()
        val eventPriceText = eventPriceInput.text.toString()
        val eventDescription = eventDescriptionInput.text.toString()

        var isValid = true

        if (eventName.isEmpty()) {
            eventNameInput.error = "Event name is required"
            isValid = false
        }

        if (eventLocation.isEmpty()) {
            eventLocationInput.error = "Event location is required"
            isValid = false
        }

        if (eventTime.isEmpty()) {
            eventTimeInput.error = "Event time is required"
            isValid = false
        }

        if (eventDate.isEmpty()) {
            eventDateInput.error = "Event date is required"
            isValid = false
        }

        if (eventPriceText.isEmpty()) {
            eventPriceInput.error = "Event price is required"
            isValid = false
        }

        if (eventDescription.isEmpty()) {
            eventDescriptionInput.error = "Event description is required"
            isValid = false
        }

        Log.d("ModifyEvent", "Validation result: $isValid")

        if (isValid) {
            val eventPrice = eventPriceText.toDouble()
            val eventPictures = dbHelper.getEventDetails(eventId)?.pictures ?: byteArrayOf() // Fetch existing pictures

            val updatedEvent = EventModel(
                eventId = eventId,
                eventName = eventName,
                eventDate = eventDate,
                eventTime = eventTime,
                eventLocation = eventLocation,
                eventPrice = eventPrice,
                pictures = eventPictures,
                eventDescription = eventDescription,
                selected = false
            )

            val rowsUpdated = dbHelper.updateEventDetails(updatedEvent)
            Log.d("ModifyEvent", "Rows updated: $rowsUpdated")
            finish()
        }
    } else {
        Log.e("ModifyEvent", "Invalid event ID")
    }
}

    // Input filter to allow only digits with two decimal values
    class DecimalDigitsInputFilter(private val decimalDigits: Int) : InputFilter {
        private val pattern = Regex("^[0-9]*\\.?[0-9]{0,$decimalDigits}\$")

        override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence? {
            val newString = dest?.replaceRange(dstart, dend, source?.subSequence(start, end) ?: "")
            return if (newString?.matches(pattern) == true) null else ""
        }
    }
}