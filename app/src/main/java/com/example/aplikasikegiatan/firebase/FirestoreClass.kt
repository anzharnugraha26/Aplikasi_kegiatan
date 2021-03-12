package com.example.aplikasikegiatan.firebase

import android.util.Log
import com.example.aplikasikegiatan.activities.SignInActivity
import com.example.aplikasikegiatan.activities.SignUpActivity
import com.example.aplikasikegiatan.models.User

import com.example.aplikasikegiatan.utils.Constans
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions



class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun regiterUser(activity: SignUpActivity, userInfo: User) {
        mFireStore.collection(Constans.USERS)
            .document(getCurrentUserId()).set(userInfo, SetOptions.merge()).addOnSuccessListener {
                activity.userResgisteredSucess()
            }.addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "error")
            }
    }

    fun getCurrentUserId(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    fun signInUser(activity: SignInActivity) {
        mFireStore.collection(Constans.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(User::class.java)!!
                activity.signInSuccess(loggedInUser)

            }.addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "error")
            }
    }

}