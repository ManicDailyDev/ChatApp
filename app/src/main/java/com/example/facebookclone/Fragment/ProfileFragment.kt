package com.example.facebookclone.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.facebookclone.LoginActivity
import com.example.facebookclone.R
import com.example.facebookclone.utils.Constants
import com.example.facebookclone.utils.Extensions
import com.example.facebookclone.utils.FirebaseManager
import com.example.facebookclone.utils.PrefSingleton

class ProfileFragment : Fragment() {
    private lateinit var prefSingleton: PrefSingleton
    private lateinit var  signOutButton :Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        prefSingleton = PrefSingleton(requireContext())
        signOutButton = view.findViewById(R.id.singoutButton)
        signOutButton.setOnClickListener {
            prefSingleton.saveString(key = Constants.USER_ID, value = "")
            prefSingleton.saveBool(key = Constants.LOGGED_IN, value = false)
            FirebaseManager().singOutUser()
            val intent = Intent(requireContext(),LoginActivity::class.java)
            startActivity(intent)
        }

        return view
    }




}