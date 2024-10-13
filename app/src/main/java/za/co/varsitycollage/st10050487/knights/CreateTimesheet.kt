package za.co.varsitycollage.st10050487.knights

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import za.co.varsitycollage.st10050487.knights.databinding.ActivityCreateTimesheetBinding

class CreateTimesheet : AppCompatActivity() {
    private lateinit var binding: ActivityCreateTimesheetBinding
    private lateinit var btnUpload: Button
    private lateinit var recycleView: RecyclerView

    private var imageUriList = mutableListOf<Uri?>()
    private var fileNameList = mutableListOf<String?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_create_timesheet)
        binding = ActivityCreateTimesheetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnUpload = binding.uploadBtn

        btnUpload.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                it.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                imageLauncher.launch(Intent.createChooser(it, "Select Picture"))
            }
        }
    }

    private val imageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.clipData?.let {
                val clipDataCount = it.itemCount
                for (clipPosition in 0 until clipDataCount) {
                    val uri = it.getItemAt(clipPosition).uri
                    val filename = getFilenameFromUri(this, uri)
                    addItems(uri, filename)
                }
                setUpRecyclerView()
            } ?: run {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.rvHighlights.apply {
            layoutManager = LinearLayoutManager(this@CreateTimesheet, RecyclerView.HORIZONTAL, false)
            adapter = MultipleImageAdapter(imageUriList, fileNameList)
        }

        // to clear list
        imageUriList = mutableListOf()
        fileNameList = mutableListOf()
    }

    private fun addItems(imageUri: Uri?, fileName: String?) {
        imageUriList.add(imageUri)
        fileNameList.add(fileName)
    }

    private fun getFilenameFromUri(context: Context, uri: Uri): String? {
        val fileName: String?
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        fileName = cursor?.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
        cursor?.close()
        return fileName
    }
}
//https://www.youtube.com/watch?v=t-jRw9XYgNY