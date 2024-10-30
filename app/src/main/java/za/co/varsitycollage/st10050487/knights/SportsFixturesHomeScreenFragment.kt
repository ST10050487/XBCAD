package za.co.varsitycollage.st10050487.knights

import MatchesPagerAdapter
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class SportsFixturesHomeScreenFragment(private val isAdmin: Boolean = false) : Fragment(R.layout.fragment_sports_fixtures_home_screen) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = view.findViewById<ViewPager2>(R.id.view_pager)
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)

        // Pass the isAdmin parameter to the adapter
        val adapter = MatchesPagerAdapter(requireActivity(), isAdmin)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Upcoming Matches"
                1 -> "Past Matches"
                else -> null
            }
        }.attach()
    }
}