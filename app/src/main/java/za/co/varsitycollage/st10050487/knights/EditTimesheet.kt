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
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import za.co.varsitycollage.st10050487.knights.databinding.ActivityEditTimesheetBinding
import java.util.Calendar

class EditTimesheet : AppCompatActivity() {
    private lateinit var binding: ActivityEditTimesheetBinding
    private lateinit var dbHelper: DBHelper
    private lateinit var adapter: MultipleImageAdapter
    private var fixtureId: Int = 1
    private var userId: Int = 1
    private var timesheetID: Int = 0
    private var isGetItemsCalled = false

    private var imageByteArrayList = mutableListOf<ByteArray?>()
    private var fileNameList = mutableListOf<String?>()

    private val matchStatuses = listOf(
        "First Half",
        "Half Time",
        "Second Half",
        "Match Over",
        "Cancelled"
    )

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

        dbHelper = DBHelper(this)

        adapter = MultipleImageAdapter()
        binding.rvHighlights.layoutManager = LinearLayoutManager(this)
        binding.rvHighlights.adapter = adapter

        fixtureId = intent.getIntExtra("FIXTURE_ID", -1)
        Log.d("EditTimesheet", "Retrieved Fixture ID: $fixtureId")

        if (fixtureId != -1) {
            loadTimesheet(fixtureId)
        } else {
            Toast.makeText(this, "Invalid fixture ID", Toast.LENGTH_SHORT).show()
            finish()
        }

        setupSpinner()

        binding.btnBack.setOnClickListener { finish() }
        binding.btnSave.setOnClickListener {
            if (validateInputs()) {
                updateTimesheetData()
            }
        }

        ImageUpload()
        setupTimePickers()
    }

    private fun setupSpinner() {
        val spinner: Spinner = binding.spinnerMatchStatus
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, matchStatuses)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun loadTimesheet(fixtureId: Int) {
        val timesheet = dbHelper.getTimesDetails(fixtureId)

        Log.d("EditTimesheet", "Loaded Timesheet: $timesheet")

        if (timesheet != null) {
            timesheetID = timesheet.timeId
            binding.txtMsg.setText(timesheet.message)
            binding.txtMeetTime.setText(timesheet.meetTime)
            binding.txtDepTime.setText(timesheet.busDepartureTime)
            binding.txtReturnTime.setText(timesheet.busReturnTime)
            binding.txtHomeResult.setText(timesheet.homeScore?.toString())
            binding.txtAwayResult.setText(timesheet.awayScore?.toString())
            binding.txtManOfTheMatch.setText(timesheet.manOfTheMatch)

            val matchStatusValue = timesheet.matchstatus
            val matchStatusText =
                matchStatusMap.entries.firstOrNull { it.value == matchStatusValue }?.key
            if (matchStatusText != null) {
                setSpinner(binding.spinnerMatchStatus, matchStatuses, matchStatusText)
            }

            loadHighlights(timesheet.timeId)
        } else {
            Toast.makeText(this, "Failed to load timesheet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadHighlights(timesheetId: Int) {
        val highlights = dbHelper.getHighlights(timesheetId)
        imageByteArrayList.clear() // Clear the list before adding new highlights
        highlights.forEach { highlight ->
            imageByteArrayList.add(highlight)
        }
        adapter.updateItems(imageByteArrayList) // Use the new updateItems method
    }

    private fun updateTimesheetData() {
        val meetTime = binding.txtMeetTime.text.toString()
        val busDepartureTime = binding.txtDepTime.text.toString()
        val busReturnTime = binding.txtReturnTime.text.toString()
        val message = binding.txtMsg.text.toString()
        val matchStatusText = binding.spinnerMatchStatus.selectedItem.toString()
        val matchStatusValue = matchStatusMap[matchStatusText] ?: 0
        val homeScore = binding.txtHomeResult.text.toString().toIntOrNull()
        val awayScore = binding.txtAwayResult.text.toString().toIntOrNull()
        val manOfTheMatch = binding.txtManOfTheMatch.text.toString()

        val timesheet = TimesheetModel(
            timeId = timesheetID,
            fixtureId = fixtureId,
            meetTime = if (meetTime.isNotBlank()) meetTime else null,
            busDepartureTime = if (busDepartureTime.isNotBlank()) busDepartureTime else null,
            busReturnTime = if (busReturnTime.isNotBlank()) busReturnTime else null,
            message = if (message.isNotBlank()) message else null,
            homeScore = homeScore,
            awayScore = awayScore,
            manOfTheMatch = if (manOfTheMatch.isNotBlank()) manOfTheMatch else null,
            matchstatus = matchStatusValue
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

    private fun saveHighlights() {
        val dbHelper = DBHelper(this)
        if (imageByteArrayList.isNotEmpty()) {
            Toast.makeText(this, "Highlights updated successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed to update highlights", Toast.LENGTH_SHORT).show()
        }
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
                } else {
                    Toast.makeText(this, "Failed to delete timesheet", Toast.LENGTH_SHORT).show()
                }
            }
            builder.setNegativeButton("No") { dialog: DialogInterface, which: Int ->
                dialog.dismiss()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        } else {
            Toast.makeText(this, "Invalid timesheet ID", Toast.LENGTH_SHORT).show()
        }
    }

    private fun ImageUpload() {
        binding.uploadBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                it.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
            imageLauncher.launch(Intent.createChooser(intent, "Select Picture"))
        }
    }

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
                            )
                        }
                    } ?: data.data?.let { uri ->
                        uris.add(uri)
                        filenames.add(getFilenameFromUri(this, uri))
                        imageByteArrayList.add(contentResolver.openInputStream(uri)?.readBytes())
                    }

                    if (uris.isNotEmpty()) {
                        adapter.updateItems(imageByteArrayList) // Update the adapter with new items
                        saveHighlights()
                    } else {
                        Toast.makeText(this, "No images selected", Toast.LENGTH_SHORT).show()
                    }
                } ?: run {
                    Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun getFilenameFromUri(context: Context, uri: Uri): String? {
        val fileName: String?
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        fileName = cursor?.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
        cursor?.close()
        return fileName
    }

    private fun setupTimePickers() {
        binding.txtMeetTime.setOnClickListener { showTimePickerDialog(binding.txtMeetTime) }
        binding.txtDepTime.setOnClickListener { showTimePickerDialog(binding.txtDepTime) }
        binding.txtReturnTime.setOnClickListener { showTimePickerDialog(binding.txtReturnTime) }
    }

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
        return true
    }
}