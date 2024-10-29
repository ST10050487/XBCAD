package za.co.varsitycollage.st10050487.knights

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView

private const val ARG_FIXTURE_ID = "fixture_id"

class PlayerListFragment : Fragment() {
    private var fixtureId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fixtureId = it.getInt(ARG_FIXTURE_ID)
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_player_list, container, false)
        val listView: ListView = view.findViewById(R.id.playerList)

        // Fetch and display player list based on fixtureId
        fixtureId?.let {
            val dbHelper = DBHelper(requireContext())
            val players = dbHelper.getAllFixturePlayers(it)
            // Set up the adapter for the ListView
            listView.adapter = PlayerListAdapter(requireContext(), players)
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(fixtureId: Int) =
            PlayerListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_FIXTURE_ID, fixtureId)
                }
            }
    }
}