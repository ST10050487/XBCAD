package za.co.varsitycollage.st10050487.knights

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.output.ByteArrayOutputStream

class CreateProduct : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper
    private lateinit var imageView: ImageView
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_PICK = 2
    private var roleId: Int = -1
    private var userPrivileges: String? = null
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_product)

        // Retrieve the ROLE_ID and user privileges from the intent
        roleId = intent.getIntExtra("ROLE_ID", -1)
        userPrivileges = intent.getStringExtra("USER_PRIVILEGES")

        if (roleId == -1) {
            Log.e("HomeScreen", "ROLE_ID not found in intent")
            // Handle the case where the ROLE_ID is not found
        } else {
            Log.d("HomeScreen", "ROLE_ID: $roleId")
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Setup NavigationView and load the header image
        //setupNavigationView(navView)
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, AdminHome::class.java)
                    intent.putExtra("ROLE_ID", roleId)
                    startActivity(intent)
                }
                R.id.nav_sport_management -> {
                    if (roleId == 1 || roleId == 2 || userPrivileges?.contains("SPORT_MANAGEMENT") == true) {
                        val intent = Intent(this, AdminSportsFixtures::class.java)
                        intent.putExtra("ROLE_ID", roleId)
                        startActivity(intent)
                    } else {
                        showToast("Access denied to Sport Management")
                        Log.e("AdminHome", "Access denied to Sport Management")
                    }
                }
                R.id.nav_event_management -> {
                    if (roleId == 1 || roleId == 3 || userPrivileges?.contains("EVENT_MANAGEMENT") == true) {
                        val intent = Intent(this, EventManagement::class.java)
                        intent.putExtra("ROLE_ID", roleId)
                        startActivity(intent)
                    } else {
                        showToast("Access denied to Event Management")
                        Log.e("AdminHome", "Access denied to Event Management")
                    }
                }
                R.id.nav_shop -> {
                    if (roleId == 1 || userPrivileges?.contains("SHOP") == true) {
                        val intent = Intent(this, DisplayCatalogProducts::class.java)
                        intent.putExtra("ROLE_ID", roleId)
                        startActivity(intent)
                    } else {
                        showToast("Access denied to Shop")
                        Log.e("AdminHome", "Access denied to Shop")
                    }
                }
                R.id.nav_profile -> {
                    if (roleId == 1 || roleId == 2 || userPrivileges?.contains("GENERATE_REPORTS") == true) {
                        val intent = Intent(this, PlayerProfileView::class.java)
                        intent.putExtra("ROLE_ID", roleId)
                        startActivity(intent)
                    } else {
                        showToast("Access denied to Player Profile")
                        Log.e("AdminHome", "Access denied to Player Profile")
                    }
                }
                R.id.nav_player_profiles -> {
                    if (roleId == 1 || roleId == 2 || userPrivileges?.contains("PLAYER_PROFILES") == true) {
                        val intent = Intent(this, ViewAllPlayerProfiles::class.java)
                        intent.putExtra("ROLE_ID", roleId)
                        startActivity(intent)
                    } else {
                        showToast("Access denied to Player Profile")
                        Log.e("AdminHome", "Access denied to Player Profiles")
                    }
                }
                R.id.nav_logout -> {
                    val intent = Intent(this, Login::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
            }
            true
        }

        dbHelper = DBHelper(this)
        imageView = findViewById(R.id.prodImage)

        val btnSave = findViewById<Button>(R.id.btnAddProduct)
        btnSave.setOnClickListener {
            createProduct()
        }

        val btnUpload = findViewById<Button>(R.id.uploadBtn)
        btnUpload.setOnClickListener {
            openImagePicker()
        }
    }

    private fun showToast(message: String) {
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_container))

        val text: TextView = layout.findViewById(R.id.toast_text)
        text.text = message

        with(Toast(applicationContext)) {
            duration = Toast.LENGTH_SHORT
            view = layout
            show()
        }
    }

    private fun openImagePicker() {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Select Option")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> openCamera()
                1 -> openGallery()
            }
        }
        builder.show()
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun openGallery() {
        val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    imageView.setImageBitmap(imageBitmap)
                }
                REQUEST_IMAGE_PICK -> {
                    val imageUri: Uri? = data?.data
                    imageView.setImageURI(imageUri)
                }
            }
        }
    }

    private fun createProduct() {
        val titleEditText = findViewById<EditText>(R.id.txtTitle)
        val priceEditText = findViewById<EditText>(R.id.txtPrice)
        val descriptionEditText = findViewById<EditText>(R.id.txtDescription)

        val title = titleEditText.text.toString()
        val price = priceEditText.text.toString().toDoubleOrNull()
        val description = descriptionEditText.text.toString()

        var isValid = true

        if (title.isEmpty()) {
            titleEditText.error = "Title is required"
            isValid = false
        }

        if (price == null) {
            priceEditText.error = "Valid price is required"
            isValid = false
        }

        if (description.isEmpty()) {
            descriptionEditText.error = "Description is required"
            isValid = false
        }

        if (!isValid) {
            return
        }

        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val imageByteArray = stream.toByteArray()

        val userId = 1 // Replace with actual user ID

        val productId = dbHelper.addProduct(title, description, price!!, imageByteArray, userId)
        if (productId != -1L) {
            Toast.makeText(this, "Product created successfully", Toast.LENGTH_SHORT).show()
            clearInputFields()
        } else {
            Toast.makeText(this, "Failed to create product", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearInputFields() {
        findViewById<EditText>(R.id.txtTitle).text.clear()
        findViewById<EditText>(R.id.txtPrice).text.clear()
        findViewById<EditText>(R.id.txtDescription).text.clear()
        imageView.setImageResource(0) // Clear the image
    }
}