package com.sro.stupa.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sro.stupa.Adapter.MyPagerAdapter
import com.sro.stupa.databinding.ActivityHomeBinding
import com.sro.stupa.fragment.FirstFragment
import com.sro.stupa.fragment.SecondFragment
import com.sro.stupa.fragment.ThirdFragment

class Home : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var userName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize UI components
        initUI()
    }

    /**
     * Initializes the UI components and sets up the ViewPager with tabs.
     */
    private fun initUI() {
        // Get the username passed from the previous activity
        userName = intent.getStringExtra("name")!!

        // Display a welcome message with the username
        Toast.makeText(this@Home, "Hi $userName", Toast.LENGTH_SHORT).show()

        // Define titles for the tabs
        val titles = listOf("Tab 1", "Tab 2", "Tab 3")

        // Create a list of fragments
        val fragmentList = listOf(FirstFragment(), SecondFragment(), ThirdFragment())

        // Create an adapter for the ViewPager
        val adapter = MyPagerAdapter(supportFragmentManager, fragmentList, titles)

        // Set the adapter to the ViewPager
        binding.viewPager.adapter = adapter

        // Connect the TabLayout with the ViewPager
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }
}
