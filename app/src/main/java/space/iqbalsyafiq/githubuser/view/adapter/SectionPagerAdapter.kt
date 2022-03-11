package space.iqbalsyafiq.githubuser.view.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import space.iqbalsyafiq.githubuser.view.fragment.ListFragment

class SectionPagerAdapter(
    fragment: Fragment,
    private val username: String
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = ListFragment()

        fragment.arguments = Bundle().apply {
            putInt(ListFragment.ARG_SECTION_NUMBER, position + 1)
            putString(ListFragment.ARG_USERNAME, username)
        }

        return fragment
    }
}