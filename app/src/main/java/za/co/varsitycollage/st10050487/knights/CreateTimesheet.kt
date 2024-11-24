package za.co.varsitycollage.st10050487.knights

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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

        // Setup image upload
        ImageUpload()
        setupBackButton()
        setupSpinner()
        setupSaveButton()
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
                        adapter.addItems(byteArrayList, fileNameList) // Pass both byte arrays and file names
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

    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            val fixtureId = CreateSportFixture.fixtureID
            if (fixtureId == -1L) {
                Toast.makeText(
                    this,
                    "You need to create a sports fixture first",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(this, CreateSportFixture::class.java)
                startActivity(intent)
                finish()
            } else {
                saveTimesheet(fixtureId)
            }
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
        dbHelper.addTimes(
            meetingTime,
            busDepartureTime,
            busReturnTime,
            message,
            matchStatusValue,
            fixtureId
        )

        Toast.makeText(this, "Timesheet saved successfully", Toast.LENGTH_SHORT).show()
    }
}