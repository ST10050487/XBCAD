package za.co.varsitycollage.st10050487.knights

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class PlayerListAdapter(
    private val context: Context,
    private val players: List<PlayerProfileModel>,
    private val updateSelectButton: (Int) -> Unit
) : RecyclerView.Adapter<PlayerListAdapter.PlayerViewHolder>() {

    private val selectedPlayers = mutableListOf<PlayerProfileModel>()
    init {
        // Populate the selectedPlayers list with players where selected is true
        selectedPlayers.addAll(players.filter { it.selected })
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_player_list_item, parent, false)

        return PlayerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val player = players[position]
        holder.bind(player)
    }

    override fun getItemCount(): Int {
        return players.size
    }

    fun getSelectedPlayers(): List<PlayerProfileModel> {
        return selectedPlayers
    }

    fun clearSelection() {
        selectedPlayers.clear()
        notifyDataSetChanged()
        updateSelectButton(selectedPlayers.size)
    }
    inner class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val playerNameTextView: TextView = itemView.findViewById(R.id.playerName)
        private val playerPositionTextView: TextView = itemView.findViewById(R.id.playerPosition)
        private val playerImageView: ImageView = itemView.findViewById(R.id.playerImage)
        private val playerCheckBox: CheckBox = itemView.findViewById(R.id.playerCheckBox)

        fun bind(player: PlayerProfileModel) {
            playerNameTextView.text = "${player.name} ${player.surname}"
            playerPositionTextView.text = player.position
            player.profilePicture?.let {
                playerImageView.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
            }
            if (selectedPlayers.contains(player)) {
                playerCheckBox.isChecked = true
            } else {
                playerCheckBox.isChecked = false
            }

            // Make the CheckBox visible
            playerCheckBox.visibility = View.VISIBLE

            itemView.setOnClickListener {
                if (selectedPlayers.contains(player)) {
                    selectedPlayers.remove(player)
                    playerCheckBox.isChecked = false
                } else {
                    selectedPlayers.add(player)
                    playerCheckBox.isChecked = true
                }
                updateSelectButton(selectedPlayers.size)
            }
        }
    }

//class PlayerListAdapter(
//    private val context: Context,
//    private val players: List<PlayerProfileModel>,
//    private val updateSelectButton: (Int) -> Unit
//) : BaseAdapter() {
//
//    override fun getCount(): Int {
//        return players.size
//    }
//
//    override fun getItem(position: Int): Any {
//        return players[position]
//    }
//
//    override fun getItemId(position: Int): Long {
//        return players[position].playerId.toLong()
//    }
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.fragment_player_list_item, parent, false)
//
//        val player = players[position]
//
//        val playerNameTextView: TextView = view.findViewById(R.id.playerName)
//        val playerPositionTextView: TextView = view.findViewById(R.id.playerPosition)
//        val playerImageView: ImageView = view.findViewById(R.id.playerImage)
//
//        playerNameTextView.text = "${player.name} ${player.surname}"
//        playerPositionTextView.text = player.position
//        player.profilePicture?.let {
//            playerImageView.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
//        }
//
//        return view
//    }
}
