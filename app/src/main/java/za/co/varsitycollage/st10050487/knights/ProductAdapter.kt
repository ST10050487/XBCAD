package za.co.varsitycollage.st10050487.knights

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(private val products: List<ProductModel>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productName: TextView = view.findViewById(R.id.textView18)
        val productDescription: TextView = view.findViewById(R.id.textView19)
        val productPrice: TextView = view.findViewById(R.id.textView19)
        val productImage: ImageView = view.findViewById(R.id.imageView6)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_fragment, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        Log.d("ProductAdapter", "Binding product at position $position: ${product.prodName}")
        holder.productName.text = product.prodName
        holder.productDescription.text = product.prodDescription
        holder.productPrice.text = product.prodPrice.toString()
        holder.productImage.setImageBitmap(convertByteArrayToBitmap(product.prodPicture))
    }

    override fun getItemCount(): Int {
        val count = products.size
        Log.d("ProductAdapter", "Total number of products: $count")
        return count
    }

    private fun convertByteArrayToBitmap(byteArray: ByteArray?): Bitmap? {
        return byteArray?.let {
            BitmapFactory.decodeByteArray(it, 0, it.size)
        }
    }
}