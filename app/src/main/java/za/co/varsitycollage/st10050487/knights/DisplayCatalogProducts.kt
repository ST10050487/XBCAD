package za.co.varsitycollage.st10050487.knights

                    import android.content.Intent
                    import android.os.Bundle
                    import android.widget.Button
                    import androidx.appcompat.app.AppCompatActivity
                    import androidx.recyclerview.widget.LinearLayoutManager
                    import androidx.recyclerview.widget.RecyclerView

                    class DisplayCatalogProducts : AppCompatActivity() {

                        private var roleId: Int = -1
                        private var userId: Int = -1
                        private lateinit var dbHelper: DBHelper
                        private lateinit var productAdapter: ProductAdapter

                        override fun onCreate(savedInstanceState: Bundle?) {
                            super.onCreate(savedInstanceState)
                            setContentView(R.layout.display_catalog_products)

                            // Getting the userId from the Intent
                            userId = intent.getIntExtra("USER_ID", -1)
                            roleId = intent.getIntExtra("ROLE_ID", -1)

                            dbHelper = DBHelper(this)

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

                        override fun onResume() {
                            super.onResume()
                            refreshProductList()
                        }

                        private fun refreshProductList() {
                            val products = dbHelper.getAllProducts()

                            val recyclerView: RecyclerView = findViewById(R.id.product_recycler_view)
                            recyclerView.layoutManager = LinearLayoutManager(this)
                            productAdapter = ProductAdapter(products, userId, dbHelper)
                            recyclerView.adapter = productAdapter
                        }
                    }