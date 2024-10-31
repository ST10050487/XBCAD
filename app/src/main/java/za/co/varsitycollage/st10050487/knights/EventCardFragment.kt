package za.co.varsitycollage.st10050487.knights

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EventCardFragment : Fragment() {

    private lateinit var dbHelper: DBHelper
    private lateinit var eventAdapter: EventAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event_card_list, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = DBHelper(requireContext())
        val events = dbHelper.getAllEvents()
        eventAdapter = EventAdapter(events, { show ->
            (activity as? EventManagement)?.showDeleteMenu(show)
        }, { count ->
            (activity as? EventManagement)?.updateDeleteButton(count)
        })
        recyclerView.adapter = eventAdapter
    }

    fun clearSelection() {
        eventAdapter.clearSelection()
    }

    fun deleteSelectedEvents() {
        val selectedEvents = eventAdapter.getSelectedEvents()
        Toast.makeText(requireContext(), "Deleting ${selectedEvents.size} events", Toast.LENGTH_SHORT).show()
        dbHelper.deleteEvents(selectedEvents)
        eventAdapter.removeSelectedEvents()
        (activity as? EventManagement)?.showDeleteMenu(false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            EventCardFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}