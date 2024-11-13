package za.co.varsitycollage.st10050487.knights

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import za.co.varsitycollage.st10050487.knights.databinding.ActivityEditTimesheetBinding
import java.util.Calendar

class EditTimesheet : AppCompatActivity() {
    // Binding object to access views in the layout
    private lateinit var binding: ActivityEditTimesheetBinding
    private lateinit var dbHelper: DBHelper
    private lateinit var adapter: MultipleImageAdapter
    private var fixtureId: Int = 1
    private var userId: Int = 1
    private var timesheetID: Int = 0
    private var isGetItemsCalled = false

    // Lists to store image URIs and filenames
    private var imageByteArrayList = mutableListOf<ByteArray?>()
    private var fileNameList = mutableListOf<String?>()

    // List of match statuses
    private val matchStatuses = listOf(
        "First Half",
        "Half Time",
        "Second Half",
        "Match Over",
        "Cancelled"
    )

    // Map to convert match status text to numeric values
    private val matchStatusMap = mapOf(
        "First Half" to 1,
        "Half Time" to 2,
        "Second Half" to 3,
        "Match Over" to 4,
        "Cancelled" to 5
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditTimesheetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this) // Initialize DBHelper

        // Initialize the RecyclerView and its adapter
        adapter = MultipleImageAdapter()
        binding.rvHighlights.layoutManager = LinearLayoutManager(this)
        binding.rvHighlights.adapter = adapter

        // Retrieve the fixture ID from the intent
        fixtureId = intent.getIntExtra("FIXTURE_ID", -1) // Default value of -1
        Log.d("EditTimesheet", "Retrieved Fixture ID: $fixtureId") // Log the fixture ID

        if (fixtureId != -1) {
            loadTimesheet(fixtureId)
        } else {
            Toast.makeText(this, "Invalid fixture ID", Toast.LENGTH_SHORT).show()
            finish() // Close the activity if the ID is invalid
        }

        // Initialize the Spinner
        setupSpinner() // Call to set up the spinner

        // Set up button listeners
        binding.btnBack.setOnClickListener { finish() }
        binding.btnDelete.setOnClickListener { deleteTimesheet() }
        binding.btnSave.setOnClickListener {
            if (validateInputs()) {
                updateTimesheetData()
            }
        }

