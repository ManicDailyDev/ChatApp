package com.example.facebookclone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.facebookclone.Fragment.ChatFragment
import com.example.facebookclone.Fragment.GroupFragment
import com.example.facebookclone.Fragment.ProfileFragment
import com.example.facebookclone.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(ProfileFragment())




        binding.bottomNav.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.profile -> replaceFragment(ProfileFragment())
                R.id.message -> replaceFragment(ChatFragment())
                R.id.group -> replaceFragment(GroupFragment())
                else -> {


                }
            }
            true
        }


    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()

    }
}


//
//        fun getUser(id: String) {
//
//
//        }



