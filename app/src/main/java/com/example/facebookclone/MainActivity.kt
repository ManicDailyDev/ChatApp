package com.example.facebookclone

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.facebookclone.Fragment.ChatFragment
import com.example.facebookclone.Fragment.GroupFragment
import com.example.facebookclone.Fragment.ProfileFragment
import com.example.facebookclone.databinding.ActivityMainBinding
import com.example.facebookclone.utils.Constants
import com.example.facebookclone.utils.FirebaseManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(ProfileFragment())

        /* intent */
        val idUser = intent.getStringExtra(Constants.USER_ID)

        /* Buttons bottom navigation */
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

fun getUser(id: String) {


}



