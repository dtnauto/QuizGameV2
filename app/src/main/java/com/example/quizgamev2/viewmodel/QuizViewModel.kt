package com.example.quizgamev2.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizgamev2.model.QuizModel
import com.example.quizgamev2.model.ResultModel
import com.example.quizgamev2.repository.QuizRepository
import com.example.quizgamev2.repository.ResultRepository
import kotlinx.coroutines.async

class QuizViewModel: ViewModel(), QuizRepository.OnQuestionLoad {
    var repository = QuizRepository(this)
    var currentListQuestions = MutableLiveData<List<QuizModel>>()

    fun readQuestions() {
        repository.readQuestions()
    }

    fun observerCurrentListQuestions(): LiveData<List<QuizModel>> {
        return currentListQuestions
    }

    override fun onLoad(listQuestions: MutableList<QuizModel>) {
        currentListQuestions.value = listQuestions
    }
}