package za.co.varsitycollage.st10050487.knights

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView



class PlayerProfileAdapter(private val profiles: List<PlayerProfileView>) :
    RecyclerView.Adapter<PlayerProfileAdapter.ProfileViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_player_profile, parent, false)
        return ProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val profile = profiles[position]
        holder.tvName.text = profile.name
        holder.tvEmail.text = "Email: ${profile.email}"
        holder.tvGradeAge.text = "${profile.grade}, Age: ${profile.age}"
    }

    override fun getItemCount() = profiles.size

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvEmail: TextView = itemView.findViewById(R.id.tv_email)
        val tvGradeAge: TextView = itemView.findViewById(R.id.tv_grade_age)
        val ivProfile: ImageView = itemView.findViewById(R.id.iv_profile)
    }
}