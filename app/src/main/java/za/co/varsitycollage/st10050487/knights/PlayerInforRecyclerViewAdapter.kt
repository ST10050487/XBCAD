package za.co.varsitycollage.st10050487.knights

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import za.co.varsitycollage.st10050487.knights.databinding.FragmentPlayerInforListBinding

class PlayerInforRecyclerViewAdapter(
    private val values: MutableList<PlayerProfileModel>
) : RecyclerView.Adapter<PlayerInforRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentPlayerInforListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(values[position])
    }

    override fun getItemCount(): Int = values.size

    fun updateData(newValues: List<PlayerProfileModel>) {
        values.clear()
        values.addAll(newValues)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: FragmentPlayerInforListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PlayerProfileModel) {
            binding.name.text = item.name
            binding.surname.text = item.surname
            binding.grade.text = item.grade
            binding.ageGroup.text = item.ageGroup

            // Fetch and display the email
            val dbHelper = DBHelper(binding.root.context)
            val email = dbHelper.getPlayerEmail(item.userId)
            binding.email.text = email // Assuming you have an email TextView in your layout
        }

        override fun toString(): String {
            return super.toString() + " '" + binding.name.text + " " + binding.surname.text + "'"
        }
    }
}