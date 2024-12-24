package za.co.varsitycollage.st10050487.knights

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
    private var allEvents: List<EventModel> = listOf() // Store all events

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
        allEvents = dbHelper.getAllEvents() // Store all events
        eventAdapter = EventAdapter(allEvents.toMutableList(), {}, {}) // Initialize with all events

        // Set the layout manager and adapter for the RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = eventAdapter
    }

    fun filterEvents(query: String) {
        val filteredEvents = if (query.isEmpty()) {
            allEvents // Show all events if the query is empty
        } else {
            allEvents.filter { event ->
                event.eventName?.contains(query, ignoreCase = true)
                    ?: false // Handle nullable eventName
            }
        }
        eventAdapter.updateEvents(filteredEvents.toMutableList()) // Update the adapter with filtered events
    }
}