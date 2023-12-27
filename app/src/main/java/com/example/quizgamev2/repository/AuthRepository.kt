package com.example.quizgamev2.repository

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

class AuthRepository(var application: Application) {

    var firebaseAuth = Firebase.auth
    var currentUserLiveData = MutableLiveData<FirebaseUser>()
    fun signUp(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("mycodeisblocking", "createUserWithEmail:success")
                    currentUserLiveData.postValue(firebaseAuth.currentUser)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("mycodeisblocking", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        application,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    fun signIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.e("mycodeisblocking", "signInWithEmail:success")
                    currentUserLiveData.postValue(firebaseAuth.currentUser)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.e("mycodeisblocking", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        application,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    fun signOut() {
        firebaseAuth.signOut()
        currentUserLiveData.postValue(firebaseAuth.currentUser)
    }

    fun signInWithGoogle(idToken: String) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(firebaseCredential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.e("mycodeisblocking", "signInWithEmail:success")
                    currentUserLiveData.postValue(firebaseAuth.currentUser)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.e("mycodeisblocking", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        application,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
}