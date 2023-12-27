package com.example.quizgamev2.repository

import android.util.Log
import com.example.quizgamev2.model.QuizModel
import com.example.quizgamev2.model.ResultModel
import com.google.firebase.Firebase
import com.google.firebase.database.database

class ResultRepository {

    var database = Firebase.database

    fun writeScores(score: ResultModel) {
//        val key = firebaseRef.push().key // Generate a unique key for each data entry
        database.reference.child("scores").child("correct").setValue(score.correct)
            .addOnSuccessListener {
                // Data successfully pushed
                Log.e("mycodeisblocking", "Data pushed successfully!")
            }
            .addOnFailureListener {
                // Handle error
                Log.e("mycodeisblocking", "Failed to push data: ${it.message}")
            }
        database.reference.child("scores").child("wrong").setValue(score.wrong)
            .addOnSuccessListener {
                // Data successfully pushed
                Log.e("mycodeisblocking", "Data pushed successfully!d")
            }
            .addOnFailureListener {
                // Handle error
                Log.e("mycodeisblocking", "Failed to push data: ${it.message}")
            }
    }

    lateinit var score: ResultModel
    fun readScores() {
//        database.getReference("test").get().addOnSuccessListener {
//            Log.e("mycodeisblocking", "Got value ${it.value.toString()}")
//        }.addOnFailureListener{
//            Log.e("mycodeisblocking", "Error getting data", it)
//        }
        database.reference.child("scores").get()
            .addOnSuccessListener {
                score = ResultModel(
                    correct = it.child("correcet").value.toString().toInt(),
                    wrong = it.child("wrong").value.toString().toInt()
                )
            }
            .addOnFailureListener {
                Log.e("mycodeisblocking", "Error getting data")
            }
    }

    interface OnResultLoad{
        fun OnResultLoad(score: ResultModel)
    }
}