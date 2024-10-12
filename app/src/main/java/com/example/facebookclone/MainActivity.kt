package com.example.facebookclone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.facebookclone.databinding.ActivityMainBinding
import com.example.facebookclone.utils.Constants



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent.getStringExtra(Constants.USER_ID)

    }
    fun getUser(id:String){

    }
}