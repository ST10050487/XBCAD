package za.co.varsitycollage.st10050487.knights

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import za.co.varsitycollage.st10050487.knights.databinding.ActivityCreateTimesheetBinding
import java.io.ByteArrayOutputStream
import java.util.*

class CreateTimesheet : AppCompatActivity() {
    // Binding object to access views in the layout
    private lateinit var binding: ActivityCreateTimesheetBinding
    private lateinit var btnUpload: Button
    private lateinit var recycleView: RecyclerView
    private lateinit var adapter: MultipleImageAdapter

    // Lists to store image URIs and filenames
    private var imageUriList = mutableListOf<ByteArray?>()
    private var fileNameList = mutableListOf<String?>()
    private var imageByteArrayList = mutableListOf<ByteArray?>()

    // List of match statuses
    private val matchStatusMap = mapOf(
        "First Half" to 1,
        "Half Time" to 2,
        "Second Half" to 3,
        "Match Over" to 4,
        "Cancelled" to 5
    )

    // List of match statuses (Add this line)
    private val matchStatuses = listOf(
        "First Half",
        "Half Time",
        "Second Half",
        "Match Over",
        "Cancelled"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Set the content view to the layout
        setContentView(R.layout.activity_create_timesheet)
        binding = ActivityCreateTimesheetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the fixture ID from the static variable
        val fixtureId = CreateSportFixture.fixtureID // Access the static variable

        // Initialize the RecyclerView and its adapter
        adapter = MultipleImageAdapter(imageByteArrayList, fileNameList)
        binding.rvHighlights.layoutManager = LinearLayoutManager(this)
        binding.rvHighlights.adapter = adapter

        // Set up the image upload button and time pickers
        ImageUpload()
        setupTimePickers()
        setupBackButton()

        // Initialize the Spinner
        setupSpinner()

        // Disable the save button if the fixture ID is invalid
        if (fixtureId == -1L) {
            binding.btnSave.isEnabled = false // Disable the button
            Toast.makeText(this, "Please create a sports fixture first", Toast.LENGTH_SHORT).show()
        } else {
            binding.btnSave.isEnabled = true // Enable the button if fixture ID is valid
        }

        binding.btnSave.setOnClickListener {
            if (fixtureId == -1L) {
                // If the fixture ID is invalid, show a message
                Toast.makeText(
                    this,
                    "You need to create a sports fixture first",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                saveTimesheet(fixtureId) // Pass the fixture ID to saveTimesheet
            }
        }
    }


    // Function to set up the Spinner
    private fun setupSpinner() {
        val spinner: Spinner = binding.spinnerMatchStatus // Use binding to get the Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, matchStatuses)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    // Function to handle image upload
    private fun ImageUpload() {
        btnUpload = binding.uploadBtn

        btnUpload.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                it.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
            imageLauncher.launch(Intent.createChooser(intent, "Select Picture"))
        }
    }

    // Function to convert Uri to ByteArray
    private fun uriToByteArray(uri: Uri): ByteArray? {
        return try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                ByteArrayOutputStream().use { byteArrayOutputStream ->
                    val buffer = ByteArray(1024)
                    var bytesRead: Int
                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        byteArrayOutputStream.write(buffer, 0, bytesRead)
                    }
                    byteArrayOutputStream.toByteArray()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Activity result launcher for image selection
    private val imageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.let { data ->
                    val uris = mutableListOf<Uri?>()
                    val filenames = mutableListOf<String?>()

                    data.clipData?.let { clipData ->
                        for (i in 0 until clipData.itemCount) {
                            val uri = clipData.getItemAt(i).uri
                            uris.add(uri)
                            filenames.add(getFilenameFromUri(this, uri))
                            imageByteArrayList.add(uriToByteArray(uri)) // Convert Uri to ByteArray
                        }
                    } ?: data.data?.let { uri ->
                        uris.add(uri)
                        filenames.add(getFilenameFromUri(this, uri))
                        imageByteArrayList.add(uriToByteArray(uri)) // Convert Uri to ByteArray
                    }

                    if (imageByteArrayList.isNotEmpty()) {
                        adapter.addItems(
                            imageByteArrayList,
                            filenames
                        ) // Update the adapter with new items
                    } else {
                        Toast.makeText(this, "No images selected", Toast.LENGTH_SHORT).show()
                    }
                } ?: run {
                    Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    // Function to get the filename from a URI
    private fun getFilenameFromUri(context: Context, uri: Uri): String? {
        val fileName: String?
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        fileName = cursor?.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
        cursor?.close()
        return fileName
    }

    // Function to set up time pickers for the time fields
    private fun setupTimePickers() {
        binding.txtMeetTime.setOnClickListener { showTimePickerDialog(binding.txtMeetTime) }
        binding.txtDepTime.setOnClickListener { showTimePickerDialog(binding.txtDepTime) }
        binding.txtArrTime.setOnClickListener { showTimePickerDialog(binding.txtArrTime) }
    }

    // Function to show a time picker dialog
    private fun showTimePickerDialog(textView: TextView) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            textView.text = String.format("%02d:%02d", selectedHour, selectedMinute)
        }, hour, minute, true)
        timePickerDialog.show()
    }

    // Function to set up the back button functionality
    private fun setupBackButton() {
        val backButton = binding.backBtn // Use the binding to get the back button layout
        backButton.setOnClickListener {
            finish() // This will close the current activity and return to the previous one
        }
    }

    // Function to save the timesheet
    private fun saveTimesheet(fixtureId: Long) {
        // Check if the fixture ID is valid
        if (fixtureId == -1L) {
            Toast.makeText(this, "You need to create a sports fixture first", Toast.LENGTH_SHORT)
                .show()

            // Navigate back to the CreateSportFixture activity
            val intent = Intent(this, CreateSportFixture::class.java)
            startActivity(intent)
            finish() // Finish the current activity to prevent going back to it
            return
        }

        val meetingTime = binding.txtMeetTime.text.toString()
        val busDepartureTime = binding.txtDepTime.text.toString()
        val busReturnTime = binding.txtArrTime.text.toString()
        val message = binding.txtHomeTeam.text.toString()

        // Get selected match status and convert to its numeric value
        val matchStatusText = binding.spinnerMatchStatus.selectedItem.toString()
        val matchStatusValue = matchStatusMap[matchStatusText] ?: 0 // Default to 0 if not found

        val dbHelper = DBHelper(this)
        dbHelper.addTimes(
            meetingTime,
            busDepartureTime,
            busReturnTime,
            message,
            matchStatusValue,
            fixtureId // Pass the fixture ID to the database method
        )

        Toast.makeText(this, "Timesheet saved successfully", Toast.LENGTH_SHORT).show()
    }
}