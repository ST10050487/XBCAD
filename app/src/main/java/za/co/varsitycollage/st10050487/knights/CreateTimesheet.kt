package za.co.varsitycollage.st10050487.knights

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import za.co.varsitycollage.st10050487.knights.databinding.ActivityCreateTimesheetBinding
import java.io.ByteArrayOutputStream
import java.util.*

class CreateTimesheet : AppCompatActivity() {
    private lateinit var binding: ActivityCreateTimesheetBinding
    private lateinit var adapter: MultipleImageAdapter

    private var imageByteArrayList = mutableListOf<ByteArray?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCreateTimesheetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the RecyclerView and its adapter
        adapter = MultipleImageAdapter(imageByteArrayList)
        binding.rvHighlights.layoutManager = LinearLayoutManager(this)
        binding.rvHighlights.adapter = adapter

        // Retrieve the fixture ID from SharedPreferences
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val fixtureId = sharedPreferences.getLong("FIXTURE_ID", -1L)

        // Check if the fixture ID is valid
        if (fixtureId == -1L) {
            Toast.makeText(this, "You need to create a sports fixture first", Toast.LENGTH_SHORT)
                .show()
            finish()
            return
        }

        // Setup image upload
        ImageUpload()
        setupBackButton()
        setupSpinner()
        setupSaveButton(fixtureId) // Pass the fixture ID to the save button setup

        // Set up time pickers
        setupTimePickers()
    }

    private fun ImageUpload() {
        binding.uploadBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
            imageLauncher.launch(Intent.createChooser(intent, "Select Picture"))
        }
    }

    private val imageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.let { data ->
                    val byteArrayList = mutableListOf<ByteArray?>()
                    val fileNameList = mutableListOf<String>() // New list for file names

                    data.clipData?.let { clipData ->
                        for (i in 0 until clipData.itemCount) {
                            val uri = clipData.getItemAt(i).uri
                            byteArrayList.add(uriToByteArray(uri))
                            fileNameList.add(getFileName(uri)) // Get the file name and add to the list
                        }
                    } ?: data.data?.let { uri ->
                        byteArrayList.add(uriToByteArray(uri))
                        fileNameList.add(getFileName(uri)) // Get the file name and add to the list
                    }

                    if (byteArrayList.isNotEmpty()) {
                        adapter.addItems(
                            byteArrayList,
                            fileNameList
                        ) // Pass both byte arrays and file names
                    } else {
                        Toast.makeText(this, "No images selected", Toast.LENGTH_SHORT).show()
                    }
                } ?: run {
                    Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show()
                }
            }
        }

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

    private fun getFileName(uri: Uri): String {
        var name = "unknown"
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (it.moveToFirst()) {
                name = it.getString(nameIndex)
            }
        }
        return name
    }

    private fun setupBackButton() {
        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun setupSpinner() {
        val spinner = binding.spinnerMatchStatus
        val matchStatuses =
            listOf("First Half", "Half Time", "Second Half", "Match Over", "Cancelled")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, matchStatuses)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun setupSaveButton(fixtureId: Long) {
        binding.btnSave.setOnClickListener {
            // Now you can directly use the fixtureId that was passed
            saveTimesheet(fixtureId)
        }
    }

    private fun saveTimesheet(fixtureId: Long) {
        val meetingTime = binding.txtMeetTime.text.toString()
        val busDepartureTime = binding.txtDepTime.text.toString()
        val busReturnTime = binding.txtArrTime.text.toString()
        val message = binding.txtHomeTeam.text.toString()
        val matchStatusText = binding.spinnerMatchStatus.selectedItem.toString()
        val matchStatusMap = mapOf(
            "First Half" to 1,
            "Half Time" to 2,
            "Second Half" to 3,
            "Match Over" to 4,
            "Cancelled" to 5
        )
        val matchStatusValue = matchStatusMap[matchStatusText] ?: 0

        val dbHelper = DBHelper(this)
        val isSaved = dbHelper.addTimes(
            meetingTime,
            busDepartureTime,
            busReturnTime,
            message,
            matchStatusValue,
            fixtureId // Use the fixture ID passed from the intent
        )

        if (isSaved) {
            Toast.makeText(this, "Timesheet saved successfully", Toast.LENGTH_SHORT).show()
            // Navigate back to AdminSportsFixture activity
            val intent = Intent(this, AdminSportsFixtures::class.java)
            startActivity(intent)
            finish() // Optional: Call finish() if you want to remove this activity from the back stack
        } else {
            Toast.makeText(this, "Failed to save timesheet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupTimePickers() {
        // Set up click listener for meeting time
        binding.txtMeetTime.setOnClickListener {
            showTimePickerDialog(binding.txtMeetTime)
        }

        // Set up click listener for bus departure time
        binding.txtDepTime.setOnClickListener {
            showTimePickerDialog(binding.txtDepTime)
        }

        // Set up click listener for bus arrival time
        binding.txtArrTime.setOnClickListener {
            showTimePickerDialog(binding.txtArrTime)
        }
    }

    private fun showTimePickerDialog(editText: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            // Format the time and set it to the EditText
            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            editText.setText(formattedTime)
        }, hour, minute, true)

        timePickerDialog.show()
    }
}