        ImageUpload()
        setupTimePickers()
    }

    // Function to set up the Spinner
    private fun setupSpinner() {
        val spinner: Spinner = binding.spinnerMatchStatus // Use binding to get the Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, matchStatuses)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun loadTimesheet(fixtureId: Int) {
        // Retrieve the timesheet details based on the fixture ID
        val timesheet = dbHelper.getTimesDetails(fixtureId)

        Log.d("EditTimesheet", "Loaded Timesheet: $timesheet") // Log the timesheet object

        if (timesheet != null) {
            timesheetID = timesheet.timeId
            binding.txtMsg.setText(timesheet.message)
            binding.txtMeetTime.setText(timesheet.meetTime)
            binding.txtDepTime.setText(timesheet.busDepartureTime)
            binding.txtReturnTime.setText(timesheet.busReturnTime)
            binding.txtHomeResult.setText(timesheet.homeScore?.toString())
            binding.txtAwayResult.setText(timesheet.awayScore?.toString())
            binding.txtManOfTheMatch.setText(timesheet.manOfTheMatch)

            // Set the spinner to the current match status based on its numeric value
            val matchStatusValue = timesheet.matchstatus
            val matchStatusText =
                matchStatusMap.entries.firstOrNull { it.value == matchStatusValue }?.key
            if (matchStatusText != null) {
                setSpinner(binding.spinnerMatchStatus, matchStatuses, matchStatusText)
            }

            // Load existing highlights
            loadHighlights(timesheet.timeId)
        } else {
            Toast.makeText(this, "Failed to load timesheet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadHighlights(timesheetId: Int) {
        val highlights = dbHelper.getHighlights(timesheetId)
        highlights.forEach { highlight ->
            imageByteArrayList.add(highlight)
            fileNameList.add("Highlight ${imageByteArrayList.size}") // Change as needed
        }
        adapter.getItems(
            imageByteArrayList,
            fileNameList
        ) // Assuming you have a method to set the adapter items
    }

    private fun updateTimesheetData() {
        val meetTime = binding.txtMeetTime.text.toString()
        val busDepartureTime = binding.txtDepTime.text.toString()
        val busReturnTime = binding.txtReturnTime.text.toString()
        val message = binding.txtMsg.text.toString()
        val matchStatusText =
            binding.spinnerMatchStatus.selectedItem.toString() // Get selected match status
        val matchStatusValue = matchStatusMap[matchStatusText] ?: 0 // Convert to numeric value
        val homeScore = binding.txtHomeResult.text.toString().toIntOrNull()
        val awayScore = binding.txtAwayResult.text.toString().toIntOrNull()
        val manOfTheMatch = binding.txtManOfTheMatch.text.toString()

        // Create a TimesheetModel with updated values
        val timesheet = TimesheetModel(
            timeId = timesheetID,
            fixtureId = fixtureId,
            meetTime = meetTime,
            busDepartureTime = busDepartureTime,
            busReturnTime = busReturnTime,
            message = message,
            homeScore = homeScore,
            awayScore = awayScore,
            manOfTheMatch = manOfTheMatch,
            matchstatus = matchStatusValue // Store as a number
        )

        val rowsAffected = dbHelper.updateTimesheet(timesheet)
        if (rowsAffected > 0) {
            Toast.makeText(this, "Timesheet updated successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed to update timesheet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setSpinner(spinner: Spinner, items: List<String>, value: String) {
        val index = items.indexOf(value)
        if (index >= 0) {
            spinner.setSelection(index)
        }
    }

    private fun populateSpinner(spinner: Spinner, data: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, data)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun saveHighlights() {
        val dbHelper = DBHelper(this)
        if (imageByteArrayList.isNotEmpty()) {
            // dbHelper.updateHighlights(timesheetID, adapter.returnItems())
            Toast.makeText(this, "Highlights updated successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed to update highlights", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveDummyData() {
        val dbHelper = DBHelper(this)
        dbHelper.addDummyTimesEntry(1)
    }

    private fun deleteTimesheet() {
        if (timesheetID != -1) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Delete Timesheet")
            builder.setMessage("Are you sure you want to delete this Timesheet?")

            builder.setPositiveButton("Yes") { dialog: DialogInterface, which: Int ->
                val dbHelper = DBHelper(this)
                val successTime = dbHelper.deleteTimes(timesheetID)
                val successHighlights = dbHelper.deleteHighlights(timesheetID)
                if (successTime && successHighlights) {
                    Toast.makeText(this, "Timesheet deleted successfully", Toast.LENGTH_SHORT)
                        .show()
                    // finish() // Close the activity
                } else {
                    Toast.makeText(this, "Failed to delete timesheet", Toast.LENGTH_SHORT).show()
                }
            }
            builder.setNegativeButton("No") { dialog: DialogInterface, which: Int ->
                // Dismiss the dialog
                dialog.dismiss()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        } else {
            Toast.makeText(this, "Invalid timesheet ID", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to handle image upload
    private fun ImageUpload() {
        binding.uploadBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                it.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
            imageLauncher.launch(Intent.createChooser(intent, "Select Picture"))
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
                            imageByteArrayList.add(
                                contentResolver.openInputStream(uri)?.readBytes()
                            ) // Add to class-level list
                        }
                    } ?: data.data?.let { uri ->
                        uris.add(uri)
                        filenames.add(getFilenameFromUri(this, uri))
                        imageByteArrayList.add(
                            contentResolver.openInputStream(uri)?.readBytes()
                        ) // Add to class-level list
                    }

                    if (uris.isNotEmpty()) {
                        // Update the adapter with the new highlights
                        adapter.getItems(
                            imageByteArrayList,
                            filenames
                        ) // Assuming you have a method to set the adapter items
                        saveHighlights() // Call to save highlights if needed
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
        binding.txtReturnTime.setOnClickListener { showTimePickerDialog(binding.txtReturnTime) }
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

    private fun validateInputs(): Boolean {
        val message = binding.txtMsg.text.toString().trim()
        val meetTime = binding.txtMeetTime.text.toString().trim()
        val depTime = binding.txtDepTime.text.toString().trim()
        val returnTime = binding.txtReturnTime.text.toString().trim()

        if (message.isEmpty()) {
            binding.txtMsg.error = "Message is required"
            return false
        }

        if (meetTime.isEmpty()) {
            binding.txtMeetTime.error = "Meet time is required"
            return false
        }

        if (depTime.isEmpty()) {
            binding.txtDepTime.error = "Bus departure time is required"
            return false
        }

        if (returnTime.isEmpty()) {
            binding.txtReturnTime.error = "Bus return time is required"
            return false
        }

        return true
    }
}