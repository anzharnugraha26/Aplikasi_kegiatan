package com.example.aplikasikegiatan.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.aplikasikegiatan.activities.*
import com.example.aplikasikegiatan.models.Board
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


    fun createdBoard(activity: BoardActivity, board: Board) {
        mFireStore.collection(Constans.BOARDS)
            .document()
            .set(board, SetOptions.merge())
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName, "Board Created Successfully")
                Toast.makeText(activity, "Board Created Successfully", Toast.LENGTH_LONG).show()
                activity.boardCreatedSuccessfully()
            }
            .addOnFailureListener { exception ->
                activity.hideProggresDialog()
                Log.e(activity.javaClass.simpleName, "Error Created Successfully", exception)
            }
    }


    fun updateUserProfileData(activity: MyProfileActivity, userHashMap: HashMap<String, Any>) {
        mFireStore.collection(Constans.USERS)
            .document(getCurrentUserId())
            .update(userHashMap)
            .addOnSuccessListener {
                Log.i(activity.javaClass.simpleName, "Profile Data Success updated")
                activity.profileUpdateSuccess()
            }.addOnFailureListener { e ->
                activity.hideProggresDialog()
                Log.e(activity.javaClass.simpleName, "Error while creating a board", e)
                Toast.makeText(activity, "erorr", Toast.LENGTH_LONG).show()
            }
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