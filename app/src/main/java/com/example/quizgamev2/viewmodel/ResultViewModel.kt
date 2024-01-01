package com.example.quizgamev2.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizgamev2.model.QuizModel
import com.example.quizgamev2.model.ResultModel
import com.example.quizgamev2.repository.ResultRepository

class ResultViewModel: ViewModel() {
    var repository = ResultRepository()

    var currentScore = ResultModel(0,0)
    private var _currentScoreLiveData = MutableLiveData<ResultModel>(currentScore)
    val currentScoreLiveData: LiveData<ResultModel> = _currentScoreLiveData

    fun resetAnswer(){
        currentScore.correct = 0
        currentScore.wrong = 0
        _currentScoreLiveData.postValue(currentScore)
    }

    fun checkAnswer(checked: Boolean,num_q: Int){
        if (checked){
            currentScore.correct = currentScore.correct?.plus(num_q)
            Log.e("mycodeisblocking",currentScore.correct.toString())
        }else{
            currentScore.wrong = currentScore.wrong?.plus(num_q)
            Log.e("mycodeisblocking",currentScore.wrong.toString())
        }
        _currentScoreLiveData.postValue(currentScore)
    }

    fun checkAnswer(num_q: Int){
        currentScore.wrong = num_q - currentScore.correct!!
        _currentScoreLiveData.postValue(currentScore)
    }

    //    fun readScores() {
//        repository.readScores()
//    }

    fun writeScores() {
        repository.writeScores(currentScore)
    }
}