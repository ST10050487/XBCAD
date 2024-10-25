package za.co.varsitycollage.st10050487.knights

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class PlayerListAdaptor(
    private val combinedPlayers: MutableList<PlayerProfileModel>,
    private val updateSelectButton: (Int) -> Unit
) : RecyclerView.Adapter<PlayerListAdaptor.PlayerViewHolder>() {

    private val itemSelectedList = mutableListOf<PlayerProfileModel>()

    class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val playerName: TextView = itemView.findViewById(R.id.txtFullname)
        val playerNickname: TextView = itemView.findViewById(R.id.txtNickname)
        val playerPosition: TextView = itemView.findViewById(R.id.txtPosition)
        val playerAge: TextView = itemView.findViewById(R.id.txtAge)
        var checkBox: CheckBox = itemView.findViewById(R.id.checkSelect)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_player_card, parent, false)
        return PlayerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val player = combinedPlayers[position]

        holder.playerName.text = "${player.name} ${player.surname}"
        holder.playerNickname.text = "Nickname: ${player.nickname}"
        holder.playerAge.text = "Age: ${player.age}"
        holder.playerPosition.text = "Position: ${player.position}"

        // Remove listener temporarily to avoid triggering it when setting the state
        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = player.selected

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            toggleSelection(holder, player)
        }

        holder.itemView.findViewById<Button>(R.id.btnEdit).setOnClickListener {
            Toast.makeText(holder.itemView.context, "Edit button clicked for ${player.name}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return combinedPlayers.size
    }

    private fun toggleSelection(holder: PlayerViewHolder, player: PlayerProfileModel) {
        if (player.selected) {
            itemSelectedList.remove(player)
            player.selected = false
        } else {
            itemSelectedList.add(player)
            player.selected = true
        }
        holder.checkBox.isChecked = player.selected

        updateSelectButton(itemSelectedList.size)
        Handler(Looper.getMainLooper()).post {
            notifyItemChanged(combinedPlayers.indexOfFirst { it.playerId == player.playerId })
        }
    }

    fun getSelectedPlayers(): List<PlayerProfileModel> {
        return itemSelectedList
    }

    fun clearSelection() {
        for (player in combinedPlayers) {
            player.selected = false
        }
        itemSelectedList.clear()
        updateSelectButton(0)
        notifyDataSetChanged()
    }
}