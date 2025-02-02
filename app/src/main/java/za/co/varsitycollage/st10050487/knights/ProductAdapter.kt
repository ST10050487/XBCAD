package za.co.varsitycollage.st10050487.knights

                            import android.content.Intent
                            import android.graphics.Bitmap
                            import android.graphics.BitmapFactory
                            import android.util.Log
                            import android.view.LayoutInflater
                            import android.view.View
                            import android.view.ViewGroup
                            import android.widget.Button
                            import android.widget.ImageView
                            import android.widget.TextView
                            import androidx.recyclerview.widget.RecyclerView

                            class ProductAdapter(
                                private var products: List<ProductModel>,
                                private val userId: Int,
                                private val dbHelper: DBHelper // Add dbHelper as a parameter
                            ) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

                                class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
                                    val productName: TextView = view.findViewById(R.id.productName)
                                    val productDescription: TextView = view.findViewById(R.id.productDiscription)
                                    val productPrice: TextView = view.findViewById(R.id.productPrice)
                                    val productImage: ImageView = view.findViewById(R.id.productImage)
                                    val editButton: Button = view.findViewById(R.id.editButton)
                                }

                                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
                                    val view = LayoutInflater.from(parent.context)
                                        .inflate(R.layout.product_fragment, parent, false)
                                    return ProductViewHolder(view)
                                }

                                override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
                                    val product = products[position]
                                    holder.productName.text = product.prodName
                                    holder.productDescription.text = product.prodDescription
                                    holder.productPrice.text = " R " + product.prodPrice.toString()

                                    // Load the photo asynchronously
                                    Thread {
                                        val photo = dbHelper.getProductPhoto(product.prodId)
                                        holder.itemView.post {
                                            holder.productImage.setImageBitmap(convertByteArrayToBitmap(photo))
                                        }
                                    }.start()

                                    holder.editButton.setOnClickListener {
                                        val context = holder.itemView.context
                                        val intent = Intent(context, UpdateProduct::class.java)
                                        intent.putExtra("PRODUCT_ID", product.prodId)
                                        intent.putExtra("USER_ID", userId)
                                        context.startActivity(intent)
                                    }
                                }

                                override fun getItemCount(): Int {
                                    return products.size
                                }

                                fun updateProducts(newProducts: List<ProductModel>) {
                                    products = newProducts
                                    notifyDataSetChanged()
                                }

                                private fun convertByteArrayToBitmap(byteArray: ByteArray?): Bitmap? {
                                    return byteArray?.let {
                                        BitmapFactory.decodeByteArray(it, 0, it.size)
                                    }
                                }
                            }