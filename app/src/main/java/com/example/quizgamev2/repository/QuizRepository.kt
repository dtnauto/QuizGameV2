package com.example.quizgamev2.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.quizgamev2.model.QuizModel
import com.example.quizgamev2.model.ResultModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.tasks.await

class QuizRepository {
    private var database = Firebase.database

    fun readQuestions(
        onQuestionLoad: (MutableList<QuizModel>) -> Unit
    ) {
        database.reference.child("questions").get()
            .addOnSuccessListener {
                var listQuestions = mutableListOf<QuizModel>()
                it.children.forEach {
                    if (it.exists()) {
                        val question = QuizModel(
                            a = it.child("a").value.toString(),
                            answer = it.child("answer").value.toString(),
                            b = it.child("b").value.toString(),
                            c = it.child("c").value.toString(),
                            d = it.child("d").value.toString(),
                            q = it.child("q").value.toString()
                        )
                        listQuestions.add(question)
//                        Log.e("mycodeisblocking","${question}")
                    }
                }
                onQuestionLoad(listQuestions)
            }
            .addOnFailureListener {
                Log.e("mycodeisblocking", "Error getting data")
            }
    }
}