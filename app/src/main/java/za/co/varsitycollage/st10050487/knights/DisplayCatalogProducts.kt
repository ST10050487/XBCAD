package za.co.varsitycollage.st10050487.knights

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.Button

class DisplayCatalogProducts : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.display_catalog_products)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProductFragment())
                .commit()
        }

        val buttonCreateItem: Button = findViewById(R.id.buttonCreateItem)
        buttonCreateItem.setOnClickListener {
            val intent = Intent(this, CreateProduct::class.java)
            startActivity(intent)
        }

        val backButton: Button = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, HomeScreen::class.java)
            startActivity(intent)
        }
    }
}