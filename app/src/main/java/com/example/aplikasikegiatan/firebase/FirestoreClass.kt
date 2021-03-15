package com.example.aplikasikegiatan.firebase

import android.app.Activity
import android.util.Log
import com.example.aplikasikegiatan.activities.DashboardActivity
import com.example.aplikasikegiatan.activities.MyProfileActivity
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
        var curentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if (curentUser != null) {
            currentUserId = curentUser.uid
        }
        return currentUserId
    }

    fun loadDataUser(activity: Activity) {
        mFireStore.collection(Constans.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(User::class.java)!!
                when (activity) {
                    is SignInActivity -> {
                        activity.signInSuccess(loggedInUser)
                    }
                    is DashboardActivity -> {
                        activity.updateNavigationUSerDetails(loggedInUser)
                    }
                    is MyProfileActivity -> {
                        activity.setUserDataInUI(loggedInUser)
                    }
                }
            }.addOnFailureListener { e ->
                when (activity) {
                    is SignInActivity -> {
                        activity.hideProggresDialog()
                    }
                    is DashboardActivity -> {
                        activity.hideProggresDialog()
                    }
                }
                Log.e(activity.javaClass.simpleName, "error")
            }
    }

}