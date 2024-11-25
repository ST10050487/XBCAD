package za.co.varsitycollage.st10050487.knights

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val ARG_FIXTURE_ID = "fixture_id"

class PlayerListFragment : Fragment() {
    private var fixtureId: Int? = null
    private lateinit var dbHelper: DBHelper
    private lateinit var playerAdapter: PlayerListAdapter
    private lateinit var recyclerView: RecyclerView

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
        recyclerView = view.findViewById(R.id.playerRV)
        recyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fixtureId?.let {
            dbHelper = DBHelper(requireContext())
            val allPlayers = dbHelper.getAllPlayers()
            val fixturePlayers = dbHelper.getAllFixturePlayers(it)

            // Create a combined list without duplicates, preferring fixturePlayers
            val combinedPlayers =
                (fixturePlayers + allPlayers).distinctBy { player -> player.playerId }
                    .toMutableList()

            playerAdapter = PlayerListAdapter(
                requireContext(),
                combinedPlayers,
                { count -> (activity as? GetPlayers)?.updateSelectButton(count) }
            )
            recyclerView.adapter = playerAdapter

            // Calculate the initial count of selected players
            val initialSelectedCount = combinedPlayers.count { player -> player.selected }
            // Update the select button with the initial count
            (activity as? GetPlayers)?.updateSelectButton(initialSelectedCount)
        }
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