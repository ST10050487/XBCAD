package za.co.varsitycollage.st10050487.knights

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ViewProduct : AppCompatActivity() {

    // Declare a variable for the database helper
    private lateinit var dbHelper: DBHelper
    // Declare variables for the user ID and product ID
    private var userId: String? = null
    private var productId: Int? = null
    private var imageHolder: ByteArray? = null
    // Declare variables for UI components
    private lateinit var productImage: ImageView
    private lateinit var productName: TextView
    private lateinit var productPrice: TextView
    private lateinit var productDescription: TextView
    private lateinit var myBackBtn: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_product)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize UI components
        productImage = findViewById(R.id.prodImg)
        productName = findViewById(R.id.prodName)
        productPrice = findViewById(R.id.prodPrice)
        productDescription = findViewById(R.id.ProdDesc)
        myBackBtn = findViewById(R.id.back_btn)
        // Initialize your database helper
        dbHelper = DBHelper(this)

        // Getting the userId from the Intent
        userId = intent.getStringExtra("USER_ID")
        productId = intent.getIntExtra("PRODUCT_ID", -1)

        myBackBtn.setOnClickListener {
            Toast.makeText(this, "Back button clicked", Toast.LENGTH_SHORT).show()
            // Navigate to Store Home Page
            intent = Intent(this, MainActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }

        //Used for testing purposes
        //fillPageWithDummyData()

        // Load the product details
        if(productId != null && productId != -1) {
            loadProductDetails(productId!!)
        }else {
            // Displaying a toast message if the player data is not found
            Toast.makeText(this, "Product Id not transferred", Toast.LENGTH_SHORT).show()
        }

    }

    //-----------------------------------Load Product Details-----------------------------------
    private fun loadProductDetails(productId: Int) {
        val product = dbHelper.getProduct(productId)

        if (product != null) {
            productName.text = product.prodName
            productPrice.text = product.prodPrice.toString()
            productDescription.text = product.prodDescription
            productImage.setImageBitmap( imageHolder?.size?.let { BitmapFactory.decodeByteArray( imageHolder, 0, it) })

        }else {
            // Displaying a toast message if the player data is not found
            Toast.makeText(this, "Product data not found", Toast.LENGTH_SHORT).show()
        }
    }

    //-----------------------------------Dummy Data-----------------------------------
    private fun fillPageWithDummyData() {
        // Set dummy data
        val dummyProductName = "Sample Product"
        val dummyProductPrice = 99.99
        val dummyProductDescription = "This is a sample product description."
        val dummyImageUri = Uri.parse("android.resource://za.co.varsitycollage.st10050487.knights/drawable/bosemansdamhig")

        // Set the dummy data to the UI components
        productName.text = dummyProductName
        productPrice.text = dummyProductPrice.toString()
        productDescription.text = dummyProductDescription

        // Load the dummy image
        val inputStream = contentResolver.openInputStream(dummyImageUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        productImage.setImageBitmap(bitmap)
        inputStream?.close()

        // Log the dummy data
        Log.d("ViewProduct", "Dummy data set: $dummyProductName, $dummyProductPrice, $dummyProductDescription")
    }
}