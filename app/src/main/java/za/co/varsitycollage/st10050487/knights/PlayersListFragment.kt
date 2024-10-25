package za.co.varsitycollage.st10050487.knights

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PlayersListFragment : Fragment() {

    private lateinit var dbHelper: DBHelper
    private lateinit var playerAdapter: PlayerListAdaptor
    private lateinit var recyclerView: RecyclerView
    private var fixtureId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_players_list, container, false)
        recyclerView = view.findViewById(R.id.playerRV)
        recyclerView.layoutManager = LinearLayoutManager(context)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = DBHelper(requireContext())

        // fixtureId is passed as an argument
        fixtureId = arguments?.getInt("FIXTURE_ID") ?: 0

        val allPlayers = dbHelper.getAllPlayers()
        val fixturePlayers = dbHelper.getAllFixturePlayers(fixtureId)

        // Create a combined list without duplicates, preferring fixturePlayers
        val combinedPlayers = (fixturePlayers + allPlayers).distinctBy { it.playerId }.toMutableList()

        playerAdapter = PlayerListAdaptor(
            combinedPlayers,
            { count -> (activity as? GetPlayers)?.updateSelectButton(count) }
        )
        recyclerView.adapter = playerAdapter

        // Calculate the initial count of selected players
        val initialSelectedCount = combinedPlayers.count { it.selected }
        // Update the select button with the initial count
        (activity as? GetPlayers)?.updateSelectButton(initialSelectedCount)
    }

    fun clearSelection() {
        playerAdapter.clearSelection()
    }

    fun saveSelectedPlayers() {
        val selectedPlayers = playerAdapter.getSelectedPlayers()
        val selectedPlayerIds = selectedPlayers.map { it.playerId }
        val fixtureId = arguments?.getInt("FIXTURE_ID") ?: 0
        dbHelper.updateFixturePlayers(fixtureId, selectedPlayerIds)
        playerAdapter.clearSelection()
    }

    companion object {
        private const val ARG_FIXTURE_ID = "FIXTURE_ID"

        fun newInstance(fixtureId: Int): PlayersListFragment {
            val fragment = PlayersListFragment()
            val args = Bundle()
            args.putInt(ARG_FIXTURE_ID, fixtureId)
            fragment.arguments = args
            return fragment
        }
    }
}