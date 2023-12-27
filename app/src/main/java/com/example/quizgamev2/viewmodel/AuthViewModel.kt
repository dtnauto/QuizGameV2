package com.example.quizgamev2.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.quizgamev2.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser

class AuthViewModel(application: Application): AndroidViewModel(application) {
    var repository = AuthRepository(application)
    var currentUser: FirebaseUser? = repository.firebaseAuth.currentUser

    fun obseverCurrentUser(): LiveData<FirebaseUser>{
        return repository.currentUserLiveData
    }

    fun signUp(email: String, password: String) {
        repository.signUp(email, password)
    }

    fun signIn(email: String, password: String) {
        repository.signIn(email, password)
    }

    fun signOut() {
        repository.signOut()
    }

    fun signInWithGoogle(idToken: String) {
        repository.signInWithGoogle(idToken)
    }
}