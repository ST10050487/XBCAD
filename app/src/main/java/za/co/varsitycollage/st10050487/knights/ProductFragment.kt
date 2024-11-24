package za.co.varsitycollage.st10050487.knights

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import za.co.varsitycollage.st10050487.knights.DBHelper
import za.co.varsitycollage.st10050487.knights.ProductModel
import za.co.varsitycollage.st10050487.knights.R

class ProductFragment : Fragment() {

    class ProductFragment : Fragment() {

        private lateinit var dbHelper: DBHelper
        private lateinit var productAdapter: ProductAdapter
        private lateinit var recyclerView: RecyclerView

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.product_fragment, container, false)
            recyclerView = activity?.findViewById(R.id.recyclerView) ?: throw IllegalStateException("RecyclerView not found")
            recyclerView.layoutManager = LinearLayoutManager(context)

            dbHelper = DBHelper(requireContext())
            val products = dbHelper.getAllProducts()
            productAdapter = ProductAdapter(products)
            recyclerView.adapter = productAdapter

            return view
        }
    }
}