package za.co.varsitycollage.st10050487.knights

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AdminEventListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: EventAdapter
    private lateinit var dbHelper: DBHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_admin_event_list, container, false)

        // Initialize the RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView)
        dbHelper = DBHelper(requireContext())

        // Set up the RecyclerView
        setupRecyclerView()

        return view
    }

    private fun setupRecyclerView() {
        // Retrieve events from the database
        val events = dbHelper.getAllEvents() // Ensure this method returns a List<EventModel>

        // Initialize the adapter
        eventAdapter =
            EventAdapter(events.toMutableList(), {}, {}) // Pass empty lambdas for delete options

        // Set the layout manager and adapter for the RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = eventAdapter
    }

    override fun onResume() {
        super.onResume()
        // Refresh the RecyclerView data if needed
        setupRecyclerView()
    }
}