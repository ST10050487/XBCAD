package za.co.varsitycollage.st10050487.knights

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class FixturesAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragmentList = mutableListOf<Fragment>()
    private val fragmentTitleList = mutableListOf<String>()

    fun addFragment(
        fixtureName: String,
        fixtureTime: String,
        fixtureDate: String,
        sportName: String,
        sportCategory: String,
        title: String
    ) {
        val fragment = SportsFixtureFragment.newInstance(fixtureName, fixtureTime, fixtureDate, sportName, sportCategory)
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun getPageTitle(position: Int): CharSequence {
        return fragmentTitleList[position]
    }
}