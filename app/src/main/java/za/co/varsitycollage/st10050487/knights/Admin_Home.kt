package za.co.varsitycollage.st10050487.knights

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Admin_Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Example bitmap resource
        val imageView: ImageView = findViewById(R.id.imageView3)
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.dash_banner)

        // Get bitmap size
        val bitmapSize = getBitmapSize(bitmap)
        Log.d("Admin_Home", "Bitmap size: $bitmapSize bytes")

        // Get max memory
        val maxMemory = getMaxMemory()
        Log.d("Admin_Home", "Max memory: $maxMemory bytes")

        // Calculate max bitmap size
        val maxBitmapSize = calculateMaxBitmapSize(maxMemory)
        Log.d("Admin_Home", "Max bitmap size: $maxBitmapSize bytes")

        // Set the bitmap to the ImageView
        imageView.setImageBitmap(bitmap)
    }

    private fun getBitmapSize(bitmap: Bitmap): Int {
        return bitmap.byteCount
    }

    private fun getMaxMemory(): Long {
        return Runtime.getRuntime().maxMemory()
    }

    private fun calculateMaxBitmapSize(maxMemory: Long): Int {
        // Use 1/8th of the available memory for the bitmap
        return (maxMemory / 8).toInt()
    }
}