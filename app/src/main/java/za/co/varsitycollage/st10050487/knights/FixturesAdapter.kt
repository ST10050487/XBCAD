package za.co.varsitycollage.st10050487.knights

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FixturesAdapter(private val fixtures: List<Match>) : RecyclerView.Adapter<FixturesAdapter.FixtureViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FixtureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fixtures_fragment, parent, false)
        return FixtureViewHolder(view)
    }

    override fun onBindViewHolder(holder: FixtureViewHolder, position: Int) {
        val fixture = fixtures[position]
        holder.bind(fixture)
    }

    override fun getItemCount(): Int {
        return fixtures.size
    }

    class FixtureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageLogoHome: ImageView = itemView.findViewById(R.id.imageLogoHome)
        private val imageLogoAway: ImageView = itemView.findViewById(R.id.imageLogoAway)
        private val textView1: TextView = itemView.findViewById(R.id.textView1)
        private val textView2: TextView = itemView.findViewById(R.id.textView2)
        private val textView3: TextView = itemView.findViewById(R.id.textView3)
        private val textView4: TextView = itemView.findViewById(R.id.textView4)

        fun bind(fixture: Match) {
            textView1.text = fixture.startTime
            textView2.text = fixture.date
            textView3.text = fixture.homeTeam
            textView4.text = fixture.awayTeam

            // Set logos from byte arrays
            fixture.homeLogo?.let {
                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                imageLogoHome.setImageBitmap(bitmap)
            }

            fixture.awayLogo?.let {
                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                imageLogoAway.setImageBitmap(bitmap)
            }
        }
    }

}