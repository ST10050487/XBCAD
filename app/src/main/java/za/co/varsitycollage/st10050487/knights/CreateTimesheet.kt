package za.co.varsitycollage.st10050487.knights

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
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

        binding.btnSave.setOnClickListener {
            saveTimesheet()
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
        val backButton = findViewById<LinearLayout>(R.id.back_btn)
        backButton.setOnClickListener {
//            val intent = Intent(this, Admin_Home::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Function to save the timesheet
    private fun saveTimesheet() {
        val meetingTime = binding.txtMeetTime.text.toString()
        val busDepartureTime = binding.txtDepTime.text.toString()
        val busReturnTime = binding.txtArrTime.text.toString()
        val message = binding.txtHomeTeam.text.toString()
        val matchStatus =
            binding.spinnerMatchStatus.selectedItem.toString() // Get selected match status

        val dbHelper = DBHelper(this)
        dbHelper.addTimes(
            meetingTime,
            busDepartureTime,
            busReturnTime,
            message,
            matchStatus
        ) // Pass match status

        Toast.makeText(this, "Timesheet saved successfully", Toast.LENGTH_SHORT).show()
    }
}