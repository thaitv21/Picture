package com.nullexcom.picture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nullexcom.picture.ui.discover.DiscoverFragment
import com.nullexcom.picture.ui.studio.StudioFragment
import com.nullexcom.picture.ui.account.AccountFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_home.*

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val pages = listOf(
            StudioFragment(),
            DiscoverFragment(),
            AccountFragment()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        vpHome.adapter = HomePagerAdapter()
        vpHome.isUserInputEnabled = false
        tabBar.setPageSelected { vpHome.setCurrentItem(it, false) }
    }

    inner class HomePagerAdapter() : FragmentStateAdapter(this) {
        override fun getItemCount(): Int = pages.size
        override fun createFragment(position: Int) = pages[position]
    }
}