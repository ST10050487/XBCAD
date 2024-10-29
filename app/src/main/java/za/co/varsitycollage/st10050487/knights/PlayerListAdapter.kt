package za.co.varsitycollage.st10050487.knights

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class PlayerListAdapter(private val context: Context, private val players: List<PlayerProfileModel>) : BaseAdapter() {

    override fun getCount(): Int {
        return players.size
    }

    override fun getItem(position: Int): Any {
        return players[position]
    }

    override fun getItemId(position: Int): Long {
        return players[position].playerId.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.fragment_player_list_item, parent, false)

        val player = players[position]

        val playerNameTextView: TextView = view.findViewById(R.id.playerName)
        val playerPositionTextView: TextView = view.findViewById(R.id.playerPosition)
        val playerImageView: ImageView = view.findViewById(R.id.playerImage)

        playerNameTextView.text = "${player.name} ${player.surname}"
        playerPositionTextView.text = player.position
        player.profilePicture?.let {
            playerImageView.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
        }

        return view
    }
}
