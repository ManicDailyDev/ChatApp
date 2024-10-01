package com.example.facebookclone.Chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.facebookclone.Fragment.GroupChatFragment
import com.example.facebookclone.Fragment.MessageFragment
import com.example.facebookclone.Fragment.ProfileFragment
import com.example.facebookclone.R
import com.example.facebookclone.databinding.ActivityChatHolderBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatHolderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatHolderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firstFragment = ProfileFragment()
        val secondFragment = MessageFragment()
        val thirdFragment = GroupChatFragment()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.container, firstFragment)
                commit()
            }
        }

        // BottomNavigationView button clicks
        binding.bottomnavigationbuttons.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profile -> {
                    replaceFragment(firstFragment)
                    true
                }
                R.id.message -> {
                    replaceFragment(secondFragment)
                    true
                }
                R.id.group -> {
                    replaceFragment(thirdFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            addToBackStack(null) // Optional: Add transaction to back stack
            commit()
        }
    }
}


