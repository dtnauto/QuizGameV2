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

class QuizViewModel: ViewModel(){
    var repository = QuizRepository()

    private val _currentListQuestions = MutableLiveData<List<QuizModel>>()
    var currentListQuestions: LiveData<List<QuizModel>> = _currentListQuestions

    fun readQuestions() {
        repository.readQuestions(
            onQuestionLoad = { listQuestions ->
                _currentListQuestions.postValue(listQuestions)
            },
        )
    }
}