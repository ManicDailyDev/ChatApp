package com.example.facebookclone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.facebookclone.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* firebase */
        auth = FirebaseAuth.getInstance()

    }
}