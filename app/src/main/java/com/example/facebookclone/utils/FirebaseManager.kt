package com.example.facebookclone.utils

import com.example.facebookclone.DataClasses.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class FirebaseManager {


    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val firebaseStore = FirebaseFirestore.getInstance()


    fun registerUser(user: User, id: String) {
        firebaseStore.collection("users").document(id).set(user)
    }



}
