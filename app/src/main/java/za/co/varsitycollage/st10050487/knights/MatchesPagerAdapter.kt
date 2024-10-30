import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import za.co.varsitycollage.st10050487.knights.pastMatchesFragment
import za.co.varsitycollage.st10050487.knights.upcomingMatchesFragment

// For Adding the MatchPager Adapter for Upcoming and Past Matches
class MatchesPagerAdapter(activity: FragmentActivity, private val isAdmin: Boolean) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> upcomingMatchesFragment(isAdmin) // Pass the isAdmin flag
            1 -> pastMatchesFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}