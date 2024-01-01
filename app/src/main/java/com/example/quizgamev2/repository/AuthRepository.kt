package com.example.quizgamev2.repository

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

class AuthRepository {

    var firebaseAuth = Firebase.auth
    fun signUp(
        email: String,
        password: String,
        onComplete: (FirebaseUser?) -> Unit,
        onError: (String) -> Unit
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    onComplete(user)
                } else {
                    onError("Authentication failed")
                }
            }
    }

    fun signIn(
        email: String,
        password: String,
        onComplete: (FirebaseUser?) -> Unit,
        onError: (String) -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    onComplete(user)
                } else {
                    onError("Authentication failed")
                }
            }
    }

    fun signOut(
        onComplete: (FirebaseUser?) -> Unit,
        onError: (String) -> Unit
    ) {
        firebaseAuth.signOut()
        val user = firebaseAuth.currentUser
        onComplete(user)
//        currentUserLiveData.postValue(firebaseAuth.currentUser)
    }

    fun signInWithGoogle(
        idToken: String,
        onComplete: (FirebaseUser?) -> Unit,
        onError: (String) -> Unit
    ) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(firebaseCredential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    onComplete(user)
                } else {
                    onError("Authentication failed")
                }
            }
    }

    fun sendEmailToResetPassword(email: String?): Boolean {
        if (email != null && email.isNotEmpty()) {
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("SendEmailResetPassword", "Email sent.")
                    }
                }
            return true
        } else {
            return false
        }
    }
}