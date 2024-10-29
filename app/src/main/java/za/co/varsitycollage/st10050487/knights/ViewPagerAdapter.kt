package za.co.varsitycollage.st10050487.knights

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PlayerListFragment()
            1 -> OverviewFragment()
            else -> PlayerListFragment()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}