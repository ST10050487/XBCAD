package za.co.varsitycollage.st10050487.knights

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.ViewTreeObserver
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
// Banner frame size: width = 1440, height = 672
        val bannerFrame: ImageView = findViewById(R.id.imageView3)
        bannerFrame.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // Remove the listener to avoid multiple calls
                bannerFrame.viewTreeObserver.removeOnGlobalLayoutListener(this)

                // Get the width and height of the ImageView
                val width = bannerFrame.width
                val height = bannerFrame.height

                // Log the dimensions
                Log.d("Admin_Home", "Banner frame size: width = $width, height = $height")
            }
        })
    }


}