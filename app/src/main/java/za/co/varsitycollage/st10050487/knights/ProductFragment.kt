package za.co.varsitycollage.st10050487.knights

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProductFragment : Fragment() {

    private lateinit var dbHelper: DBHelper
    private lateinit var productAdapter: ProductAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.display_catalog_products, container, false)
        recyclerView = view.findViewById(R.id.product_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        dbHelper = DBHelper(requireContext())
        val products = dbHelper.getAllProducts()
        Log.d("ProductFragment", "Number of products retrieved: ${products.size}")
        productAdapter = ProductAdapter(products)
        recyclerView.adapter = productAdapter

        return view
    }
}