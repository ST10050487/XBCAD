package za.co.varsitycollage.st10050487.knights

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.Button

class DisplayCatalogProducts : AppCompatActivity() {

    private var roleId: Int = -1
    private var userId: Int = -1
    private var userPrivileges: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.display_catalog_products)

        // Getting the userId from the Intent
        userId = intent.getIntExtra("USER_ID", -1)
        roleId = intent.getIntExtra("ROLE_ID", -1)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProductFragment())
                .commit()
        }

        val buttonCreateItem: Button = findViewById(R.id.buttonCreateItem)
        buttonCreateItem.setOnClickListener {
            val intent = Intent(this, CreateProduct::class.java)
            intent.putExtra("USER_ID", userId)
            intent.putExtra("ROLE_ID", roleId)
            startActivity(intent)
        }

        val backButton: Button = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, HomeScreen::class.java)
            intent.putExtra("USER_ID", userId)
            intent.putExtra("ROLE_ID", roleId)
            startActivity(intent)
        }
    }
}