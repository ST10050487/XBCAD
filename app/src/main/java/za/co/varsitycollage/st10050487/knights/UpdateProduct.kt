package za.co.varsitycollage.st10050487.knights

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import za.co.varsitycollage.st10050487.knights.databinding.ActivityUpdateProductBinding
import java.io.ByteArrayOutputStream

class UpdateProduct : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProductBinding
    private lateinit var dbHelper: DBHelper
    private var imageHolder: ByteArray? = null
    private var productId: Int = 1
    private var userId: Int = 0
    private val PICK_IMAGE_REQUEST = 1
    private val CAMERA_REQUEST = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)
        // Insert dummy data
//        val newProductId = dbHelper.insertProduct(userId)
//        Toast.makeText(this, "New Product ID: $newProductId", Toast.LENGTH_SHORT).show()

        // productId and userId are passed via Intent
        // productId = intent.getIntExtra("PRODUCT_ID", 0)
        // userId = intent.getIntExtra("USER_ID", 0)

        loadProductDetails()

        binding.uploadBtn.setOnClickListener {
            showImagePickerOptions()
        }

        binding.btnSave.setOnClickListener {
            if (validateInputs()) {
                updateProductData()
            }
        }
    }

    private fun loadProductDetails() {
        // Fetch product details from the database
        val product = dbHelper.getProduct(productId)
        // Set the product details to the UI
        product?.let {
            binding.txtTitle.setText(it.prodName)
            binding.txtPrice.setText(String.format("%.2f", it.prodPrice))
            binding.txtDescription.setText(it.prodDescription)
            // Set the product picture to image holder variable
            imageHolder = product?.prodPicture

            // if product picture is not null
           if (imageHolder != null)
           { // set UI image to product picture
                binding.prodImage.setImageBitmap(imageHolder?.size?.let { BitmapFactory.decodeByteArray(imageHolder, 0, it) })
            }

        }

    }

    private fun updateProdPicture(bitmap: Bitmap) {
        binding.prodImage.setImageBitmap(bitmap)
        // assign the bitmap to the prodBlob variable using the bitmapToByteArray function to convert it to a byte array
        imageHolder = bitmapToByteArray(bitmap)
    }

    private fun updateProductData() {
        val name = binding.txtTitle.text.toString()
        val description = binding.txtDescription.text.toString()
        val price = binding.txtPrice.text.toString().toDoubleOrNull() ?: 0.0

        val product = ProductModel(
            prodId = productId,
            userId = userId,
            prodName = name,
            prodDescription = description,
            prodPrice = price,
            prodPicture = imageHolder
        )

        val result = dbHelper.updateProduct(product)

        if (result > 0) {
            Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed to update product", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInputs(): Boolean {
        val name = binding.txtTitle.text.toString()
        val price = binding.txtPrice.text.toString()
        val description = binding.txtDescription.text.toString()

        if (name.isEmpty()) {
            binding.txtTitle.error = "Product name is required"
            return false
        }
        if (price.isEmpty()) {
            binding.txtPrice.error = "Price is required"
            return false
        }
        if (description.isEmpty()) {
            binding.txtDescription.error = "Description is required"
            return false
        }
        return true
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    val imageUri = data?.data
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                    updateProdPicture(bitmap)
                }
                CAMERA_REQUEST -> {
                    val bitmap = data?.extras?.get("data") as Bitmap
                    updateProdPicture(bitmap)
                }
            }
        }
    }
    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun showImagePickerOptions() {
        val options = arrayOf("Take a Photo", "Choose from Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose Profile Picture")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> checkCameraPermission()
                1 -> openGallery()
            }
        }
        builder.show()
    }
    private fun checkCameraPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST)
            } else {
                openCamera()
            }
        } else {
            openCamera()
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        } else {
            Toast.makeText(this, "Camera permission is required to take a picture.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST)
    }
}