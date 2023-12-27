package com.example.quizgamev2.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.quizgamev2.model.QuizModel
import com.example.quizgamev2.model.ResultModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.tasks.await

class QuizRepository(var onQuestionLoad: OnQuestionLoad) {
    var database = Firebase.database
    var listQuestions = mutableListOf<QuizModel>()
//    var currentListQuestions = MutableLiveData<List<QuizModel>>()

    fun readQuestions() {
//        database.getReference("test").get().addOnSuccessListener {
//            Log.e("mycodeisblocking", "Got value ${it.value.toString()}")
//        }.addOnFailureListener{
//            Log.e("mycodeisblocking", "Error getting data", it)
//        }
        database.reference.child("questions").get()
            .addOnSuccessListener {
                listQuestions.clear()
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
                onQuestionLoad.onLoad(listQuestions)
//                Log.e("mycodeisblocking","ajdsfkls ${currentListQuestions.value?.get(0)?.q}")
            }
            .addOnFailureListener {
                Log.e("mycodeisblocking", "Error getting data")
            }
    }

    interface OnQuestionLoad{ // tạo interface
        fun onLoad(listQuestions: MutableList<QuizModel>)
    }
}