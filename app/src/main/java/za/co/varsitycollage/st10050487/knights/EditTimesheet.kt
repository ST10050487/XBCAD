package za.co.varsitycollage.st10050487.knights

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import za.co.varsitycollage.st10050487.knights.databinding.ActivityCreateTimesheetBinding
import za.co.varsitycollage.st10050487.knights.databinding.ActivityEditTimesheetBinding
import java.util.Calendar

class EditTimesheet : AppCompatActivity()  {
    // Binding object to access views in the layout
    private lateinit var binding: ActivityEditTimesheetBinding
    private lateinit var btnUpload: Button
    private lateinit var recycleView: RecyclerView
    private lateinit var adapter: MultipleImageAdapter

    // Lists to store image URIs and filenames
    private var imageUriList = mutableListOf<Uri?>()
    private var fileNameList = mutableListOf<String?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Set the content view to the layout
        setContentView(R.layout.activity_edit_timesheet)
        binding = ActivityEditTimesheetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the RecyclerView and its adapter
        adapter = MultipleImageAdapter(imageUriList, fileNameList)
        binding.rvHighlights.layoutManager = LinearLayoutManager(this)
        binding.rvHighlights.adapter = adapter

        // Set up the image upload button and time pickers
        ImageUpload()
        setupTimePickers()
        setupBackButton()
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
                        }
                    } ?: data.data?.let { uri ->
                        uris.add(uri)
                        filenames.add(getFilenameFromUri(this, uri))
                    }

                    if (uris.isNotEmpty()) {
                        adapter.addItems(uris, filenames)
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
}